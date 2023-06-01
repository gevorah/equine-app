package co.edu.icesi.dev.equineapp.view.appointments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import co.edu.icesi.dev.equineapp.R
import co.edu.icesi.dev.equineapp.databinding.FragmentAppointmentInfoBinding
import co.edu.icesi.dev.equineapp.model.Appointment
import co.edu.icesi.dev.equineapp.view.home.HomeFragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.*

class AppointmentInfoFragment (private val appointment: Appointment): Fragment()  {

    private lateinit var binding: FragmentAppointmentInfoBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentAppointmentInfoBinding.inflate(inflater, container, false)

        binding.backPetInfoButton.setOnClickListener{
            activity?.supportFragmentManager?.popBackStack()
        }
        binding.homeBttn.setOnClickListener{
            val homeFragment = HomeFragment()
            setFragment(homeFragment)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //datos de la cita
        super.onViewCreated(view, savedInstanceState)

        binding.textViewHorseName.text = appointment.horseName
        binding.textViewAddres.text = appointment.address
        binding.textViewPhone.text = appointment.phoneNumber
        binding.textViewDescription.text = appointment.reason
        binding.textViewDate.text = appointment.date
        binding.textViewHour.text = appointment.time

    }

    private fun setFragment(fragment: Fragment) = requireActivity().supportFragmentManager.beginTransaction().apply {
        replace(R.id.fl_wrapper, fragment)
        addToBackStack(null)
        commit()
    }

}