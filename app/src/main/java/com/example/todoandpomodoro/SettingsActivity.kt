package com.example.todoandpomodoro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settingsContainer,SettingsFragment())
            .commit()
        setContentView(R.layout.activity_settings)

        backButton.setOnClickListener {
            finish()
        }
    }


}