package com.magicianguo.menukey.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.Menu
import android.view.MenuItem
import android.widget.SeekBar
import androidx.appcompat.app.AlertDialog
import com.magicianguo.menukey.R
import com.magicianguo.menukey.constant.RequestCode
import com.magicianguo.menukey.constant.ViewOrientation
import com.magicianguo.menukey.databinding.ActivityMainBinding
import com.magicianguo.menukey.util.MenuKeyViewTools
import com.magicianguo.menukey.util.PermissionTools
import com.magicianguo.menukey.util.SPUtils
import com.magicianguo.menukey.util.ToastUtils
import com.magicianguo.menukey.view.KeyButton

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        PermissionTools.checkNotificationPermission(this)
        initView()
        registerReceiver()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver()
    }

    private fun initView() {
        setSupportActionBar(binding.toolBar)
        binding.swShowMenu.isChecked = MenuKeyViewTools.showMenuKey
        binding.swShowMenu.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                if (checkHasPermission()) {
                    MenuKeyViewTools.addView()
                } else {
                    binding.swShowMenu.isChecked = false
                }
            } else {
                MenuKeyViewTools.removeView()
            }
        }
        binding.btnSwitchIm.setOnClickListener {
            PermissionTools.requestSelectIM(this)
        }
        binding.rgOrientation.check(
            when (SPUtils.getOrientation()) {
                ViewOrientation.VERTICAL -> R.id.rb_vertical
                else -> R.id.rb_horizontal
            }
        )
        binding.rgOrientation.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rb_vertical -> {
                    MenuKeyViewTools.setOrientation(ViewOrientation.VERTICAL)
                    SPUtils.setOrientation(ViewOrientation.VERTICAL)
                }
                R.id.rb_horizontal -> {
                    MenuKeyViewTools.setOrientation(ViewOrientation.HORIZONTAL)
                    SPUtils.setOrientation(ViewOrientation.HORIZONTAL)
                }
            }
        }
        val buttonSize = SPUtils.getButtonSize()
        binding.tvButtonSize.text = getString(R.string.main_title4, buttonSize)
        binding.sbButtonSize.progress = buttonSize
        binding.sbButtonSize.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    val size = progress + 1
                    KeyButton.setButtonSize(size)
                    MenuKeyViewTools.refreshLayout()
                    binding.tvButtonSize.text = getString(R.string.main_title4, size)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                SPUtils.setButtonSize(KeyButton.buttonSizeDp)
            }
        })
        binding.keyOrder
    }

    private fun checkHasPermission(): Boolean {
        val alert = PermissionTools.hasAlertPermission(this)
        val accessibility = PermissionTools.isAccessibilityEnabled
        val imEnable = PermissionTools.isCurrentIMEnabled(this)
        val imSelected = PermissionTools.isCurrentIMSelected(this)
        return if (alert && accessibility && imEnable && imSelected) {
            true
        } else {
            AlertDialog.Builder(this)
                .setMessage(Html.fromHtml(
                    "应用需要以下授权：<br>" +
                            "1、悬浮窗权限${getPermissionTxt(alert)}<br>" +
                            "2、开启辅助功能${getPermissionTxt(accessibility)}<br>" +
                            "3、开启输入法服务${getPermissionTxt(imEnable)}<br>" +
                            "4、设为默认输入法${getPermissionTxt(imSelected)}"
                ))
                .setPositiveButton("去授权") { _, _ ->
                    when {
                        !alert -> PermissionTools.requestAlertPermission(this)
                        !accessibility -> PermissionTools.requestEnableAccessibility(this)
                        !imEnable -> PermissionTools.requestEnableCurrentIM(this)
                        else -> {
                            ToastUtils.short("请选择“${getString(R.string.app_name)}”")
                            binding.root.postDelayed({
                                PermissionTools.requestSelectIM(this)
                            }, 300)
                        }
                    }
                }
                .setNegativeButton("取消") { _,_-> }
                .create().show()
            false
        }
    }

    private fun getPermissionTxt(granted: Boolean): String {
        return "${if (granted) "<font color='#00DD00'>（已授权√）" else "<font color='#DD0000'>（未授权×）"}</font>"
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RequestCode.ALERT, RequestCode.ACCESSIBILITY, RequestCode.IM_ENABLE -> {
                checkHasPermission()
            }
        }
    }

    private val mReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == Intent.ACTION_INPUT_METHOD_CHANGED) {
                val selected = PermissionTools.isCurrentIMSelected(this@MainActivity)
                if (!selected) {
                    binding.swShowMenu.isChecked = false
                }
            }
        }
    }

    private fun registerReceiver() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(Intent.ACTION_INPUT_METHOD_CHANGED)
        registerReceiver(mReceiver, intentFilter)
    }

    private fun unregisterReceiver() {
        unregisterReceiver(mReceiver)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.about -> showAboutDialog()
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    private fun showAboutDialog() {
        AlertDialog.Builder(this)
            .setTitle("关于")
            .setMessage("菜单键\n" +
                        "Powered by MagicianGuo")
            .setPositiveButton("确认") { _, _ -> }
            .create().show()
    }
}