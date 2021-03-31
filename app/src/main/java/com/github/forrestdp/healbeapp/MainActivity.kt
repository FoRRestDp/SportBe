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

    private val email: String = "example@mail.com"
    private val password: String = "your_password"
    private val device: HealbeDevice = HealbeDevice.create(
        name = "Device name",
        mac = "Device mac adress",
        pin = "Device pin",
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

                    val stepCount = HealbeSdk.get().HEALTH_DATA.getEnergySummary(0).firstElement().await()?.get()?.steps
                    println("EGOR: $stepCount")
//
//                    val stepLengthCm = HealbeSdk.get().USER.user.await().bodyMeasurements.stepLengthCM
//                    val spentKCalories = HealbeSdk.get().HEALTH_DATA.getEnergyData(1616940745621).asFlow()
//                        .map { list ->
//                            list.map { energyData -> energyData.energyOut }.sum()
//                        }
//                        .onEach { println("DAVE kcal: $it") }
//                        .reduce { a, b -> a + b } // Суммируем значения
                }
            }
        }
    }
}
