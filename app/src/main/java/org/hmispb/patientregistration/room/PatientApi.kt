package org.hmispb.patientregistration.room

import org.hmispb.patientregistration.model.LoginRequest
import org.hmispb.patientregistration.model.LoginResponse
import org.hmispb.patientregistration.model.SavePatientRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface PatientApi {
    @POST("saveRegistration")
    suspend fun savePatient(
        @Body patientRequest: SavePatientRequest
    )

    @POST("Login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ) : LoginResponse
}