package com.example.cupcake.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

// price per cupcake
private const val PRICE_PER_CUPCAKE = 2.00

// price for cupcake but the same day pickup
private const val PRICE_FOR_SAME_DAY_PICKUP = 3.00

class OrderViewModel : ViewModel() {
    var index = 0
    // for quantity
    private val _quantity = MutableLiveData<Int>()
    val quantity: LiveData<Int> = _quantity

    // for flavor
    private val _flavor = MutableLiveData<String>()
    val flavor: LiveData<String> = _flavor

    // for list flavor
    private val _listOfFlavor = MutableLiveData<ArrayList<String>>()
    val listOfFlavor: MutableLiveData<ArrayList<String>> = _listOfFlavor


    // for date
    private val _date = MutableLiveData<String>()
    val date: LiveData<String> = _date

    // for price
    private val _price = MutableLiveData<Double>()
    val price: LiveData<String> = Transformations.map(_price) {
        // format the price to user currency
        NumberFormat.getCurrencyInstance().format(it)
    }

    // for date options
    val dateOptions = getPickupOptions()

    // for date
    private val _specialFlavor = MutableLiveData<Boolean>()
    val specialFlavor: LiveData<Boolean> = _specialFlavor


    init {
        resetOrder()
    }

    fun setQuantity(numberCupcakes: Int) {
        _quantity.value = numberCupcakes
        // update the price
        updatePrice()
    }

    fun setFlavor(desiredFlavor: String) {
        _flavor.value = desiredFlavor
        index += 1

        val list = listOfFlavor.value.toString()

        // update the price
        updatePrice()
    }

    fun setDate(pickupDate: String) {
        _date.value = pickupDate
        // update the price
        updatePrice()
    }

    /*
    * check if user set the flavor or not
    */
    fun hasNoFlavorSet(): Boolean {
        return _flavor.value.isNullOrEmpty()
    }

    /*
    * to create and return list of pickup dates
    */
    private fun getPickupOptions(): List<String> {
        // create list of option
        val options = mutableListOf<String>()
        // create date format
        val formatter = SimpleDateFormat("E MMM d", Locale.getDefault())
        // get the calendar
        val calendar = Calendar.getInstance()
        // Create a list of dates starting with the current date and the following 3 dates
        repeat(4) {
            options.add(formatter.format(calendar.time))
            calendar.add(Calendar.DATE, 1)
        }
        return options
    }

    // reset the data
    fun resetOrder() {
        _quantity.value = 0
        _flavor.value = ""
        _date.value = dateOptions[0]
        _price.value = 0.0
    }

    // update the total price
    private fun updatePrice() {
        var calculatedPrice = (quantity.value ?: 0) * PRICE_PER_CUPCAKE
        // If the user selected the first option (today) for pickup, add the surcharge
        if (dateOptions[0] == _date.value) {
            calculatedPrice += PRICE_FOR_SAME_DAY_PICKUP
        }
        //  update the price value
        _price.value = calculatedPrice
    }

}