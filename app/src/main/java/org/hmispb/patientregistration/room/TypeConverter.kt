package org.hmispb.patientregistration.room

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object TypeConverter {
    @TypeConverter
    fun oldCrsToJSON(oldCrs : MutableList<String>) : String = Gson().toJson(oldCrs)

    @TypeConverter
    fun oldCrsFromJSON(json : String) : MutableList<String> = Gson().fromJson(json,object: TypeToken<MutableList<String>>() {}.type) ?: mutableListOf()
}