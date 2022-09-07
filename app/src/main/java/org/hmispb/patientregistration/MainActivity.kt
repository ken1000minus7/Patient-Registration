package org.hmispb.patientregistration

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.hmispb.patientregistration.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnNewUser.setOnClickListener {
        val intent = Intent(this, NewPatientActivity::class.java)
            startActivity(intent)
        }
        binding.btnExistingUser.setOnClickListener {
            val intent = Intent(this, ExistingUserActivity::class.java)
            startActivity(intent)
        }
    }
}