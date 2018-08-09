package com.example.there.flextube.start

import android.Manifest
import android.accounts.AccountManager
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.net.ConnectivityManager
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.example.there.flextube.R
import com.example.there.flextube.databinding.ActivityStartBinding
import com.example.there.flextube.lifecycle.ConnectivityComponent
import com.example.there.flextube.lifecycle.DisposablesComponent
import com.example.there.flextube.main.MainActivity
import com.google.android.gms.auth.UserRecoverableAuthException
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.util.ExponentialBackOff
import com.google.api.services.youtube.YouTubeScopes
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_start.*
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

class StartActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {

    private var credential: GoogleAccountCredential? = null

    private val disposablesComponent = DisposablesComponent()
    private val connectivityComponent: ConnectivityComponent by lazy(LazyThreadSafetyMode.NONE) {
        ConnectivityComponent(
                this,
                false,
                {
                    if (credential == null)
                        credential = GoogleAccountCredential.usingOAuth2(applicationContext, SCOPES).setBackOff(ExponentialBackOff())
                    checkAuthAndLoadData()
                },
                start_activity_root_layout
        )
    }

    private val startViewState = StartViewState()

    private val startView: StartView by lazy(LazyThreadSafetyMode.NONE) {
        StartView(startViewState, View.OnClickListener { chooseAccount() })
    }

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

    private fun checkAuthAndLoadData() {
        if (!isGooglePlayServicesAvailable()) {
            acquireGooglePlayServices()
        } else if (credential!!.selectedAccountName == null) {
            chooseAccount()
        } else if (!isDeviceOnline()) {
            Toast.makeText(this, "No network connection available.", Toast.LENGTH_SHORT).show()
        } else {
            loadAccessTokenAndLoadData()
        }
    }

    private fun loadAccessTokenAndLoadData() {
        startViewState.authInProgress.set(true)
        disposablesComponent.add(Single.fromCallable { credential!!.token }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError { startViewState.authInProgress.set(false) }
                .subscribe({ token ->
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
            val accountName = PreferenceManager.getDefaultSharedPreferences(this).getString(PREF_ACCOUNT_NAME, null)
            if (accountName != null) {
                credential!!.selectedAccountName = accountName
                checkAuthAndLoadData()
            } else {
                // Start a dialog from which the user can choose an account
                startActivityForResult(credential!!.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER)
            }
        } else {
            // Request the GET_ACCOUNTS permission via a user dialog
            EasyPermissions.requestPermissions(
                    this,
                    "This app needs to access your Google account (via Contacts).",
                    REQUEST_PERMISSION_GET_ACCOUNTS,
                    Manifest.permission.GET_ACCOUNTS)
        }
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
                Toast.makeText(this,
                        "This app requires Google Play Services. Please install Google Play Services on your device and relaunch this app.",
                        Toast.LENGTH_SHORT).show()
            } else {
                checkAuthAndLoadData()
            }
            REQUEST_ACCOUNT_PICKER -> if (resultCode == Activity.RESULT_OK && data != null && data.extras != null) {
                val accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME)
                if (accountName != null) {
                    val preferences = PreferenceManager.getDefaultSharedPreferences(this)
                    val editor = preferences.edit()
                    editor.putString(PREF_ACCOUNT_NAME, accountName)
                    editor.apply()
                    credential!!.selectedAccountName = accountName
                    checkAuthAndLoadData()
                }
            }
            REQUEST_AUTHORIZATION -> if (resultCode == Activity.RESULT_OK) {
                checkAuthAndLoadData()
            }
            REQUEST_MAIN_ACTIVITY -> {
                val preferences = PreferenceManager.getDefaultSharedPreferences(this)
                val editor = preferences.edit()
                editor.remove(PREF_ACCOUNT_NAME)
                editor.apply()
                credential!!.selectedAccountName = null
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
            Toast.LENGTH_SHORT)
            .show()

    /**
     * Checks whether the device currently has a network connection.
     * @return true if the device has a network connection, false otherwise.
     */
    private fun isDeviceOnline(): Boolean {
        val connMgr = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connMgr.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    /**
     * Check that Google Play services APK is installed and up to date.
     * @return true if Google Play Services is available and up to
     * date on this device false otherwise.
     */
    private fun isGooglePlayServicesAvailable(): Boolean =
            GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this) == ConnectionResult.SUCCESS

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
    private fun showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode: Int) = GoogleApiAvailability
            .getInstance()
            .getErrorDialog(this@StartActivity, connectionStatusCode, REQUEST_GOOGLE_PLAY_SERVICES)
            .apply { show() }

    companion object {
        private const val REQUEST_ACCOUNT_PICKER = 1000
        private const val REQUEST_AUTHORIZATION = 1001
        private const val REQUEST_GOOGLE_PLAY_SERVICES = 1002
        private const val REQUEST_PERMISSION_GET_ACCOUNTS = 1003
        const val REQUEST_MAIN_ACTIVITY = 1004

        const val PREF_ACCOUNT_NAME = "accountName"
        private val SCOPES = listOf(YouTubeScopes.YOUTUBE_FORCE_SSL, YouTubeScopes.YOUTUBEPARTNER)
    }
}
