package co.edu.icesi.dev.equineapp.view.appointments

import android.app.Activity
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
import co.edu.icesi.dev.equineapp.databinding.FragmentMedicalhistoryFormBinding
import co.edu.icesi.dev.equineapp.model.Appointment
import co.edu.icesi.dev.equineapp.model.Horse
import co.edu.icesi.dev.equineapp.model.MedicalHistory
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.*

class MedicalHistoryFormFragment (): Fragment() {

    private lateinit var binding: FragmentMedicalhistoryFormBinding
    private lateinit var galleryLauncher: ActivityResultLauncher<Intent>
    private var uri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMedicalhistoryFormBinding.inflate(inflater, container, false)

        galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(), ::onGalleryResult)

        binding.imageButtonHorse.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            galleryLauncher.launch(intent)
        }
        binding.saveBttn.setOnClickListener {
            val idMedicalHistory = UUID.randomUUID().toString()
            if(publish(idMedicalHistory)){
                Firebase.firestore.collection("medicalHistories").document(idMedicalHistory).get().addOnSuccessListener {
                    val medicalHistory = it.toObject(MedicalHistory::class.java)
                    val medicalHistoryInfoFragment = MedicalHistoryInfoFragment(medicalHistory!!)
                    setFragment(medicalHistoryInfoFragment)
                }
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

    private fun onGalleryResult(result: ActivityResult) {
        if(result.resultCode == Activity.RESULT_OK) {
            uri = result.data?.data
            uri?.let {
                binding.imageButtonHorse.setImageURI(uri)
            }
        }
    }

    private fun publish(medicalHistoryId: String): Boolean {
        if(uri==null){
            Toast.makeText(this.context, "Debe subir una imagen del caballo", Toast.LENGTH_SHORT).show()
            return false
        }
        else if(!checkIfNotBlankOrEmpty(binding.editTextTextHorseName.text.toString()))
            return false
        else if(!checkIfNotBlankOrEmpty(binding.editTextTextOwnerName.text.toString()))
            return false
        else if(!checkIfNotBlankOrEmpty(binding.editTextemail.text.toString()))
            return false
        else if(!checkIfNotBlankOrEmpty(binding.editTextTextHorseAge.text.toString()))
            return false
        else if(!checkIfNotBlankOrEmpty(binding.editTextDescription.text.toString()))
            return false

        //caballo
        val horseId= UUID.randomUUID().toString()
        val image = UUID.randomUUID().toString() // OK
        val name = binding.editTextTextHorseName.text.toString()
        val horseAge = binding.editTextTextHorseAge.text.toString()

        //historia clinica
        val owner = binding.editTextTextOwnerName.text.toString()
        val email = binding.editTextemail.text.toString()
        val description = binding.editTextDescription.text.toString()


        val horse = Horse(
            horseId,
            image,
            name,
            horseAge,
            medicalHistoryId,
            Firebase.auth.currentUser!!.uid
        )
        Firebase.storage.reference.child("horses").child(image).putFile(uri!!)
        Firebase.firestore.collection("horses").document(horse.id).set(horse)
        return true

        val medicalHistory = MedicalHistory(
            medicalHistoryId,
            owner,
            email,
            description,
            horseId,
            Firebase.auth.currentUser!!.uid
        )
        Firebase.firestore.collection("medicalHistories").document(medicalHistory.id).set(medicalHistory)
        return true
    }

    private fun checkIfNotBlankOrEmpty(field: String): Boolean {
        if(field.isNotBlank() && field.isNotEmpty()){
            return true
        }
        Toast.makeText(this.context, "Complete todos los campos", Toast.LENGTH_SHORT).show()
        return false
    }

    private fun checkRadioButton(id: Int): Boolean {
        if(id!=-1){
            return true
        }
        Toast.makeText(this.context, "Escoja una opción", Toast.LENGTH_SHORT).show()
        return false
    }

}