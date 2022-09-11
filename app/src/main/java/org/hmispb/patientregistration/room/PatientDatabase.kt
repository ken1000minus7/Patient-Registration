package org.hmispb.patientregistration.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.hmispb.patientregistration.model.Patient

@Database(
    entities = [Patient::class],
    version = 1,
    exportSchema = false
)

@TypeConverters(TypeConverter::class)
abstract class PatientDatabase : RoomDatabase() {
    abstract val patientDao : PatientDao
}