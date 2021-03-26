package com.github.forrestdp.healbeapp.ui.settings

import android.graphics.drawable.Drawable
import androidx.lifecycle.ViewModel
import com.github.forrestdp.healbeapp.util.FragmentToolbarable

class SettingsViewModel : ViewModel(), FragmentToolbarable {
    override val title: String = "Настройки"
    override lateinit var titleImage: Drawable
}