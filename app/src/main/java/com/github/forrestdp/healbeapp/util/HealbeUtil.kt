package com.github.forrestdp.healbeapp.util

import com.healbe.healbesdk.business_api.user_storage.entity.HealbeDevice

internal fun HealbeDevice.Companion.create(
    name: String,
    mac: String,
    pin: String,
    isActive: Boolean = true,
): HealbeDevice = EMPTY.copy(
    name = name,
    mac = mac,
    pin = pin,
    isActive = isActive,
)