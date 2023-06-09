package co.edu.icesi.dev.equineapp.view.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import co.edu.icesi.dev.equineapp.R
import co.edu.icesi.dev.equineapp.databinding.FragmentHomeBinding
import co.edu.icesi.dev.equineapp.model.Appointment
import co.edu.icesi.dev.equineapp.view.appointments.AppointmentFormFragment
import co.edu.icesi.dev.equineapp.view.appointments.MedicalHistoryFormFragment
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_home.*
import java.time.Instant
import java.time.LocalDate
import java.util.Date

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var appointmentsLayoutManager: GridLayoutManager
    private var appointmentsAdapter: AppointmentAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.appointmentImageButton.setOnClickListener {
            val appointmentFormFragment = AppointmentFormFragment()
            setFragment(appointmentFormFragment)
        }

        binding.historyImageButton.setOnClickListener {
            val medicalHistoryFormFragment = MedicalHistoryFormFragment()
            setFragment(medicalHistoryFormFragment)
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
        Firebase.firestore.collection("appointments").whereEqualTo("date", LocalDate.now().toString()).get().addOnCompleteListener { task ->
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