package org.hmispb.patientregistration

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.hmispb.patientregistration.databinding.ActivityExistingUserBinding

@AndroidEntryPoint
class ExistingUserActivity : AppCompatActivity() {
    private lateinit var patientViewModel: PatientViewModel
    private lateinit var sharedPreferences: SharedPreferences
    lateinit var binding: ActivityExistingUserBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExistingUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences = getSharedPreferences(Utils.OPD_COUNTER, MODE_PRIVATE)
        patientViewModel = ViewModelProvider(this)[PatientViewModel::class.java]
        binding.btnSubmit.setOnClickListener {
            lifecycleScope.launch  {
                if (patientViewModel.searchPatientByCRNumber(binding.crNumber.text.toString())) {
                    val id = sharedPreferences.getInt(Utils.OPD_ID, 0)
                    val opdId = id.toString()
                    sharedPreferences.edit()
                        .putInt(Utils.OPD_ID, id + 1)
                        .commit()

                    val dialogView =
                        LayoutInflater.from(this@ExistingUserActivity)
                            .inflate(R.layout.confirmation_verified_existing_user, null, false)
                    val dialog = AlertDialog.Builder(this@ExistingUserActivity)
                        .setView(dialogView)
                        .setPositiveButton(
                            "Ok"
                        ) { p0, p1 ->
                            p0.cancel()
                            finish()
                        }
                        .create()
                    dialog.setCanceledOnTouchOutside(false)
                    dialog.setOnShowListener {

                        val opdText: TextView? = dialog.findViewById(R.id.new_opd_no)
                        opdText?.text = opdId
                    }
                    dialog.show()
                } else {
                    Toast.makeText(this@ExistingUserActivity, "Wrong CR number", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.upload_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val view = LayoutInflater.from(this).inflate(R.layout.login_dialog, null, false)
        val dialog = AlertDialog.Builder(this)
            .setView(view)
            .create()
        dialog.setOnShowListener { dialogInterface ->
            val username = dialog.findViewById<EditText>(R.id.username)
            val password = dialog.findViewById<EditText>(R.id.password)
            val upload = dialog.findViewById<Button>(R.id.upload)
            upload?.setOnClickListener {
                if (username?.text.toString().isEmpty() || password?.text.isNullOrEmpty()) {
                    if (username?.text.toString().isEmpty())
                        username?.error = "Required"
                    if (password?.text.toString().isEmpty())
                        password?.error = "Required"
                    Toast.makeText(
                        this@ExistingUserActivity,
                        "One or more fields are empty",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
                patientViewModel.upload(username!!.text.toString(), password!!.text.toString())
            }
            patientViewModel.uploaded.observe(this@ExistingUserActivity) { uploaded ->
                if (uploaded) {
                    Toast.makeText(
                        this@ExistingUserActivity,
                        "Data successfully uploaded",
                        Toast.LENGTH_SHORT
                    ).show()
                    dialogInterface.cancel()
                    patientViewModel.uploaded.value = false
                }
            }
        }
        dialog.show()
        return super.onOptionsItemSelected(item)
    }
}