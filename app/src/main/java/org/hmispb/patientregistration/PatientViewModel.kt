package org.hmispb.patientregistration

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

    fun savePatient(patient: Patient, hospitalCode: String, userID: String) {
        viewModelScope.launch {
            patientRepository.savePatient(patient, hospitalCode, userID)
        }
    }

    fun upload(username : String, password : String) {
        viewModelScope.launch {
            try {
                val response = patientRepository.login(username,password)
                val patients = patientList.value ?: mutableListOf()
                for(patient in patients) {
                    if(response!=null) savePatient(patient, response.dataValue!![0][0], response.dataValue[0][2])
                    deletePatient(patient)
                }
                uploaded.value = true
            } catch (e : Exception) {
                e.printStackTrace()
            }
        }
    }


    suspend fun login(username : String, password : String) : LoginResponse? =
        patientRepository.login(username, password)

}