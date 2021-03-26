package com.github.forrestdp.healbeapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.forrestdp.healbeapp.util.create
import com.healbe.healbesdk.business_api.HealbeSdk
import com.healbe.healbesdk.business_api.user.data.HealbeSessionState
import com.healbe.healbesdk.business_api.user_storage.entity.HealbeDevice
import com.healbe.healbesdk.device_api.ClientState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.rx2.asFlow
import kotlinx.coroutines.rx2.await

class HomeViewModel : ViewModel() {

    private val email: String = "egorponomarev93@gmail.com"
    private val password: String = "dYnfoc-quxmiq-4fewka"
    private val device: HealbeDevice = HealbeDevice.create(
        name = "Healbe 78:9B:AF",
        mac = "88:6B:0F:78:9B:AF",
        pin = "091297",
    )

    private val _pulse = MutableLiveData("Pulse: unknown")
    val pulse: LiveData<String> = _pulse

    private val _isPulseVisible = MutableLiveData(false)
    val isPulseVisible: LiveData<Boolean> = _isPulseVisible

    private val _status = MutableLiveData("DISCONNECTED")
    val status: LiveData<String> = _status

    private val _isStatusVisible = MutableLiveData(true)
    val isStatusVisible: LiveData<Boolean> = _isStatusVisible

    private val _isProgressVisible = MutableLiveData(true)
    val isProgressVisible: LiveData<Boolean> = _isProgressVisible

    @ExperimentalCoroutinesApi
    suspend fun runChain() {
        with(HealbeSdk.get()) {
            try {
                USER.prepareSession().await()
                val sessionState = USER.login(email, password).await()
                if (!HealbeSessionState.isUserValid(sessionState)) {
//                    throw RuntimeException("user invalid")
                    println("User fukuser")
                }
                GOBE.set(device).await()
                GOBE.connect().await()
                GOBE.observeConnectionState().asFlow()
                    .onEach { _status.value = it.toString() }
                    .takeWhile { it != ClientState.READY }
                    .collect()
                TASKS.observeHeartRate().asFlow()
                    .collect { heartRate ->
                        _isProgressVisible.value = false
                        if (!heartRate.isEmpty) {
                            _isStatusVisible.value = false
                            _isPulseVisible.value = true
                            _pulse.value = "Pulse: ${heartRate.heartRate} bpm"
                        } else {
                            _isStatusVisible.value = true
                            _isPulseVisible.value = false
                            _status.value = "Pulse is not found :("
                        }
                    }
            } catch (e: Throwable) {
                _isProgressVisible.value = false
                _isPulseVisible.value = false
                _status.value = "Error: ${e.message}"
            }
        }
    }
}