package com.example.firebasestudyproject.ui.dashboard

import android.os.Bundle
import androidx.core.content.ContextCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.firebasestudyproject.R
import com.example.firebasestudyproject.base.BaseActivity
import com.example.firebasestudyproject.databinding.ActivityDashBoardBinding

class DashBoardActivity : BaseActivity() {

    private lateinit var binding: ActivityDashBoardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDashBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.setBackgroundDrawable(
            ContextCompat.getDrawable(
                this@DashBoardActivity,
                R.drawable.toolbar_bg
            )
        )

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_dash_board)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_dashboard, R.id.navigation_product, R.id.navigation_orders
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onBackPressed() {
        doubleTapToExit()
    }
}