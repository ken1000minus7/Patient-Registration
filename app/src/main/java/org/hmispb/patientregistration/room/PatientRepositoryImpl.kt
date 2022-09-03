package org.hmispb.patientregistration.room

import androidx.lifecycle.LiveData
import com.google.gson.Gson
import org.hmispb.patientregistration.model.Patient
import org.hmispb.patientregistration.model.SavePatientRequest

class PatientRepositoryImpl(private val patientDao: PatientDao, private val patientApi: PatientApi) : PatientRepository {

    override fun insertPatient(patient: Patient) {
        patientDao.insertPatient(patient)
    }

    override fun getAllPatients(): LiveData<List<Patient>> {
        return patientDao.getAllPatients()
    }

    override fun deletePatient(patient: Patient) {
        patientDao.deletePatient(patient)
    }

    override fun deleteAllPatients() {
        patientDao.deleteAllPatients()
    }

    override suspend fun savePatient(patient: Patient) {
        val patientString = Gson().toJson(patient)
        // TODO : hospitalCode and seatId unknown
        val request = SavePatientRequest(0,0,patientString)
        patientApi.savePatient(request)
    }
}