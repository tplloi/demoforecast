package com.loitp.ui.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import com.annotation.LogTag
import com.core.base.BaseFontActivity
import com.core.common.Constants
import com.core.helper.adhelper.AdHelperActivity
import com.core.utilities.*
import com.data.EventBusData
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.material.navigation.NavigationView
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.loitp.BuildConfig
import com.loitp.R
import com.loitp.ui.fragment.HomeFragment
import com.loitp.ui.fragment.SettingFragment
import com.loitp.util.LLocationUtil
import com.skydoves.transformationlayout.onTransformationStartContainer
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.view_drawer_end.*
import kotlinx.android.synthetic.main.view_drawer_main.*

@LogTag("loitppMainActivity")
class MainActivity : BaseFontActivity(), NavigationView.OnNavigationItemSelectedListener {

    companion object {
        // location updates interval - 10min
        private const val UPDATE_INTERVAL_IN_MILLISECONDS: Long = 10 * 60 * 1000

        // fastest updates interval - 5 min
        // location updates will be received if another app is requesting the locations
        // than your app can handle
        private const val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS: Long = 5 * 60 * 1000
        private const val REQUEST_CHECK_SETTINGS = 100
    }

    private var mFusedLocationClient: FusedLocationProviderClient? = null// bunch of location related apis
    private var mSettingsClient: SettingsClient? = null
    private var mLocationRequest: LocationRequest? = null
    private var mLocationSettingsRequest: LocationSettingsRequest? = null
    private var mLocationCallback: LocationCallback? = null
    private var mCurrentLocation: Location? = null
    private var currentItemId = R.id.navHome

    override fun setLayoutResourceId(): Int {
        return R.layout.activity_main
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        onTransformationStartContainer()
        super.onCreate(savedInstanceState)

        initLocation()
        setupViews()
    }

    private fun setupViews() {
        LUIUtil.createAdBanner(adView)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navViewStart.setNavigationItemSelectedListener(this)
        drawerLayout.useCustomBehavior(Gravity.START)
        drawerLayout.useCustomBehavior(Gravity.END)

        //cover
//        LImageUtil.load(context = this, any = R.drawable.vn_express, imageView = navViewStart.getHeaderView(0).ivCover)

        tvAd.text = LStoreUtil.readTxtFromRawFolder(nameOfRawFile = R.raw.ad)

        switchHomeScreen()
    }

    private fun switchHomeScreen() {
        navViewStart.menu.performIdentifierAction(R.id.navHome, 0)
        navViewStart.menu.findItem(R.id.navHome).isChecked = true
    }

    public override fun onPause() {
        adView.pause()
        super.onPause()
        stopLocationUpdates()
    }

    public override fun onResume() {
        adView.resume()
        super.onResume()

        // Resuming location updates depending on button state and
        // allowed permissions
        if (checkPermissions()) {
            startLocationWithPermisson()
        }
        updateLocationUI()
    }

    public override fun onDestroy() {
        adView.destroy()
        stopLocationUpdates()
        super.onDestroy()
    }

    private var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed()
                return
            }
            this.doubleBackToExitPressedOnce = true
            showShortInformation(msg = getString(R.string.press_again_to_exit), isTopAnchor = false)
            Handler(Looper.getMainLooper()).postDelayed({
                doubleBackToExitPressedOnce = false
            }, 2000)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navHome -> {
                logD("onNavigationItemSelected navHome")
                currentItemId = R.id.navHome
                LScreenUtil.replaceFragment(
                        this,
                        R.id.flContainer,
                        HomeFragment(),
                        false
                )
            }
            R.id.navSetting -> {
                logD("onNavigationItemSelected navHome")
                currentItemId = R.id.navSetting
                LScreenUtil.replaceFragment(this, R.id.flContainer, SettingFragment(), false)
            }
            R.id.navGithub -> {
                LSocialUtil.openUrlInBrowser(context = this, url = "https://github.com/tplloi/vnexpress")
            }
            R.id.navRateApp -> {
                LSocialUtil.rateApp(activity = this, packageName = packageName)
            }
            R.id.navMoreApp -> {
                LSocialUtil.moreApp(this)
            }
            R.id.navFacebookFanPage -> {
                LSocialUtil.likeFacebookFanpage(this)
            }
            R.id.navChatWithDev -> {
                LSocialUtil.chatMessenger(this)
            }
            R.id.navShareApp -> {
                LSocialUtil.shareApp(this)
            }
            R.id.navPolicy -> {
                LSocialUtil.openBrowserPolicy(context = this)
            }
            R.id.navAd -> {
                val intent = Intent(this, AdHelperActivity::class.java)
                intent.putExtra(Constants.AD_HELPER_IS_ENGLISH_LANGUAGE, false)
                startActivity(intent)
                LActivityUtil.tranIn(this)
            }
//            R.id.navDonation -> {
//                currentItemId = R.id.navDonation
//                LScreenUtil.replaceFragment(
//                        activity = this,
//                        containerFrameLayoutIdRes = R.id.flContainer,
//                        fragment = FrmDonate(),
//                        isAddToBackStack = false
//                )
//            }
        }

        drawerLayout.closeDrawer(GravityCompat.START)
        navViewStart.postDelayed({
            if (currentItemId == R.id.navHome) {
                navViewStart.menu.findItem(R.id.navHome).isChecked = true
            }
//            else if (currentItemId == R.id.navDonation) {
//                navViewStart.menu.findItem(R.id.navDonation).isChecked = true
//            }
        }, 500)
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_drawer_end, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.actionRightDrawer -> {
                drawerLayout.openDrawer(GravityCompat.END)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNetworkChange(event: EventBusData.ConnectEvent) {
        super.onNetworkChange(event)

        //TODO
        showSnackBarInfor("TODO onNetworkChange")
    }

    private fun initLocation() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mSettingsClient = LocationServices.getSettingsClient(this)
        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)

                // location is received
                mCurrentLocation = locationResult.lastLocation
                updateLocationUI()
            }
        }
        mLocationRequest = LocationRequest()
        mLocationRequest?.let {
            it.interval = UPDATE_INTERVAL_IN_MILLISECONDS
            it.fastestInterval = FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS
            it.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

            val builder = LocationSettingsRequest.Builder()
            builder.addLocationRequest(it)
            mLocationSettingsRequest = builder.build()
        }
    }

    private fun updateLocationUI() {
        mCurrentLocation?.let {

            var moreInformation = ""
            LLocationUtil.getCityByLatLon(
                    latitude = it.latitude,
                    longitude = it.longitude)
            { address: String, city: String, state: String, country: String ->
                moreInformation += "$address - $city - $state - $country"
            }

            logD("updateLocationUI Lat: " + it.latitude + ", " + "Lng: " + it.longitude + ", more moreInformation: $moreInformation")
        }
    }

    /**
     * Starting location updates
     * Check whether location settings are satisfied and then
     * location updates will be requested
     */
    private fun startLocationUpdates() {
        mSettingsClient?.let { settingsClient ->
            settingsClient.checkLocationSettings(mLocationSettingsRequest)
                    .addOnSuccessListener(this) {
                        logD("All location settings are satisfied.")
                        logD("Started location updates!")
                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            mFusedLocationClient?.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper())
                            updateLocationUI()
                        } else {
                            showShortInformation("Dont have permission ACCESS_FINE_LOCATION && ACCESS_COARSE_LOCATION")
                        }
                    }
                    .addOnFailureListener(this) { e ->
                        when ((e as ApiException).statusCode) {
                            LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                                logD("Location settings are not satisfied. Attempting to upgrade location settings ")
                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    val rae = e as ResolvableApiException
                                    rae.startResolutionForResult(this, REQUEST_CHECK_SETTINGS)
                                } catch (sie: IntentSender.SendIntentException) {
                                    sie.printStackTrace()
                                }
                            }
                            LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                                val errorMessage = "Location settings are inadequate, and cannot be fixed here. Fix in Settings."
                                logD(errorMessage)
                                showShortInformation(errorMessage)
                            }
                        }
                        updateLocationUI()
                    }
        }
    }

    private fun startLocationWithPermisson() {
        // Requesting ACCESS_FINE_LOCATION using Dexter library
        Dexter.withContext(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(response: PermissionGrantedResponse) {
                        startLocationUpdates()
                    }

                    override fun onPermissionDenied(response: PermissionDeniedResponse) {
                        if (response.isPermanentlyDenied) {
                            // open device settings when the permission is
                            // denied permanently
                            openSettings()
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest, token: PermissionToken) {
                        token.continuePermissionRequest()
                    }
                }).check()
    }

    private fun stopLocationUpdates() {
        // Removing location updates
        mFusedLocationClient?.removeLocationUpdates(mLocationCallback)?.addOnCompleteListener(this) {
            logD("Location updates stopped!")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQUEST_CHECK_SETTINGS -> when (resultCode) {
                Activity.RESULT_OK -> {
                    logE("User agreed to make required location settings changes.")
                }
                Activity.RESULT_CANCELED -> {
                    logE("User chose not to make required location settings changes.")
                }
            }
        }
    }

    private fun openSettings() {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val uri = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
        intent.data = uri
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    private fun checkPermissions(): Boolean {
        val permissionState = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        return permissionState == PackageManager.PERMISSION_GRANTED
    }

}
