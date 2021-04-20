package com.example.petexchange.model.sources.exchangerate.exchangerate_api.retrofit

import com.squareup.moshi.Json

class PairExchangeRateAnswer(
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
    @field:Json(name = "target_code")
    val targetCode: String = "",
    @field:Json(name = "conversion_rate")
    val conversionRate: Double = 0.0,
    @field:Json(name = "conversion_result")
    val conversionResult: Double = 0.0,
    @field:Json(name = "error-type")
    val errorType: String = ""
) {
}