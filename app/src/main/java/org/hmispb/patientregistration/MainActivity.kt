package org.hmispb.patientregistration

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.gson.Gson
import org.hmispb.patientregistration.Utils.OPD_COUNTER
import org.hmispb.patientregistration.Utils.OPD_ID
import org.hmispb.patientregistration.Utils.PREV_DATE
import org.hmispb.patientregistration.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences(OPD_COUNTER, MODE_PRIVATE)
        val dateString = sharedPreferences.getString(PREV_DATE,"")
        val prevDate = if(dateString.isNullOrEmpty()) Date(1) else Gson().fromJson(dateString,Date::class.java)
        val currentDate = Date()
        if(prevDate.before(currentDate) && prevDate.day!=currentDate.day) {
            sharedPreferences.edit()
                .putInt(OPD_ID,0)
                .putString(PREV_DATE,Gson().toJson(currentDate))
                .commit()
        }

        binding.btnNewUser.setOnClickListener {
        val intent = Intent(this, NewPatientActivity::class.java)
            startActivity(intent)
        }
        binding.btnExistingUser.setOnClickListener {
            val intent = Intent(this, ExistingUserActivity::class.java)
            startActivity(intent)
        }
    }
}