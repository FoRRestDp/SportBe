package com.github.forrestdp.healbeapp.ui.support

import android.graphics.drawable.Drawable
import androidx.lifecycle.ViewModel
import com.github.forrestdp.healbeapp.util.FragmentToolbarable

class SupportViewModel : ViewModel(), FragmentToolbarable {
    override val title = "Поддержка"
    override lateinit var titleImage: Drawable

}