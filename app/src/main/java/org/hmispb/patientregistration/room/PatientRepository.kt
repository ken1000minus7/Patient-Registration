package org.hmispb.patientregistration.room

import androidx.lifecycle.LiveData
import org.hmispb.patientregistration.model.LoginResponse
import org.hmispb.patientregistration.model.Patient

interface PatientRepository {
    fun insertPatient(patient: Patient)

    fun getAllPatients() : LiveData<List<Patient>>

    fun deletePatient(patient: Patient)

    fun deleteAllPatients()

    suspend fun savePatient(patient: Patient,response: LoginResponse?)

    suspend fun login(username : String, password : String) : LoginResponse?
}