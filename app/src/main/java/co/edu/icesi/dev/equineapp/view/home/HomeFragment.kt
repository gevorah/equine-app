package co.edu.icesi.dev.equineapp.view.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import co.edu.icesi.dev.equineapp.R
import co.edu.icesi.dev.equineapp.databinding.FragmentHomeBinding
import co.edu.icesi.dev.equineapp.model.Publication
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var publicationLayoutManager: GridLayoutManager
    private var homePublicationAdapter: HomePublicationAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.lostImageButton.setOnClickListener {
            val publicationFormFragment = PublicationFormFragment(Publication.LOST)
            setFragment(publicationFormFragment)
        }
        binding.adoptionImageButton.setOnClickListener {
            val publicationFormFragment = PublicationFormFragment(Publication.ADOPTION)
            setFragment(publicationFormFragment)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.publicationLayoutManager = GridLayoutManager(context, 2)
        homePublicationRecyclerView.layoutManager = publicationLayoutManager
        homePublicationRecyclerView.setHasFixedSize(true)
        homePublicationAdapter = HomePublicationAdapter(this)
        homePublicationRecyclerView.adapter = homePublicationAdapter
        loadPublicationsFromFirebase()
    }

    private fun loadPublicationsFromFirebase() {
        Firebase.firestore.collection("publications").get().addOnCompleteListener { task ->
            for (doc in task.result!!) {
                val publication = doc.toObject(Publication::class.java)
                homePublicationAdapter?.addPublication(publication)
            }
        }
    }

    fun setFragment(fragment: Fragment) =
        requireActivity().supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_wrapper, fragment)
            addToBackStack(null)
            commit()
        }
}