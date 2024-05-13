package com.example.eletriccars.ui


import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.eletriccars.R

class CalculateAutonomyActivity : AppCompatActivity() {

    lateinit var closeBtn: ImageView
    lateinit var priceKwh: EditText
    lateinit var kmTraveled: EditText
    lateinit var btnCalculate: Button
    lateinit var result: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculate_autonomy)
        setupViews()
        setupListeners()

        setupCachedResult()
    }

    private fun setupViews() {
        closeBtn = findViewById(R.id.iv_close_btn)
        priceKwh = findViewById(R.id.et_price_kwh)
        kmTraveled = findViewById(R.id.et_km_traveled)
        btnCalculate = findViewById(R.id.btn_calculate)
        result = findViewById(R.id.tv_result)

    }

    private fun setupListeners() {
        btnCalculate.setOnClickListener() {
            calculate()
        }
        closeBtn.setOnClickListener() {
            finish()
        }


    }

    private fun calculate() {
        val km = kmTraveled.text.toString().toFloat()
        val price = priceKwh.text.toString().toFloat()

        val response = (km / price)

        result.text = response.toString()
        saveSharedPref(response)
    }

    fun saveSharedPref(value: Float) {
        val sharedPref = getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putFloat(getString(R.string.saved_calc), value)
            apply()
        }
    }

    fun getSharedPref(): Float {
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        return sharedPref.getFloat(getString(R.string.saved_calc), 0.0F)
    }


    private fun setupCachedResult() {
        val calculatedValue = getSharedPref()
        result.text = calculatedValue.toString()
    }


}