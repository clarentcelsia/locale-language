package com.example.languagesettings

import android.app.ActivityOptions
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBar.DISPLAY_SHOW_CUSTOM
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.recreate
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.android.synthetic.main.settinglayout.*
import java.util.*

@Suppress("DEPRECATION")
class SettingActivity : AppCompatActivity() {

    lateinit var fadeout: Animation
    var count: Int = 0

    companion object {
        var selectedLanguage: String? = null
        var langString: String = "jp"
        var isChanged: Boolean = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        count = 0
        supportActionBar?.let {
            it.apply {
                displayOptions = DISPLAY_SHOW_CUSTOM
                setCustomView(R.layout.custom_title)
                setDisplayHomeAsUpEnabled(true)
            }
        }

        val languages = arrayOf<String>("Indonesian", "English", "Italian", "日本語")
        val dialog: AlertDialog.Builder = AlertDialog.Builder(this)
        dialog.apply {
            setTitle(getString(R.string.prefferedlang))
            setSingleChoiceItems(
                languages,
                -1
            ) { _, p1 ->
                selectedLanguage = languages[p1]
            }
            setPositiveButton("OK") { dialogInterface, i ->
                when (selectedLanguage) {
                    "Indonesian" -> langString = "in"
                    "English" -> langString = "en"
                    "Italian" -> langString = "it"
                    "日本語" -> langString = "jp"
                    else -> langString
                }

                val setHelper = SettingHelper()
                setHelper.setLocale(this@SettingActivity, langString)

                val fadein = AnimationUtils.loadAnimation(context, android.R.anim.fade_in)
                fadeout = AnimationUtils.loadAnimation(context, android.R.anim.fade_out)

                setLayout.apply {
                    visibility = VISIBLE
                    startAnimation(fadein)
                }

                Handler().postDelayed({
                    isChanged = true
                    setLayout.apply {
//                        recreate()
                        startActivity(
                            Intent("relaunch.activity.ACTIVITY_SELF_START_INTENT")
                                .putExtra("Restart", "this is relaunch data")
                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK),
                            ActivityOptions.makeSceneTransitionAnimation(this@SettingActivity)
                                .toBundle())

//                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK),
////                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP),
//                            ActivityOptions.makeSceneTransitionAnimation(this@SettingActivity)
//                                .toBundle()
//                        )
                    }
                    count = 1
                }, 2000L)
            }

            setNegativeButton("Cancel") { _, _ ->
            }
        }

        btnChoose.setOnClickListener {
            isChanged = false
            dialog.show()
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val relaunch = intent?.getStringExtra("Restart")

    }

    override fun onResume() {
        super.onResume()
        Log.d("TAG", "onResume: called")
        if (count == 1) {
            setLayout.apply {
                startAnimation(fadeout)
                visibility = INVISIBLE
            }
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        startActivity(
            Intent(this, MainActivity::class.java)
                .setAction("relaunch.activity.ACTIVITY_SELF_START_INTENT")
        )
        return true


    }
}