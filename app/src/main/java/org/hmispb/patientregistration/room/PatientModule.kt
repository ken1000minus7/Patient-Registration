package org.hmispb.patientregistration.room

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.hmispb.patientregistration.Utils.password
import org.hmispb.patientregistration.Utils.username
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PatientModule {
    @Provides
    @Singleton
    fun providePatientDatabase(app : Application) : PatientDatabase {
        return Room.databaseBuilder(
            app,
            PatientDatabase::class.java,
            "patient_database"
        ).build()
    }

    @Provides
    @Singleton
    fun providePatientRepository(patientDatabase: PatientDatabase, patientApi: PatientApi) : PatientRepository {
        return PatientRepositoryImpl(patientDatabase.patientDao,patientApi)
    }

    @Provides
    @Singleton
    fun providePatientApi(app : Application) : PatientApi{

        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        val cacheSize = (10*1024*1024).toLong()
        val cache = Cache(app.cacheDir, cacheSize)
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .cache(cache)
            .addInterceptor(BasicAuthInterceptor(username,
                password))
            .addInterceptor(logging)
            .addInterceptor{
                var request = it.request()
                request = if (hasNetwork(app) ==true)
                    request.newBuilder().header("Cache-Control","public, max-age="+5).build()
                else
                    request.newBuilder().header("Cache-Control","public, only-if-cached, max-stale="+60*60*24*7).build()
                it.proceed(request)
            }
            .build()
        return Retrofit.Builder()
            .baseUrl("https://hmispb.in/HISUtilities/services/restful/EMMSMasterDataWebService/DMLService/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(PatientApi::class.java)
    }

    private fun hasNetwork(app : Application): Boolean? {
        var isConnected: Boolean? = false
        val connectivityManager = app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        if (activeNetwork != null && activeNetwork.isConnected)
            isConnected = true
        return isConnected
    }
}