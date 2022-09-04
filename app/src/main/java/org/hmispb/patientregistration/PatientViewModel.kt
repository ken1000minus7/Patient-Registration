package org.hmispb.patientregistration

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.hmispb.patientregistration.model.LoginResponse
import org.hmispb.patientregistration.model.Patient
import org.hmispb.patientregistration.room.PatientRepository
import javax.inject.Inject

@HiltViewModel
class PatientViewModel @Inject constructor(private val patientRepository: PatientRepository) : ViewModel() {
    var patientList = patientRepository.getAllPatients()
    var uploaded : MutableLiveData<Boolean> = MutableLiveData(false)

    fun insertPatient(patient: Patient) {
        viewModelScope.launch(Dispatchers.IO) {
            patientRepository.insertPatient(patient)
        }
    }

    private fun deletePatient(patient: Patient) {
        viewModelScope.launch(Dispatchers.IO) {
            patientRepository.deletePatient(patient)
        }
    }

    fun deleteAllPatients() {
        viewModelScope.launch(Dispatchers.IO) {
            patientRepository.deleteAllPatients()
        }
    }

    private fun savePatient(patient: Patient, response: LoginResponse) {
        viewModelScope.launch {
            patientRepository.savePatient(patient,response)
        }
    }

    fun upload(username : String, password : String) {
        viewModelScope.launch {
            try {
                val response = patientRepository.login(username,password)
                Log.d("yellow",response.toString())
                val patients = patientList.value ?: mutableListOf()
                for(patient in patients) {
                    savePatient(patient,response)
                    deletePatient(patient)
                }
                uploaded.value = true
            } catch (e : Exception) {
                e.printStackTrace()
            }
        }
    }
}