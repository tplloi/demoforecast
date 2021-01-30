package com.loitp.ui.fragment

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.annotation.LogTag
import com.core.base.BaseFragment
import com.core.utilities.LActivityUtil
import com.core.utilities.LDialogUtil
import com.core.utilities.LSharedPrefsUtil
import com.core.utilities.LUIUtil
import com.loitp.R
import com.loitp.constant.Cons
import com.loitp.ui.activity.MainActivity
import kotlinx.android.synthetic.main.frm_setting.*

@LogTag("SettingFragment")
class SettingFragment : BaseFragment() {

    private var dialog: AlertDialog? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
    }

    override fun onDestroyView() {
        dialog?.dismiss()
        super.onDestroyView()
    }

    override fun setLayoutResourceId(): Int {
        return R.layout.frm_setting
    }

    private fun setupViews() {
        val isDarkTheme = LUIUtil.isDarkTheme()
        swEnableDarkMode.isChecked = isDarkTheme

        swEnableDarkMode.setOnCheckedChangeListener { _, isChecked ->
            handleSwitchDarkTheme(isChecked = isChecked)
        }

        val isSmallThumb = LSharedPrefsUtil.instance.getBoolean(Cons.IS_SMALL_THUMB, false)
        swSmallSizeItem.isChecked = isSmallThumb
        swSmallSizeItem.setOnCheckedChangeListener { _, isChecked ->
            handleSwitchSmallThumb(isChecked = isChecked)
        }

        val isGridView = LSharedPrefsUtil.instance.getBoolean(Cons.IS_GRID_VIEW, false)
        swIsGridView.isChecked = isGridView
        swIsGridView.setOnCheckedChangeListener { _, isChecked ->
            handleSwitchGridView(isChecked = isChecked)
        }
    }

    private fun handleSwitchDarkTheme(isChecked: Boolean) {
        context?.let { c ->
            val isDarkTheme = LUIUtil.isDarkTheme()
            if (isDarkTheme == isChecked) {
                return@let
            }

            dialog = LDialogUtil.showDialog2(
                    context = c,
                    title = getString(com.R.string.warning_vn),
                    msg = getString(com.R.string.app_will_be_restarted_vn),
                    button1 = getString(com.R.string.cancel),
                    button2 = getString(com.R.string.ok),
                    onClickButton1 = {
                        swEnableDarkMode?.isChecked = LUIUtil.isDarkTheme()
                    },
                    onClickButton2 = {
                        if (isChecked) {
                            LUIUtil.setDarkTheme(isDarkTheme = true)
                        } else {
                            LUIUtil.setDarkTheme(isDarkTheme = false)
                        }

                        val intent = Intent(context, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(intent)
                        context?.let {
                            LActivityUtil.transActivityNoAnimation(it)
                        }

                        dialog?.dismiss()
                    }
            )
            dialog?.setOnCancelListener {
                swEnableDarkMode?.isChecked = LUIUtil.isDarkTheme()
            }
        }
    }

    private fun handleSwitchSmallThumb(isChecked: Boolean) {
        LSharedPrefsUtil.instance.putBoolean(Cons.IS_SMALL_THUMB, isChecked)
        showShortInformation(getString(R.string.setting_success))
    }

    private fun handleSwitchGridView(isChecked: Boolean) {
        LSharedPrefsUtil.instance.putBoolean(Cons.IS_GRID_VIEW, isChecked)
        showShortInformation(getString(R.string.setting_success))
    }
}
