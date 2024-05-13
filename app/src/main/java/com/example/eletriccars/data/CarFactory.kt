package com.example.eletriccars.data

import com.example.eletriccars.domain.Car

object CarFactory {

    val carList: List<Car> = listOf(
        Car(
            id = 1,
            price = "R$ 300.000,00",
            battery = "300 kWh",
            horsepower = "200cv",
            recharge = "30 min",
            urlPhoto = "www.google.com.br",
            isFavorite = false
        ),
        Car(
            id = 2,
            price = "R$ 150.000,00",
            battery = "180 kWh",
            horsepower = "100cv",
            recharge = "50 min",
            urlPhoto = "www.google.com.br",
            isFavorite = false
        ),
        Car(
            id = 3,
            price = "R$ 1500.000,00",
            battery = "256 kWh",
            horsepower = "350cv",
            recharge = "160 min",
            urlPhoto = "www.google.com.br",
            isFavorite = false
        )
    )
}