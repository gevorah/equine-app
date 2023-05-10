package co.edu.icesi.dev.equineapp.view.profile

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import co.edu.icesi.dev.equineapp.R
import co.edu.icesi.dev.equineapp.databinding.FragmentUserPublicationsBinding
import co.edu.icesi.dev.equineapp.model.Publication
import co.edu.icesi.dev.equineapp.view.home.PublicationFormFragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_user_publications.*
import java.util.*

class UserPublicationsFragment : Fragment() {

    private lateinit var binding: FragmentUserPublicationsBinding
    private lateinit var publicationLayoutManager: LinearLayoutManager
    private lateinit var userPublicationAdapter: UserPublicationAdapter
    private lateinit var userPublicationList: ArrayList<Publication>
    private var isLostButtonGreen: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserPublicationsBinding.inflate(inflater, container, false)
        binding.lostPublicationButton.setOnClickListener {
            fetchUserPublicationsByStatus(Publication.LOST)
            binding.lostPublicationButton.setBackgroundColor(Color.GREEN)
            binding.adoptionPublicationButton.setBackgroundColor(Color.parseColor("#FFCF9E70"))
            isLostButtonGreen = true
        }
        binding.adoptionPublicationButton.setOnClickListener {
            fetchUserPublicationsByStatus(Publication.ADOPTION)
            binding.adoptionPublicationButton.setBackgroundColor(Color.GREEN)
            binding.lostPublicationButton.setBackgroundColor(Color.parseColor("#FFCF9E70"))
            isLostButtonGreen = false
        }
        binding.addFloatingActionButton.setOnClickListener {
            if(isLostButtonGreen){
                val publicationFormFragment = PublicationFormFragment(Publication.LOST)
                setFragment(publicationFormFragment)
            }else {
                val publicationFormFragment = PublicationFormFragment(Publication.ADOPTION)
                setFragment(publicationFormFragment)
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        publicationLayoutManager = LinearLayoutManager(context)
        userPublicationRecyclerView.layoutManager = publicationLayoutManager
        userPublicationRecyclerView.setHasFixedSize(true)
        userPublicationAdapter = UserPublicationAdapter(this)
        userPublicationRecyclerView.adapter = userPublicationAdapter
        userPublicationList = ArrayList()
        binding.lostPublicationButton.performClick()
    }

    private fun fetchUserPublicationsByStatus(status: String){
        userPublicationAdapter.clearList(userPublicationAdapter.itemCount)
        Firebase.firestore.collection("publications")
            .whereEqualTo("userId", Firebase.auth.currentUser!!.uid).get()
            .addOnCompleteListener { task ->
            for(doc in task.result!!){
                val publication = doc.toObject(Publication::class.java)
                if(publication.status==status) {
                    userPublicationAdapter.addPublication(publication)
                }
            }
        }
    }

    fun setFragment(fragment: Fragment) = requireActivity().supportFragmentManager.beginTransaction().apply {
        replace(R.id.fl_wrapper, fragment)
        addToBackStack(null)
        commit()
    }
}