package com.example.petexchange.ui.currency

import androidx.annotation.DrawableRes
import android.os.Parcel;
import android.os.Parcelable;
import com.example.petexchange.R
import com.example.petexchange.model.sources.database.CurrencyRate

data class Currency(@DrawableRes var flag: Int, var nameTo: String?, var nameFrom:String?, var _value: Double) : Parcelable {

    val value: String
        get() = "$_value $nameTo"

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(flag)
        parcel.writeString(nameTo)
        parcel.writeString(nameFrom)
        parcel.writeDouble(_value)
    }

    constructor(currencyRate: CurrencyRate) : this(
        R.drawable.ic_coin,
        currencyRate.toCurrency,
        currencyRate.fromCurrency,
        currencyRate.exchangeRate) {
    }

    private constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readDouble()) {
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