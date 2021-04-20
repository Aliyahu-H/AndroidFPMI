package com.example.petexchange.model.sources.exchangerate.exchangerate_api.retrofit

import com.squareup.moshi.Json

// От этого класса можно наследовать производные,
// но тогда конструкция становится слишком громоздкой,
// и зарещается использование data
open class ExchangeRateAnswer(
    val result: String,
    val documentation: String="",
    @field:Json(name = "terms_of_use")
    val termsOfUse: String = "",
    @field:Json(name = "time_last_update_unix")
    val timeLastUpdateUnix: Long = -1,
    @field:Json(name = "time_last_update_utc")
    val timeLastUpdateUtc: String = "",
    @field:Json(name = "time_next_update_unix")
    val timeNextUpdateUnix: Long = -1,
    @field:Json(name = "time_next_update_utc")
    val timeNextUpdateUtc: String = "",
    @field:Json(name = "base_code")
    val baseCode: String = "",
    @field:Json(name = "error-type")
    val errorType: String = ""
) {
}