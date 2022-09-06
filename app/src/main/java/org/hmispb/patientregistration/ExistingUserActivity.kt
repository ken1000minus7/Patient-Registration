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
import java.util.*

@AndroidEntryPoint
class ExistingUserActivity : AppCompatActivity() {
    private lateinit var patientViewModel: PatientViewModel
    lateinit var binding: ActivityExistingUserBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExistingUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        patientViewModel = ViewModelProvider(this)[PatientViewModel::class.java]
        val calender = Calendar.getInstance()
        val year = calender.get(Calendar.YEAR)
        val month = calender.get(Calendar.MONTH)
        val day = calender.get(Calendar.DAY_OF_MONTH)
        binding.crNoInit.text = "$day$month$year"

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
                    Toast.makeText(this@ExistingUserActivity,"One or more fields are empty", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                patientViewModel.upload(username!!.text.toString(),password!!.text.toString())
            }
            patientViewModel.uploaded.observe(this@ExistingUserActivity) { uploaded ->
                if(uploaded) {
                    Toast.makeText(this@ExistingUserActivity,"Data successfully uploaded", Toast.LENGTH_SHORT).show()
                    dialogInterface.cancel()
                    patientViewModel.uploaded.value = false
                }
            }
        }
        dialog.show()
        return super.onOptionsItemSelected(item)
    }
}