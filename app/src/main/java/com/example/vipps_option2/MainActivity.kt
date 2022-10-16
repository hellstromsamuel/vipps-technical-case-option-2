package com.example.vipps_option2

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    private lateinit var inputField: EditText
    private lateinit var searchButton: Button
    private lateinit var countryNameTV: TextView
    private lateinit var countryCapitalTV: TextView
    private lateinit var countryAltSpellingsTV: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        // Setting references to UI elements
        inputField = findViewById(R.id.input)
        searchButton = findViewById(R.id.button)
        countryNameTV = findViewById(R.id.country_name)
        countryCapitalTV = findViewById(R.id.country_capital)
        countryAltSpellingsTV = findViewById(R.id.country_alt_spellings)

        // Handle onClick searchButton
        searchButton.setOnClickListener {
            // Hide keyboard
            it.hideKeyboard()

            // Async API call through ViewModel
            val countryQuery = inputField.text.toString()
            viewModel.getCountryData(countryQuery)

            // Observing when the data updates from the API
            viewModel.countryApiLiveData().observe(this, Observer { countryData ->
                if (countryData != null) {
                    countryNameTV.text = countryData.name
                    countryCapitalTV.text = countryData.capital
                    countryAltSpellingsTV.text = countryData.altSpellings?.joinToString("\n")
                } else {
                    countryNameTV.text = getString(R.string.message_response_fail)
                    inputField.setText("")
                    countryCapitalTV.text = ""
                    countryAltSpellingsTV.text = ""
                }
            })
        }
    }

    private fun View.hideKeyboard() {
        val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }


}