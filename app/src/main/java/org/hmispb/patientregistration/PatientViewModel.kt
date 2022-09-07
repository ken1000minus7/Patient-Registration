package org.hmispb.patientregistration

import android.util.Log
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

    init {
        Log.d("poppy",patientList.value.toString())
    }

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

    private fun savePatient(patient: Patient, hospitalCode: String, userID: String) {
        viewModelScope.launch {
            patientRepository.savePatient(patient, hospitalCode, userID)
        }
    }

    private fun setUploaded(crNo : String) {
        viewModelScope.launch(Dispatchers.IO) {
            patientRepository.setUploaded(crNo)
        }
    }

    fun upload(username : String, password : String,patients : List<Patient>) {
        viewModelScope.launch {
            try {
                val response = login(username,password)
                for(patient in patients) {
                    Log.d("pop",patient.toString())
                    if(response!=null && !patient.isUploaded) {
                        try {
                            savePatient(patient, response.dataValue!![0][0], response.dataValue[0][2])
                            setUploaded(patient.crNo)
                        } catch (e : Exception) {
                            e.printStackTrace()
                        }
                    }
                }
                uploaded.postValue(true)
            } catch (e : Exception) {
                e.printStackTrace()
            }
        }
    }

    suspend fun login(username : String, password : String) : LoginResponse? =
        patientRepository.login(username, password)

    suspend fun containsNotUploaded() : Boolean {
        return patientRepository.containsNotUploaded()
    }
}