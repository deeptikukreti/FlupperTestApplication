package com.live.fluppertestapplication.colorPickerDialog

interface ColorListener {

    /**
     *
     * Call when user select color
     *
     * @param color Color Resource
     * @param colorHex Hex String of Color Resource
     */
    //fun onColorSelected(color: Int, colorHex:String)
    fun onColorSelected(colorHex: ArrayList<String>)
}
