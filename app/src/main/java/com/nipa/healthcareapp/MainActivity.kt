package com.nipa.healthcareapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nipa.healthcaremobile.R  // Add this import!

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // The NavHostFragment in activity_main.xml will handle all navigation
    }
}