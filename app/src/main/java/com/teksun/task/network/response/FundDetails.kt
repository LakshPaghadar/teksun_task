package com.teksun.task.network.response

data class FundDetails(
    val `data`: List<Data>,
    val meta: Meta,
    val status: String
)