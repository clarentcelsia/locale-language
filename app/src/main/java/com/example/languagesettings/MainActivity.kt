package com.example.languagesettings

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.text.Layout.JUSTIFICATION_MODE_INTER_WORD
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.core.app.ActivityCompat.recreate
import com.example.languagesettings.SettingActivity.Companion.isChanged
import com.example.languagesettings.SettingActivity.Companion.langString
import kotlinx.android.synthetic.main.activity_main.*

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity(), PopupMenu.OnMenuItemClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        supportActionBar?.title = Html.fromHtml("<font color ='#FFFFFF'>Settings</font")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            textview.justificationMode = JUSTIFICATION_MODE_INTER_WORD
        }
    }

    override fun onResume() {
        super.onResume()
        if (isChanged) {
            val setHelper = SettingHelper()
            setHelper.setLocale(this, langString)
//            recreate()

            isChanged = false
        }
    }

    fun btnSetting(view: View) {
        PopupMenu(this, view).apply {
            setOnMenuItemClickListener(this@MainActivity)
            inflate(R.menu.settings)
            show()
        }
    }

    override fun onMenuItemClick(p0: MenuItem): Boolean {
        return when (p0.itemId) {
            R.id.setting -> {
                startActivity(Intent(this, SettingActivity::class.java)
                    .setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP))
                true
            }
            else -> false
        }
    }

}