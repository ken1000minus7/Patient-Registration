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
    fun insertPatient(patient: Patient)

    @Query("SELECT * FROM patient")
    fun getAllPatients() : LiveData<List<Patient>>

    @Delete
    fun deletePatient(patient: Patient)

    @Query("DELETE FROM patient")
    fun deleteAllPatients()
}