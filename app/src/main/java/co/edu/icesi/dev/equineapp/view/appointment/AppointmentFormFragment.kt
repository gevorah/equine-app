package co.edu.icesi.dev.equineapp.view.appointment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import co.edu.icesi.dev.equineapp.R
import co.edu.icesi.dev.equineapp.databinding.FragmentAppointmentFormBinding
import co.edu.icesi.dev.equineapp.model.Appointment
import co.edu.icesi.dev.equineapp.model.Horse
import co.edu.icesi.dev.equineapp.model.Publication
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.*

class AppointmentFormFragment (private val status : String) : Fragment(){

    private lateinit var binding: FragmentAppointmentFormBinding
    private lateinit var galleryLauncher: ActivityResultLauncher<Intent>
    private var uri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAppointmentFormBinding.inflate(inflater, container, false)

        galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(), ::onGalleryResult)

        binding.imageButtonHorse.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            galleryLauncher.launch(intent)
        }
        binding.appointmentBttn.setOnClickListener {
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

    private fun onGalleryResult(result: ActivityResult) {
        if(result.resultCode == RESULT_OK) {
            uri = result.data?.data
            uri?.let {
                binding.imageButtonHorse.setImageURI(uri)
            }
        }
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
        val image = UUID.randomUUID().toString() // OK
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
            image,
            horseName,
            horseAge,
            Firebase.auth.currentUser!!.uid
        )
        Firebase.storage.reference.child("horses").child(image).putFile(uri!!)
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