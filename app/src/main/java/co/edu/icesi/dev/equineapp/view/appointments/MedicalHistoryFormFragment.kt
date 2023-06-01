package co.edu.icesi.dev.equineapp.view.appointments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import co.edu.icesi.dev.equineapp.R
import co.edu.icesi.dev.equineapp.databinding.FragmentMedicalhistoryFormBinding
import co.edu.icesi.dev.equineapp.model.MedicalHistory
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class MedicalHistoryFormFragment (): Fragment() {

    private lateinit var binding: FragmentMedicalhistoryFormBinding
    private lateinit var galleryLauncher: ActivityResultLauncher<Intent>
    private var uri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding.saveBttn.setOnClickListener {
            if(publish()){
                val appointmentInfoFragment = AppointmentInfoFragment()
                setFragment(appointmentInfoFragment)
            }
        }
        binding.backButtn.setOnClickListener{
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

        if(!checkIfNotBlankOrEmpty(binding.editTextTextHorseName.text.toString()))
            return false
        else if(!checkIfNotBlankOrEmpty(binding.editTextTextOwnerName.text.toString()))
            return false
        else if(!checkIfNotBlankOrEmpty(binding.editTextTextAddress.text.toString()))
            return false
        else if(!checkIfNotBlankOrEmpty(binding.editTextTextPhoneNumber.text.toString()))
            return false
        else if(!checkIfNotBlankOrEmpty(binding.editTextemail.text.toString()))
            return false
        else if(!checkIfNotBlankOrEmpty(binding.editTextTextHorseAge.text.toString()))
            return false
        else if(!checkIfNotBlankOrEmpty(binding.editTextDescription.text.toString()))
            return false

        val horseName = binding.editTextTextHorseName.text.toString()
        val email = binding.editTextemail.text.toString()
        val ownerName = binding.editTextTextOwnerName.text.toString() // OK
        val address = binding.editTextTextAddress.text.toString()
        val age = binding.editTextTextHorseAge.text.toString() // OK
        val reason = binding.editTextDescription.text.toString()
        val phoneNumber = binding.editTextTextPhoneNumber.text.toString()



        val medicalHistory = MedicalHistory(
            UUID.randomUUID().toString(),
            horseName,
            ownerName,
            address,
            email,
            age,
            phoneNumber,
            reason,
            Firebase.auth.currentUser!!.uid
        )
        Firebase.firestore.collection("medicalHistories").document(medicalHistory.id).set(medicalHistory)
        return true
    }

    private fun checkIfNotBlankOrEmpty(field: String): Boolean {
        if (field.isNotBlank() && field.isNotEmpty()) {
            return true
        }
        Toast.makeText(this.context, "Complete todos los campos", Toast.LENGTH_SHORT).show()
        return false
    }

}