package com.github.forrestdp.healbeapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.github.forrestdp.healbeapp.ui.history.HistoryFragment
import com.github.forrestdp.healbeapp.ui.progress.ProgressFragment
import com.github.forrestdp.healbeapp.ui.timer.TimerFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.lang.IllegalStateException

class WorkoutInProgressActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workout_in_progress)

        val navView: BottomNavigationView = findViewById(R.id.top_nav_view)
        val navController = findNavController(R.id.nav_host_fragment_workout)
        navView.setupWithNavController(navController)
    }
}