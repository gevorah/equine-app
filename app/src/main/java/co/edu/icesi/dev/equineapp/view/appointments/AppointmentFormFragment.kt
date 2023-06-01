package co.edu.icesi.dev.equineapp.view.appointments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import co.edu.icesi.dev.equineapp.R
import co.edu.icesi.dev.equineapp.databinding.FragmentAppointmentFormBinding
import co.edu.icesi.dev.equineapp.model.Appointment
import co.edu.icesi.dev.equineapp.model.Horse
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList

class AppointmentFormFragment () : Fragment(){

    private lateinit var binding: FragmentAppointmentFormBinding
    private lateinit var galleryLauncher: ActivityResultLauncher<Intent>
    private var uri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAppointmentFormBinding.inflate(inflater, container, false)

        val horses = ArrayList<Horse>()
        Firebase.firestore.collection("horses").get().addOnCompleteListener { task ->
            for (doc in task.result!!) {
                val horse = doc.toObject(Horse::class.java)
                horses.add(horse)
            }
        }
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, horses)
        (binding.menu.editText as? AutoCompleteTextView)?.setAdapter(adapter)

        binding.registerButton.setOnClickListener {
                val medicalHistoryFormFragment = MedicalHistoryFormFragment()
                setFragment(medicalHistoryFormFragment)
        }

        binding.saveBttn.setOnClickListener {
           val idAppointment = UUID.randomUUID().toString()
            if(publish(idAppointment)){
                Firebase.firestore.collection("appointments").document(idAppointment).get().addOnSuccessListener {
                    val appointment = it.toObject(Appointment::class.java)
                    val appointmentInfoFragment = AppointmentInfoFragment(appointment!!)
                    setFragment(appointmentInfoFragment)
                }
            }
        }
        binding.backButton.setOnClickListener{
            activity?.supportFragmentManager?.popBackStack()
        }
        return binding.root
    }

    private fun setFragment(fragment: Fragment) = requireActivity().supportFragmentManager.beginTransaction().apply {
        replace(R.id.fl_wrapper, fragment)
        addToBackStack(null)
        commit()
    }



    private fun publish(id:String): Boolean {
        if(!checkIfNotBlankOrEmpty(binding.complete.text.toString()))
            return false
        else if(!checkIfNotBlankOrEmpty(binding.editTextTextAddress.text.toString()))
            return false
        else if(!checkIfNotBlankOrEmpty(binding.editTextTextPhoneNumber.text.toString()))
            return false
        else if(!checkIfNotBlankOrEmpty(binding.editTextTextMotive.text.toString()))
            return false
        else if(!checkIfNotBlankOrEmpty(binding.editTextTextDate.text.toString()))
            return false
        else if(!checkIfNotBlankOrEmpty(binding.editTextTextHour.text.toString()))
            return false

        val horseId=""
        val horseName= binding.complete.text.toString()
        val address = binding.editTextTextAddress.text.toString()
        val phoneNumber = binding.editTextTextPhoneNumber.text.toString()
        val reason = binding.editTextTextMotive.text.toString()
        val date = binding.editTextTextDate.text.toString() // OK
        val time = binding.editTextTextHour.text.toString() // OK

        val appointment = Appointment(
            id,
            horseName,
            address,
            phoneNumber,
            date,
            time,
            reason,
            Firebase.auth.currentUser!!.uid,
            horseId
        )
        Firebase.firestore.collection("appointments").document(appointment.id).set(appointment)
        return true
    }

    private fun checkIfNotBlankOrEmpty(field: String): Boolean {
        if(field.isNotBlank() && field.isNotEmpty()){
            return true
        }
        Toast.makeText(this.context, "Complete todos los campos", Toast.LENGTH_SHORT).show()
        return false
    }

}