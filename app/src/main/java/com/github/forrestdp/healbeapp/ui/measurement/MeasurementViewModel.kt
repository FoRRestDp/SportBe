package com.github.forrestdp.healbeapp.ui.measurement

import android.graphics.drawable.Drawable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.forrestdp.healbeapp.util.FragmentToolbarable
import com.healbe.healbesdk.business_api.HealbeSdk
import kotlinx.coroutines.launch
import kotlinx.coroutines.rx2.await

class MeasurementViewModel : ViewModel(), FragmentToolbarable {
    override val title: String = "Параметры"
    override lateinit var titleImage: Drawable

    private val _name = MutableLiveData<String>()
    val name: LiveData<String> = _name

    private val _height = MutableLiveData<String>()
    val height: LiveData<String> = _height

    private val _weight = MutableLiveData<String>()
    val weight: LiveData<String> = _weight

    private val _sex = MutableLiveData<String>()
    val sex: LiveData<String> = _sex

    init {
        viewModelScope.launch {
            val sdk = HealbeSdk.get()
            val user = sdk.USER.user.await()
            _name.postValue(user.userName.firstName)
            _height.postValue(user.bodyMeasurements.heightCM.toString())
            _weight.postValue(user.bodyMeasurements.weightKG.toString())
            _sex.postValue(if (user.gender == 0) "Мужской" else "Женский")
        }
    }
}