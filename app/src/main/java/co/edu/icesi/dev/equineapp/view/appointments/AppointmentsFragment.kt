package co.edu.icesi.dev.equineapp.view.appointments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import co.edu.icesi.dev.equineapp.R
import co.edu.icesi.dev.equineapp.databinding.FragmentAppointmentsBinding
import co.edu.icesi.dev.equineapp.model.Appointment
import co.edu.icesi.dev.equineapp.view.home.AppointmentAdapter
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_appointments.calendar
import kotlinx.android.synthetic.main.fragment_home.*
import java.time.Instant
import java.time.LocalDate
import java.util.Date

class AppointmentsFragment : Fragment() {

    private lateinit var binding: FragmentAppointmentsBinding
    private lateinit var appointmentsLayoutManager: GridLayoutManager
    private var appointmentsAdapter: AppointmentAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentAppointmentsBinding.inflate(inflater, container, false)

        binding.calendar.setOnClickListener {
            val datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Seleccionar fecha")
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .build()

            datePicker.show(requireActivity().supportFragmentManager, "Date Picker")

            datePicker.addOnPositiveButtonClickListener {
                // Respond to positive button click.
                Log.d(">>>", it.toString())
            }
            datePicker.addOnNegativeButtonClickListener {
                // Respond to negative button click.
            }
            datePicker.addOnCancelListener {
                // Respond to cancel button click.
            }
            datePicker.addOnDismissListener {
                // Respond to dismiss events.
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.appointmentsLayoutManager = GridLayoutManager(context, 2)
        appointmentsRecyclerView.layoutManager = appointmentsLayoutManager
        appointmentsRecyclerView.setHasFixedSize(true)
        appointmentsAdapter = AppointmentAdapter(this)
        appointmentsRecyclerView.adapter = appointmentsAdapter
        loadAppointmentsFromFirebase()
    }

    private fun loadAppointmentsFromFirebase() {
        val dateStr = ""
        val date = dateStr.ifEmpty { LocalDate.now().toString() }
        Firebase.firestore.collection("appointments").whereEqualTo("date", date).get().addOnCompleteListener { task ->
            for (doc in task.result!!) {
                val appointment = doc.toObject(Appointment::class.java)
                appointmentsAdapter?.addAppointment(appointment)
            }
        }
    }

    private fun setFragment(fragment: Fragment) =
        requireActivity().supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_wrapper, fragment)
            addToBackStack(null)
            commit()
        }
}