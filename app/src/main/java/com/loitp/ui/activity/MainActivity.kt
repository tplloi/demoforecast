package com.loitp.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.annotation.LogTag
import com.core.base.BaseFontActivity
import com.core.common.Constants
import com.core.helper.adhelper.AdHelperActivity
import com.core.helper.donate.FrmDonate
import com.core.utilities.*
import com.google.android.material.navigation.NavigationView
import com.loitp.R
import com.loitp.ui.fragment.HomeFragment
import com.loitp.ui.fragment.SettingFragment
import com.skydoves.transformationlayout.onTransformationStartContainer
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.view_drawer_end.*
import kotlinx.android.synthetic.main.view_drawer_main.*

@LogTag("MainActivity")
class MainActivity : BaseFontActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun setLayoutResourceId(): Int {
        return R.layout.activity_main
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        onTransformationStartContainer()
        super.onCreate(savedInstanceState)

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
    }

    public override fun onResume() {
        adView.resume()
        super.onResume()
    }

    public override fun onDestroy() {
        adView.destroy()
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

    private var currentItemId = R.id.navHome
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
            R.id.navDonation -> {
                currentItemId = R.id.navDonation
                LScreenUtil.replaceFragment(
                        activity = this,
                        containerFrameLayoutIdRes = R.id.flContainer,
                        fragment = FrmDonate(),
                        isAddToBackStack = false
                )
            }
        }

        drawerLayout.closeDrawer(GravityCompat.START)
        navViewStart.postDelayed({
            if (currentItemId == R.id.navHome) {
                navViewStart.menu.findItem(R.id.navHome).isChecked = true
            } else if (currentItemId == R.id.navDonation) {
                navViewStart.menu.findItem(R.id.navDonation).isChecked = true
            }
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
}
