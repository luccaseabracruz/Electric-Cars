package com.example.eletriccars.domain

import com.google.gson.annotations.SerializedName

data class Car (
    val id: Int,

    @SerializedName("preco")
    val price: String,

    @SerializedName("bateria")
    val battery: String,

    @SerializedName("potencia")
    val horsepower: String,

    @SerializedName("recarga")
    val recharge: String,

    val urlPhoto: String,

    var isFavorite: Boolean
)