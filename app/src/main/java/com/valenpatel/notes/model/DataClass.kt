package com.valenpatel.notes.model

import java.io.Serializable

data class DataClass(val title: String, val description: String, val price: String, val quantity: String, val more: String, val date: String, val time: String, val key : String, val image1: String?, val image2 : String?, val image3 : String?, val image4 : String?, var backgroundColor : String) :Serializable{
    constructor() : this("", "", "", "", "", "", "","","","","","","")  //primary constructor



}
