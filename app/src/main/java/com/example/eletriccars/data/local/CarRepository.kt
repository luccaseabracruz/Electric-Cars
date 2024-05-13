package com.example.eletriccars.data.local

import android.content.ContentValues
import android.content.Context
import android.provider.BaseColumns
import android.util.Log
import com.example.eletriccars.data.local.CarsContract.CarEntry.COLUMN_NAME_BATTERY
import com.example.eletriccars.data.local.CarsContract.CarEntry.COLUMN_NAME_CAR_ID
import com.example.eletriccars.data.local.CarsContract.CarEntry.COLUMN_NAME_HORSEPOWER
import com.example.eletriccars.data.local.CarsContract.CarEntry.COLUMN_NAME_PRICE
import com.example.eletriccars.data.local.CarsContract.CarEntry.COLUMN_NAME_RECHARGE
import com.example.eletriccars.data.local.CarsContract.CarEntry.COLUMN_NAME_URL_PHOTO
import com.example.eletriccars.data.local.CarsContract.CarEntry.TABLE_NAME
import com.example.eletriccars.domain.Car

class CarRepository(private val context: Context) {

    fun save(car: Car): Boolean {

        var isSaved = false

        try {
            val dbHelper = CarsDbHelper(context)
            val db = dbHelper.writableDatabase

            val values = ContentValues().apply{
                put(COLUMN_NAME_CAR_ID, car.id)
                put(COLUMN_NAME_PRICE, car.price)
                put(COLUMN_NAME_BATTERY, car.battery)
                put(COLUMN_NAME_HORSEPOWER, car.horsepower)
                put(COLUMN_NAME_RECHARGE, car.recharge)
                put(COLUMN_NAME_URL_PHOTO, car.urlPhoto)
            }

            val savedData =  db.insert(TABLE_NAME, null, values)

            if(savedData >= 0) {
                isSaved = true
            }
        } catch (ex: Exception){
            ex.message?.let{
                Log.e("Insert Error ->", it)
            }
        }

        return isSaved

    }

    fun delete(car: Car): Boolean {
        var isDeleted = false

        val dbCar = findCarById(car.id)

        try {
            val dbHelper = CarsDbHelper(context)
            val db = dbHelper.writableDatabase

            val condition = "${COLUMN_NAME_CAR_ID} = ?"
            val values = arrayOf(car.id.toString())

            db.delete(TABLE_NAME, condition, values)

            isDeleted = true

        } catch (ex: Exception) {
            Log.e("Error", "can't delete this car for some reason...")
        }

        return isDeleted
    }

    fun findCarById(id: Int): Car {
        val dbHelper = CarsDbHelper(context)
        val db = dbHelper.readableDatabase

        val columns = arrayOf(
            BaseColumns._ID,
            COLUMN_NAME_CAR_ID,
            COLUMN_NAME_PRICE,
            COLUMN_NAME_BATTERY,
            COLUMN_NAME_HORSEPOWER,
            COLUMN_NAME_RECHARGE,
            COLUMN_NAME_URL_PHOTO
        )

        val filter = "${COLUMN_NAME_CAR_ID} = ?"

        val filterValues = arrayOf(id.toString())

        val cursor = db.query(
            TABLE_NAME,
            columns,
            filter,
            filterValues,
            null,
            null,
            null
        )

        var itemId: Long = 0
        var itemPrice = ""
        var itemBattery = ""
        var itemHorsepower = ""
        var itemRecharge = ""
        var itemUrlPhoto = ""

        with(cursor) {
            while (moveToNext()) {
                itemId = getLong(getColumnIndexOrThrow(COLUMN_NAME_CAR_ID))
                //Log.d("id -> ", itemId.toString())

                itemPrice = getString(getColumnIndexOrThrow(COLUMN_NAME_PRICE))
                //Log.d("preço -> ", itemPrice)

                itemBattery = getString(getColumnIndexOrThrow(COLUMN_NAME_BATTERY))
                //Log.d("battery -> ", itemBattery)

                itemHorsepower = getString(getColumnIndexOrThrow(COLUMN_NAME_HORSEPOWER))
                //Log.d("horsepower -> ", itemHorsepower)

                itemRecharge = getString(getColumnIndexOrThrow(COLUMN_NAME_RECHARGE))
                //Log.d("recharge -> ", itemRecharge)

                itemUrlPhoto = getString(getColumnIndexOrThrow(COLUMN_NAME_URL_PHOTO))
                //Log.d("urlPhoto -> ", itemUrlPhoto)
            }
        }
        cursor.close()

        return Car(
            id = itemId.toInt(),
            price = itemPrice,
            battery = itemBattery,
            horsepower = itemHorsepower,
            recharge = itemRecharge,
            urlPhoto = itemUrlPhoto,
            isFavorite = true
        )
    }

    fun saveIfNotExists(car: Car) {
        val selectedCar = findCarById(car.id)

        if(selectedCar.id == ID_NO_CAR) {
            save(car)
        }
    }

    fun getAllCars(): List<Car> {
        val dbHelper = CarsDbHelper(context)
        val db = dbHelper.readableDatabase

        val columns = arrayOf(
            BaseColumns._ID,
            COLUMN_NAME_CAR_ID,
            COLUMN_NAME_PRICE,
            COLUMN_NAME_BATTERY,
            COLUMN_NAME_HORSEPOWER,
            COLUMN_NAME_RECHARGE,
            COLUMN_NAME_URL_PHOTO
        )

        val cursor = db.query(
            TABLE_NAME,
            columns,
            null,
            null,
            null,
            null,
            null
        )

        val responseCarsList = mutableListOf<Car>()


        with(cursor) {
            while (moveToNext()) {
                val itemId = getLong(getColumnIndexOrThrow(COLUMN_NAME_CAR_ID))
                Log.d("id -> ", itemId.toString())

                val itemPrice = getString(getColumnIndexOrThrow(COLUMN_NAME_PRICE))
                Log.d("preço -> ", itemPrice)

                val itemBattery = getString(getColumnIndexOrThrow(COLUMN_NAME_BATTERY))
                Log.d("battery -> ", itemBattery)

                val itemHorsepower = getString(getColumnIndexOrThrow(COLUMN_NAME_HORSEPOWER))
                Log.d("horsepower -> ", itemHorsepower)

                val itemRecharge = getString(getColumnIndexOrThrow(COLUMN_NAME_RECHARGE))
                Log.d("recharge -> ", itemRecharge)

                val itemUrlPhoto = getString(getColumnIndexOrThrow(COLUMN_NAME_URL_PHOTO))
                Log.d("urlPhoto -> ", itemUrlPhoto)

                responseCarsList.add(
                    Car(
                        id = itemId.toInt(),
                        price = itemPrice,
                        battery = itemBattery,
                        horsepower = itemHorsepower,
                        recharge = itemRecharge,
                        urlPhoto = itemUrlPhoto,
                        isFavorite = true
                    )
                )
            }
        }
        cursor.close()
        return responseCarsList
    }

    companion object {
        const val ID_NO_CAR = 0
    }
}