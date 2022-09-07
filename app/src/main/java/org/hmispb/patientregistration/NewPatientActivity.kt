package org.hmispb.patientregistration

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import org.hmispb.patientregistration.Utils.HOSPITAL_CODE
import org.hmispb.patientregistration.Utils.LOGIN_RESPONSE_PREF
import org.hmispb.patientregistration.Utils.USER_ID
import org.hmispb.patientregistration.databinding.ActivityNewPatientBinding
import org.hmispb.patientregistration.model.Data
import org.hmispb.patientregistration.model.Patient

@AndroidEntryPoint
class NewPatientActivity : AppCompatActivity() {
    private lateinit var binding : ActivityNewPatientBinding
    private lateinit var patientViewModel: PatientViewModel
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var hospitalAndUserIDPref : SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewPatientBinding.inflate(layoutInflater)
        setContentView(binding.root)

        patientViewModel = ViewModelProvider(this)[PatientViewModel::class.java]
        sharedPreferences = getSharedPreferences("opdCounter", MODE_PRIVATE)
        hospitalAndUserIDPref = getSharedPreferences(LOGIN_RESPONSE_PREF, MODE_PRIVATE)
        val hospitalCode = hospitalAndUserIDPref.getString(HOSPITAL_CODE, "")
        val userID = hospitalAndUserIDPref.getString(USER_ID, "")
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

                Toast.makeText(this@NewPatientActivity,"One or more fields are empty",Toast.LENGTH_SHORT).show()

                return@setOnClickListener
            }

            val id = sharedPreferences.getInt("id",0)
            val patient = Patient(
                id = id,
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
                patGenderCodeId = when(binding.genderRadioGroup.checkedRadioButtonId) {
                    R.id.male -> "M"
                    R.id.female -> "F"
                    else -> "T"
                },
                patGuardianName = binding.father.text.toString(),
                patHusbandName = binding.spouse.text.toString(),
                patMotherName = binding.mother.text.toString(),
                patAddCountryCodeId = data.country[binding.countries.selectedItemPosition].countryCode,
                patAddStateCodeId = data.state[binding.states.selectedItemPosition].stateCode,
                patAddMobileNo = java.lang.Long.parseLong(binding.number.text.toString())
            )
            patientViewModel.insertPatient(patient)
            sharedPreferences.edit()
                .putInt("id",id+1)
                .commit()
            Toast.makeText(this, "New OPD number $id", Toast.LENGTH_SHORT).show()

            try{
                patientViewModel.savePatient(
                    patient,
                   hospitalCode ?: "",
                    userID ?: ""
                )
            } catch (e:Exception){
                e.printStackTrace()
            }

        }

        patientViewModel.patientList.observe(this) { patients ->
            Log.d("hello", patients.size.toString())
            Log.d("hello",patients.toString())
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.upload_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val view = LayoutInflater.from(this).inflate(R.layout.login_dialog,null,false)
        val dialog = AlertDialog.Builder(this)
            .setView(view)
            .create()
        dialog.setOnShowListener { dialogInterface ->
            val username = dialog.findViewById<EditText>(R.id.username)
            val password = dialog.findViewById<EditText>(R.id.password)
            val upload = dialog.findViewById<Button>(R.id.upload)
            upload?.setOnClickListener {
                if(username?.text.toString().isEmpty() || password?.text.isNullOrEmpty()) {
                    if(username?.text.toString().isEmpty())
                        username?.error = "Required"
                    if(password?.text.toString().isEmpty())
                        password?.error = "Required"
                    Toast.makeText(this@NewPatientActivity,"One or more fields are empty",Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                patientViewModel.upload(username!!.text.toString(),password!!.text.toString())
            }
            patientViewModel.uploaded.observe(this@NewPatientActivity) { uploaded ->
                if(uploaded) {
                    Toast.makeText(this@NewPatientActivity,"Data successfully uploaded",Toast.LENGTH_SHORT).show()
                    dialogInterface.cancel()
                    patientViewModel.uploaded.value = false
                }
            }
        }
        dialog.show()
        return super.onOptionsItemSelected(item)
    }
}