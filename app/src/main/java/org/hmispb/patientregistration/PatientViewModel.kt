package org.hmispb.patientregistration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.hmispb.patientregistration.model.Patient
import org.hmispb.patientregistration.room.PatientRepository
import javax.inject.Inject

@HiltViewModel
class PatientViewModel @Inject constructor(private val patientRepository: PatientRepository) : ViewModel() {
    var patientList = patientRepository.getAllPatients()

    fun insertPatient(patient: Patient) {
        viewModelScope.launch(Dispatchers.IO) {
            patientRepository.insertPatient(patient)
        }
    }

    fun deletePatient(patient: Patient) {
        viewModelScope.launch(Dispatchers.IO) {
            patientRepository.deletePatient(patient)
        }
    }

    fun deleteAllPatients() {
        viewModelScope.launch(Dispatchers.IO) {
            patientRepository.deleteAllPatients()
        }
    }

    fun savePatient(patient: Patient) {
        viewModelScope.launch {
            patientRepository.savePatient(patient)
        }
    }
}