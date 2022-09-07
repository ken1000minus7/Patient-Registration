package org.hmispb.patientregistration.room

import androidx.lifecycle.LiveData
import org.hmispb.patientregistration.model.LoginResponse
import org.hmispb.patientregistration.model.Patient

interface PatientRepository {
    suspend fun insertPatient(patient: Patient)

    fun getAllPatients() : LiveData<List<Patient>>

    suspend fun deletePatient(patient: Patient)

    suspend fun deleteAllPatients()

    suspend fun savePatient(patient: Patient, hospitalCode: String, seatID: String)

    suspend fun login(username : String, password : String) : LoginResponse?

    suspend fun searchPatientByCRNumber(crNumber: String) : Patient?

    suspend fun setUploaded(crNo : String)

    suspend fun containsNotUploaded() : Boolean
}