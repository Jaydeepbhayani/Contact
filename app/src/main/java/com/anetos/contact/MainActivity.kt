package com.anetos.contact

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.anetos.contact.core.BaseActivity

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}