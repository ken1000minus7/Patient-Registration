package org.hmispb.patientregistration

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.hmispb.patientregistration.Utils.HOSPITAL_CODE
import org.hmispb.patientregistration.Utils.HOSPITAL_NAME
import org.hmispb.patientregistration.Utils.LOGIN_RESPONSE_PREF
import org.hmispb.patientregistration.Utils.USERNAME
import org.hmispb.patientregistration.Utils.USER_ID
import org.hmispb.patientregistration.databinding.ActivityLoginBinding

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var sharedPref: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        sharedPref = this.getSharedPreferences(
            LOGIN_RESPONSE_PREF,
            Context.MODE_PRIVATE
        )
        if (sharedPref.contains(HOSPITAL_CODE)) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val viewModel: PatientViewModel by viewModels()
        binding.btnSubmit.setOnClickListener {
            lifecycleScope.launch {
                val response = viewModel.login(
                    binding.userName.text.toString(),
                    binding.passWord.text.toString()
                )
                if (response?.dataValue == null) {
                    Toast.makeText(
                        this@LoginActivity,
                        "Incorrect Username/ Password",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val sharedPrefEditor = sharedPref.edit()
                    sharedPrefEditor.putString(HOSPITAL_CODE, response.dataValue[0][0])
                    sharedPrefEditor.putString(HOSPITAL_NAME, response.dataValue[0][1])
                    sharedPrefEditor.putString(USER_ID, response.dataValue[0][2])
                    sharedPrefEditor.putString(USERNAME, response.dataValue[0][3])
                    sharedPrefEditor.apply()
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}