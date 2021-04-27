package com.github.forrestdp.healbeapp

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.github.forrestdp.healbeapp.util.create
import com.healbe.healbesdk.business_api.HealbeSdk
import com.healbe.healbesdk.business_api.user.data.DistanceUnits
import com.healbe.healbesdk.business_api.user.data.HealbeSessionState
import com.healbe.healbesdk.business_api.user_storage.entity.HealbeDevice
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.rx2.await
import kotlinx.coroutines.withContext
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

    private val email: String = ""
    private val password: String = ""
    private val device: HealbeDevice = HealbeDevice.create(
        name = "",
        mac = "",
        pin = "",
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        lifecycleScope.launchWhenCreated {
            HealbeSdk.init(application.applicationContext).await()
            HealbeSdk.get().USER.prepareSession().await()
            with(HealbeSdk.get()) {
                withContext(Dispatchers.IO) {
                    USER.prepareSession().await()
                    USER.login(email, password).await()
                    GOBE.set(device).await()
                    GOBE.connect().await()
                }
            }
        }
    }
}
