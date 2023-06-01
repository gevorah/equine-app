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

class AppointmentFormFragment () : Fragment(){

    private lateinit var binding: FragmentAppointmentFormBinding
    private lateinit var galleryLauncher: ActivityResultLauncher<Intent>
    private var uri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAppointmentFormBinding.inflate(inflater, container, false)

        val items = listOf("Option 1", "Option 2", "Option 3", "Option 4")

        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, items)
        (binding.menu.editText as? AutoCompleteTextView)?.setAdapter(adapter)






        binding.saveBttn.setOnClickListener {
            if(publish()){
                val appointmentInfoFragment = AppointmentInfoFragment()
                setFragment(appointmentInfoFragment)
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



    private fun publish(): Boolean {
        if(uri==null){
            Toast.makeText(this.context, "Debe subir una imagen del caballo", Toast.LENGTH_SHORT).show()
            return false
        }
        else if(!checkIfNotBlankOrEmpty(binding.editTextTextHorseName.text.toString()))
            return false
        else if(!checkIfNotBlankOrEmpty(binding.editTextHorseAge.text.toString()))
            return false
        else if(!checkIfNotBlankOrEmpty(binding.editTextTextOwnerName.text.toString()))
            return false
        else if(!checkIfNotBlankOrEmpty(binding.editTextTextAddress.text.toString()))
            return false
        else if(!checkIfNotBlankOrEmpty(binding.editTextTextDate.text.toString()))
            return false
        else if(!checkIfNotBlankOrEmpty(binding.editTextTextHour.text.toString()))
            return false
        else if(!checkIfNotBlankOrEmpty(binding.editTextTextMotive.text.toString()))
            return false
        else if(!checkIfNotBlankOrEmpty(binding.editTextTextPhoneNumber.text.toString()))
            return false

        val horseName = binding.editTextTextHorseName.text.toString()
        val horseAge = binding.editTextHorseAge.text.toString()
        val ownerName = binding.editTextTextOwnerName.text.toString() // OK
        val address = binding.editTextTextAddress.text.toString()
        val date = binding.editTextTextDate.text.toString() // OK
        val hour = binding.editTextTextHour.text.toString() // OK
        val motive = binding.editTextTextMotive.text.toString()
        val phoneNumber = binding.editTextTextPhoneNumber.text.toString()

        val horseid=UUID.randomUUID().toString()

        val horse = Horse(
            horseid,
            horseName,
            horseAge,
            Firebase.auth.currentUser!!.uid
        )

        Firebase.firestore.collection("horses").document(horse.id).set(horse)

        val appointment = Appointment(
            UUID.randomUUID().toString(),
            ownerName,
            address,
            date,
            hour,
            motive,
            Firebase.auth.currentUser!!.uid,
            horseid
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