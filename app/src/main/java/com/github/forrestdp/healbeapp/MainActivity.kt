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
import com.healbe.healbesdk.business_api.user.data.HealbeSessionState
import com.healbe.healbesdk.business_api.user_storage.entity.HealbeDevice
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.rx2.await
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private val email: String = "egorponomarev93@gmail.com"
    private val password: String = "dYnfoc-quxmiq-4fewka"
    private val device: HealbeDevice = HealbeDevice.create(
        name = "Healbe 78:9B:AF",
        mac = "88:6B:0F:78:9B:AF",
        pin = "091297",
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
                    println("EGOR: prepared session")
                    val sessionState = USER.login(email, password).await()
                    println("EGOR: logged in")
                    if (!HealbeSessionState.isUserValid(sessionState)) {
//                    throw RuntimeException("user invalid")
                        println("User is invalid")
                    }
                    GOBE.set(device).await()
                    println("EGOR: set device")
                    GOBE.connect().await()
                    println("EGOR: connected")
                }
            }
        }
    }
}