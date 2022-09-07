package org.hmispb.patientregistration

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
import org.hmispb.patientregistration.databinding.ActivityExistingUserBinding

@AndroidEntryPoint
class ExistingUserActivity : AppCompatActivity() {
    private lateinit var patientViewModel: PatientViewModel
    lateinit var binding: ActivityExistingUserBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExistingUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        patientViewModel = ViewModelProvider(this)[PatientViewModel::class.java]
    }
}