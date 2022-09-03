package org.hmispb.patientregistration

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.DatePicker
import com.google.gson.Gson
import org.hmispb.patientregistration.databinding.ActivityMainBinding
import org.hmispb.patientregistration.model.Data
import org.hmispb.patientregistration.model.Patient
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val jsonString = resources!!.openRawResource(R.raw.data).bufferedReader().use { it.readText() }
        val data = Gson().fromJson(jsonString, Data::class.java)

        val ageUnits = arrayOf("Years","Months","Weeks","Days")
        val ageAdapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,ageUnits)
        binding.ageUnits.adapter = ageAdapter

        val countryList = mutableListOf<String>()
        for(country in data.country) {
            countryList.add(country.countryName)
        }
        val countryAdapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,countryList)
        binding.countries.adapter = countryAdapter
        binding.countries.setSelection(countryList.indexOf("India"))

        val stateList = mutableListOf<String>()
        for(state in data.state) {
            stateList.add(state.stateName)
        }
        val stateAdapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,stateList)
        binding.states.adapter = stateAdapter
        binding.states.setSelection(countryList.indexOf("Punjab"))

        binding.submit.setOnClickListener {
            if(
                binding.firstName.text.isNullOrEmpty() ||
                binding.age.text.isNullOrEmpty() ||
                binding.genderRadioGroup.checkedRadioButtonId==-1 ||
                binding.street.text.isNullOrEmpty() ||
                binding.city.text.isNullOrEmpty() ||
                binding.number.text.isNullOrEmpty()
            ) {
                if(binding.firstName.text.isNullOrEmpty())
                    binding.firstName.error = "Required"

                if(binding.age.text.isNullOrEmpty())
                    binding.age.error = "Required"

                if(binding.street.text.isNullOrEmpty())
                    binding.street.error = "Required"

                if(binding.city.text.isNullOrEmpty())
                    binding.city.error = "Required"

                if(binding.number.text.isNullOrEmpty())
                    binding.number.error = "Required"

                return@setOnClickListener
            }

            val patient = Patient(
                patFirstName = binding.firstName.text.toString(),
                patMiddleName = binding.middleName.text.toString(),
                patLastName = binding.lastName.text.toString(),
                patAgeId = Integer.parseInt(binding.age.text.toString()),
                patAgeUnitId = when(binding.ageUnits.selectedItemPosition) {
                    1 -> "Mth"
                    2 -> "Wk"
                    3 -> "D"
                    else -> "Yr"
                },
                patGuardianName = binding.father.text.toString(),
                patHusbandName = binding.spouse.text.toString(),
                patMotherName = binding.mother.text.toString(),
                patAddCountryCodeId = data.country[binding.countries.selectedItemPosition].countryCode,
                patAddStateCodeId = data.state[binding.states.selectedItemPosition].stateCode,
                patAddMobileNo = Integer.parseInt(binding.number.text.toString())
            )
        }
    }
}