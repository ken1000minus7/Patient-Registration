package org.hmispb.patientregistration.room

import android.util.Log
import androidx.lifecycle.LiveData
import com.google.gson.Gson
import org.hmispb.patientregistration.model.LoginRequest
import org.hmispb.patientregistration.model.LoginResponse
import org.hmispb.patientregistration.model.Patient
import org.hmispb.patientregistration.model.SavePatientRequest

class PatientRepositoryImpl(private val patientDao: PatientDao, private val patientApi: PatientApi) : PatientRepository {

    override suspend fun insertPatient(patient: Patient) {
        patientDao.insertPatient(patient)
    }

    override fun getAllPatients(): LiveData<List<Patient>> {
        return patientDao.getAllPatients()
    }

    override suspend fun deletePatient(crNo: String) {
        patientDao.deletePatient(crNo)
    }

    override suspend fun deleteAllPatients() {
        patientDao.deleteAllPatients()
    }

    override suspend fun savePatient(patient: Patient, hospitalCode: String, seatID: String) {

        val patientString = Gson().toJson(patient)
        Log.d("yellow", patientString)
        val request = SavePatientRequest(
            hospitalCode = hospitalCode,
            seatId = seatID,
            inputDataJson = patientString
        )
        patientApi.savePatient(request)
    }

    override suspend fun login(username: String, password: String): LoginResponse? {
        return patientApi.login(LoginRequest(listOf(username, password)))
    }

    override suspend fun searchPatientByCRNumber(crNumber: String) : Patient? {
        return patientDao.searchPatientByCRNumber(crNumber)
    }

    override suspend fun setUploaded(crNo: String) {
        Log.d("crno",crNo)
        patientDao.setUploaded(crNo)
    }

    override suspend fun containsNotUploaded(): Boolean {
        return patientDao.notUploadedCount()>0
    }
}