package org.hmispb.patientregistration

import android.content.Intent
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
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.hmispb.patientregistration.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    private lateinit var patientViewModel: PatientViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        patientViewModel = ViewModelProvider(this)[PatientViewModel::class.java]

        binding.btnNewUser.setOnClickListener {
        val intent = Intent(this, NewPatientActivity::class.java)
            startActivity(intent)
        }
        binding.btnExistingUser.setOnClickListener {
            val intent = Intent(this, ExistingUserActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.upload_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val view = LayoutInflater.from(this).inflate(R.layout.login_dialog,null,false)
        val dialog = AlertDialog.Builder(this)
            .setView(view)
            .create()
        dialog.setOnShowListener { dialogInterface ->
            val username = dialog.findViewById<EditText>(R.id.username)
            val password = dialog.findViewById<EditText>(R.id.password)
            val upload = dialog.findViewById<Button>(R.id.upload)
            upload?.setOnClickListener {
                if(username?.text.toString().isEmpty() || password?.text.isNullOrEmpty()) {
                    if(username?.text.toString().isEmpty())
                        username?.error = "Required"
                    if(password?.text.toString().isEmpty())
                        password?.error = "Required"
                    Toast.makeText(this@MainActivity,"One or more fields are empty", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                patientViewModel.patientList.observe(this@MainActivity) { patients ->
                    patientViewModel.upload(username!!.text.toString(),password!!.text.toString(),patients)
                }
            }
            patientViewModel.uploaded.observe(this@MainActivity) { uploaded ->
                lifecycleScope.launch {
                    if(uploaded) {
                        Toast.makeText(this@MainActivity,if(patientViewModel.containsNotUploaded()) "One or more entries were not uploaded" else "Data successfully uploaded", Toast.LENGTH_SHORT).show()
                        dialogInterface.cancel()
                        patientViewModel.uploaded.postValue(false)
                    }
                }
            }
        }
        dialog.show()
        return super.onOptionsItemSelected(item)
    }
}