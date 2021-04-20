package com.example.petexchange.ui.currency

import androidx.annotation.DrawableRes
import android.os.Parcel;
import android.os.Parcelable;

data class Currency(@DrawableRes var flag: Int, var name: String?, var _value: Double) : Parcelable {

    //@DrawableRes var flag: Int = 0

    //var name: String? = null

    //var _value: Double = .0

    val value: String
        get() = _value.toString()

    override fun describeContents(): Int {
        return 0
    }

    // упаковываем объект в Parcel
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(flag)
        parcel.writeString(name)
        parcel.writeDouble(_value)
    }

    // конструктор, считывающий данные из Parcel
    private constructor(parcel: Parcel) : this(parcel.readInt(), parcel.readString(), parcel.readDouble()) {
    }

    companion object CREATOR : Parcelable.Creator<Currency> {
        override fun createFromParcel(parcel: Parcel): Currency {
            return Currency(parcel)
        }

        override fun newArray(size: Int): Array<Currency?> {
            return arrayOfNulls(size)
        }
    }
}