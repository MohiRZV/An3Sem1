package com.mohi.maparkingapp.domain

/**
 *   created by Mohi on 1/24/2022
 */
data class Lot (
    var id: Long = 0,
    var number: String = "",
    var address: String = "",
    var status: String = "",
    var count: Int = 0
)