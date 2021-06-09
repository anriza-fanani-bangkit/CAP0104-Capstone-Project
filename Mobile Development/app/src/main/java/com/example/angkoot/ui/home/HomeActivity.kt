package com.example.angkoot.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.angkoot.R
import com.example.angkoot.databinding.ActivityHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val homeNavHostFragment =
            supportFragmentManager.findFragmentById(R.id.home_nav_host_fragment) as NavHostFragment
        navController = homeNavHostFragment.findNavController()

        setupHomeBottomNavigationView()
    }

    private fun setupHomeBottomNavigationView() {
        with(binding) {
            val appBarConfig = AppBarConfiguration(
                setOf(
                    R.id.orderingFragment,
                    R.id.profileFragment
                )
            )

            setupActionBarWithNavController(navController, appBarConfig)
            homeBottomNavigation.setupWithNavController(navController)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }

    companion object {
        const val PARAMS_USER_ID = "PARAMS_USER_ID"
    }
}