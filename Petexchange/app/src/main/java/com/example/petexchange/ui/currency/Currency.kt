package com.example.petexchange.ui.currency

import androidx.annotation.DrawableRes

class Currency(@DrawableRes flag: Int, name: String?, value: Double) {

    @DrawableRes var flag: Int = 0

    var name: String? = null

    var _value: Double = .0

    val value: String
        get() = _value.toString()

    init {
        this.flag = flag
        this.name = name
        this._value = value
    }
}