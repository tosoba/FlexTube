package com.example.there.multifeeds.start

import android.Manifest
import android.accounts.AccountManager
import android.app.Activity
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.example.there.cache.preferences.AppPreferences
import com.example.there.multifeeds.R
import com.example.there.multifeeds.databinding.ActivityStartBinding
import com.example.there.multifeeds.lifecycle.ConnectivityComponent
import com.example.there.multifeeds.lifecycle.DisposablesComponent
import com.example.there.multifeeds.main.MainActivity
import com.example.there.multifeeds.util.di.HasFragmentDispatchingAndroidInjector
import com.example.there.multifeeds.util.ext.defaultConnectivityComponentSnackbarParams
import com.example.there.multifeeds.util.ext.googlePlayServicesAvailable
import com.example.there.multifeeds.util.ext.isConnectedToInternet
import com.google.android.gms.auth.UserRecoverableAuthException
import com.google.android.gms.common.GoogleApiAvailability
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.util.ExponentialBackOff
import com.google.api.services.youtube.YouTubeScopes
import dagger.android.DispatchingAndroidInjector
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_start.*
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import javax.inject.Inject

class StartActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks, HasFragmentDispatchingAndroidInjector {

    @Inject
    override lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    private var credential: GoogleAccountCredential? = null

    private val disposablesComponent = DisposablesComponent()
    private val connectivityComponent: ConnectivityComponent by lazy(LazyThreadSafetyMode.NONE) {
        ConnectivityComponent(
                isDataLoaded = false,
                reloadDataOnConnected = {
                    if (credential == null)
                        credential = GoogleAccountCredential.usingOAuth2(applicationContext, SCOPES)
                                .setBackOff(ExponentialBackOff())
                    checkAuthAndLoadData()
                },
                snackbarParameters = defaultConnectivityComponentSnackbarParams(start_activity_root_layout)
        )
    }

    private val startViewState = StartViewState()

    private val startView: StartView by lazy(LazyThreadSafetyMode.NONE) {
        StartView(startViewState, View.OnClickListener { chooseAccount() })
    }

    @Inject
    lateinit var appPreferences: AppPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityStartBinding>(this, R.layout.activity_start)
        binding.startView = startView

        lifecycle.addObserver(disposablesComponent)
        lifecycle.addObserver(connectivityComponent)

        credential = GoogleAccountCredential.usingOAuth2(applicationContext, SCOPES).setBackOff(ExponentialBackOff())

        checkAuthAndLoadData()
    }

    override fun onStop() {
        super.onStop()
        startViewState.authInProgress.set(false)
    }

    /**
     * Called when an activity launched here (specifically, AccountPicker
     * and authorization) exits, giving you the requestCode you started it with,
     * the resultCode it returned, and any additional data from it.
     * @param requestCode code indicating which activity result is incoming.
     * @param resultCode code indicating the result of the incoming
     * activity result.
     * @param data Intent (containing result data) returned by incoming
     * activity result.
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_GOOGLE_PLAY_SERVICES -> if (resultCode != Activity.RESULT_OK) {
                Toast.makeText(this, getString(R.string.install_google_play_services), Toast.LENGTH_SHORT).show()
            } else {
                checkAuthAndLoadData()
            }
            REQUEST_ACCOUNT_PICKER -> if (resultCode == Activity.RESULT_OK && data != null && data.extras != null) {
                val accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME)
                if (accountName != null) {
                    appPreferences.accountName = accountName
                    credential?.selectedAccountName = accountName
                    checkAuthAndLoadData()
                }
            }
            REQUEST_AUTHORIZATION -> if (resultCode == Activity.RESULT_OK) {
                checkAuthAndLoadData()
            }
            REQUEST_MAIN_ACTIVITY -> {
                appPreferences.accountName = null
                credential?.selectedAccountName = null
                startViewState.splashOnly.set(false)
            }
        }
    }

    /**
     * Respond to requests for permissions at runtime for API 23 and above.
     * @param requestCode The request code passed in
     * requestPermissions(android.app.HomeItem, String, int, String[])
     * @param permissions The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     * which is either PERMISSION_GRANTED or PERMISSION_DENIED. Never null.
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    /**
     * Callback for when a permission is granted using the EasyPermissions
     * library.
     * @param requestCode The request code associated with the requested
     * permission
     * @param list The requested permission list. Never null.
     */
    override fun onPermissionsGranted(requestCode: Int, list: List<String>) = Unit

    /**
     * Callback for when a permission is denied using the EasyPermissions
     * library.
     * @param requestCode The request code associated with the requested
     * permission
     * @param list The requested permission list. Never null.
     */
    override fun onPermissionsDenied(requestCode: Int, list: List<String>) = Toast.makeText(
            this,
            "The following permissions need to be granted: ${list.joinToString(separator = ", ")}",
            Toast.LENGTH_SHORT
    ).show()

    private fun checkAuthAndLoadData() {
        if (!googlePlayServicesAvailable)
            acquireGooglePlayServices()
        else if (credential!!.selectedAccountName == null)
            chooseAccount()
        else if (!isConnectedToInternet)
            Toast.makeText(this, getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show()
        else
            loadAccessTokenAndLoadData()
    }


    private fun loadAccessTokenAndLoadData() {
        startViewState.authInProgress.set(true)
        disposablesComponent.add(Single.fromCallable { credential!!.token }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError { startViewState.authInProgress.set(false) }
                .subscribe({ token ->
                    if (appPreferences.accountName == null) {
                        appPreferences.accountName = credential!!.selectedAccountName
                    }
                    MainActivity.start(this, token)
                }, { e ->
                    if (e is UserRecoverableAuthException) {
                        startActivityForResult(e.intent, REQUEST_AUTHORIZATION)
                    }
                }))
    }

    /**
     * Attempts to set the account used with the API credentials. If an account
     * name was previously saved it will use that one otherwise an account
     * picker dialog will be shown to the user. Note that the setting the
     * account to use with the credentials object requires the app to have the
     * GET_ACCOUNTS permission, which is requested here if it is not already
     * present. The AfterPermissionGranted annotation indicates that this
     * function will be rerun automatically whenever the GET_ACCOUNTS permission
     * is granted.
     */
    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
    private fun chooseAccount() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.GET_ACCOUNTS)) {
            val accountName = appPreferences.accountName
            if (accountName != null) {
                credential!!.selectedAccountName = accountName
                checkAuthAndLoadData()
            } else {
                startActivityForResult(credential!!.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER)
            }
        } else {
            EasyPermissions.requestPermissions(
                    this,
                    getString(R.string.access_to_google_account_needed),
                    REQUEST_PERMISSION_GET_ACCOUNTS,
                    Manifest.permission.GET_ACCOUNTS
            )
        }
    }

    /**
     * Attempt to resolve a missing, out-of-date, invalid or disabled Google
     * Play Services installation via a user dialog, if possible.
     */
    private fun acquireGooglePlayServices() {
        val apiAvailability = GoogleApiAvailability.getInstance()
        val connectionStatusCode = apiAvailability.isGooglePlayServicesAvailable(this)
        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode)
        }
    }

    /**
     * Display an error dialog showing that Google Play Services is missing
     * or out of date.
     * @param connectionStatusCode code describing the presence (or lack of)
     * Google Play Services on this device.
     */
    private fun showGooglePlayServicesAvailabilityErrorDialog(
            connectionStatusCode: Int
    ) = GoogleApiAvailability.getInstance()
            .getErrorDialog(this@StartActivity, connectionStatusCode, REQUEST_GOOGLE_PLAY_SERVICES)
            .apply { show() }

    companion object {
        private const val REQUEST_ACCOUNT_PICKER = 1000
        private const val REQUEST_AUTHORIZATION = 1001
        private const val REQUEST_GOOGLE_PLAY_SERVICES = 1002
        private const val REQUEST_PERMISSION_GET_ACCOUNTS = 1003
        const val REQUEST_MAIN_ACTIVITY = 1004

        private val SCOPES = YouTubeScopes.all().toList()
    }
}
