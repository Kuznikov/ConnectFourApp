package com.example.connectfour

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.connectfour.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var mNavController: NavController
    private var _binding: ActivityMainBinding? = null
    val mBinding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val w:Window = window
        w.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        mNavController = Navigation.findNavController(this, R.id.nav_host_fragment)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}