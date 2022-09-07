package org.hmispb.patientregistration.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import org.hmispb.patientregistration.model.Patient

@Dao
interface PatientDao {
    @Insert
    suspend fun insertPatient(patient: Patient)

    @Query("SELECT * FROM patient")
    fun getAllPatients() : LiveData<List<Patient>>

    @Delete
    suspend fun deletePatient(patient: Patient)

    @Query("DELETE FROM patient")
    suspend fun deleteAllPatients()

    @Query("SELECT * FROM patient WHERE crNo =:crNumber")
    suspend  fun searchPatientByCRNumber(crNumber: String) : Patient?

    @Query("UPDATE patient SET isUploaded=1 WHERE crNo=:crNo")
    suspend fun setUploaded(crNo : String)

    @Query("SELECT count(*) FROM patient WHERE isUploaded=0")
    suspend fun notUploadedCount() : Int
}