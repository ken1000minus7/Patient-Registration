package org.hmispb.patientregistration

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.DatePicker
import org.hmispb.patientregistration.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val calendar = Calendar.getInstance()
        setContentView(binding.root)
        binding.dob.setOnClickListener {
            val dialog = DatePickerDialog(this@MainActivity, { view, year, month, day ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH,month)
                calendar.set(Calendar.DAY_OF_MONTH,day)
                binding.dob.setText("${calendar.get(Calendar.DAY_OF_MONTH)}/${calendar.get(Calendar.MONTH)}/${calendar.get(Calendar.YEAR)}")
            },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH))
            dialog.show()
        }
    }
}