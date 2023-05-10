package co.edu.icesi.dev.equineapp.view.pets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import co.edu.icesi.dev.equineapp.databinding.FragmentPetInfoBinding
import co.edu.icesi.dev.equineapp.model.Comment
import co.edu.icesi.dev.equineapp.model.Publication
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_pet_info.*
import kotlinx.android.synthetic.main.fragment_pets.*
import java.util.*

class PetInfoFragment(private val publication : Publication) : Fragment() {

    private lateinit var binding: FragmentPetInfoBinding
    private lateinit var commentLayoutManager: LinearLayoutManager
    private lateinit var commentAdapter: CommentAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
                                ): View {
        binding = FragmentPetInfoBinding.inflate(inflater, container, false)

        binding.backPetInfoButton.setOnClickListener{
            activity?.supportFragmentManager?.popBackStack()
        }
        binding.commentBttn.setOnClickListener{
            if(publish()){
                Toast.makeText(this.context, "Su comentario se publico con exito", Toast.LENGTH_SHORT).show()
                binding.CommentEditTextMultiLine.setText("")
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
       //datos de publicacion
        super.onViewCreated(view, savedInstanceState)
        Firebase.storage.reference.child("publications").child(publication.image).downloadUrl.addOnSuccessListener {
            Glide.with(binding.imageViewPet).load(it).into(binding.imageViewPet)
        }
        binding.textViewName.text = publication.name
        binding.textViewLocation.text = publication.location
        binding.textViewOwner.text = publication.owner
        binding.textViewSex.text = publication.sex
        binding.textViewAge.text = publication.age
        binding.textViewBreed.text = publication.breed
        binding.editTextMultiLineDetails.setText(publication.description)
        binding.textViewContactInfo.text = publication.contactInformation

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
        Firebase.firestore.collection("comments").whereEqualTo("publicationId", publication.id).get().addOnCompleteListener { task ->
            for (doc in task.result!!) {
                val comment = doc.toObject(Comment::class.java)
                commentAdapter?.addComment(comment)
            }
        }
    }
    private fun publish(): Boolean {

        if(!checkIfNotBlankOrEmpty(binding.CommentEditTextMultiLine.text.toString()))
            return false

        val valueComment = binding.CommentEditTextMultiLine.text.toString()

        val comment = Comment(
            UUID.randomUUID().toString(),
            valueComment,
            Firebase.auth.currentUser!!.uid,
            publication.id
        )
        Firebase.firestore.collection("comments").document(comment.id).set(comment)

        loadCommentsFromFirebase()

        return true
    }

    private fun checkIfNotBlankOrEmpty(field: String): Boolean {
        if(field.isNotBlank() && field.isNotEmpty()){
            return true
        }
        Toast.makeText(this.context, "Debe escribir un comentario", Toast.LENGTH_SHORT).show()
        return false
    }

}