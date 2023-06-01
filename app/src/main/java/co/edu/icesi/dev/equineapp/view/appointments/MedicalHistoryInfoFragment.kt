package co.edu.icesi.dev.equineapp.view.appointments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import co.edu.icesi.dev.equineapp.R
import co.edu.icesi.dev.equineapp.databinding.FragmentMedicalhistoryInfoBinding
import co.edu.icesi.dev.equineapp.model.Comment
import co.edu.icesi.dev.equineapp.model.Horse
import co.edu.icesi.dev.equineapp.model.MedicalHistory
import co.edu.icesi.dev.equineapp.view.home.HomeFragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.fragment_medicalhistory_info.*
import java.util.*

class MedicalHistoryInfoFragment (private val medicalHistory:MedicalHistory): Fragment() {

    private lateinit var binding: FragmentMedicalhistoryInfoBinding
    private lateinit var commentLayoutManager: LinearLayoutManager
    private lateinit var commentAdapter: CommentAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentMedicalhistoryInfoBinding.inflate(inflater, container, false)

        binding.backPetInfoButton.setOnClickListener{
            activity?.supportFragmentManager?.popBackStack()
        }
        binding.commentBttn.setOnClickListener{
            if(publish()){
                Toast.makeText(this.context, "Su observación se agrego con exito", Toast.LENGTH_SHORT).show()
                binding.textMultiLineComment.setText("")
            }
        }

        binding.homebutton.setOnClickListener{
            val homeFragment = HomeFragment()
            setFragment(homeFragment)
        }

        return binding.root
    }

    private fun setFragment(fragment: Fragment) = requireActivity().supportFragmentManager.beginTransaction().apply {
        replace(R.id.fl_wrapper, fragment)
        addToBackStack(null)
        commit()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //datos del historial
        super.onViewCreated(view, savedInstanceState)

        Firebase.firestore.collection("horses").document(medicalHistory.horseId).get().addOnSuccessListener {
            val horse = it.toObject(Horse::class.java)
            Firebase.storage.reference.child("horses")
                .child(horse!!.image).downloadUrl.addOnSuccessListener {
                Glide.with(binding.imageViewPet).load(it).into(binding.imageViewPet)

                    binding.textViewName.text = horse.horseName
                    binding.textViewAge.text = horse.horseAge

            }
        }
        binding.textViewOwner.text = medicalHistory.owner
        binding.textViewEmail.text = medicalHistory.email
        binding.textViewDescription.text = medicalHistory.description


        //comentarios

        this.commentLayoutManager = LinearLayoutManager(context)
        commentRecyclerView.layoutManager = commentLayoutManager
        commentRecyclerView.setHasFixedSize(true)
        commentAdapter = CommentAdapter(this)
        commentRecyclerView.adapter = commentAdapter
        loadCommentsFromFirebase()
    }

    private fun loadCommentsFromFirebase() {
        commentAdapter.clearList(commentAdapter.itemCount)
        Firebase.firestore.collection("comments").whereEqualTo("medicalhistoryId", medicalHistory.id).get().addOnCompleteListener { task ->
            for (doc in task.result!!) {
                val comment = doc.toObject(Comment::class.java)
                commentAdapter?.addComment(comment)
            }
        }
    }
    private fun publish(): Boolean {

        if(!checkIfNotBlankOrEmpty(binding.textMultiLineComment.text.toString()))
            return false

        val valueComment = binding.textMultiLineComment.text.toString()

        val comment = Comment(
            UUID.randomUUID().toString(),
            valueComment,
            Firebase.auth.currentUser!!.uid,
            medicalHistory.id
        )
        Firebase.firestore.collection("comments").document(comment.id).set(comment)

        loadCommentsFromFirebase()

        return true
    }

    private fun checkIfNotBlankOrEmpty(field: String): Boolean {
        if(field.isNotBlank() && field.isNotEmpty()){
            return true
        }
        Toast.makeText(this.context, "Debe escribir una observación", Toast.LENGTH_SHORT).show()
        return false
    }

}