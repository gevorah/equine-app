
package co.edu.icesi.dev.equineapp.view.pets

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import co.edu.icesi.dev.equineapp.R
import co.edu.icesi.dev.equineapp.databinding.FragmentPetsBinding
import co.edu.icesi.dev.equineapp.model.Publication
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_pets.*

class PetsFragment : Fragment() {

    private lateinit var binding: FragmentPetsBinding
    private lateinit var publicationLayoutManager: GridLayoutManager
    private lateinit var petsPublicationAdapter: PetsPublicationAdapter
    private var publicationFilterFragment: PublicationFilterFragment? = null
    private var isLostButtonGreen: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentPetsBinding.inflate(inflater, container, false)
        binding.adoptionPetsButton.setOnClickListener {
            fetchPublicationsByStatus(Publication.ADOPTION)
            binding.adoptionPetsButton.setBackgroundColor(Color.GREEN)
            binding.lostPetsButton.setBackgroundColor(Color.parseColor("#FFCF9E70"))
            isLostButtonGreen = false
        }
        binding.lostPetsButton.setOnClickListener {
            fetchPublicationsByStatus(Publication.LOST)
            binding.lostPetsButton.setBackgroundColor(Color.GREEN)
            binding.adoptionPetsButton.setBackgroundColor(Color.parseColor("#FFCF9E70"))
            isLostButtonGreen = true
        }
        binding.filterPetsButton.setOnClickListener {
            if(publicationFilterFragment == null){
                publicationFilterFragment = PublicationFilterFragment(this)
                setFragment(publicationFilterFragment!!)
            }else{
                setFragment(publicationFilterFragment!!)
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        GridLayoutManager(context, 2).also { this.publicationLayoutManager = it }
        petsPublicationRecyclerView.layoutManager = publicationLayoutManager
        petsPublicationRecyclerView.setHasFixedSize(true)
        this.petsPublicationAdapter = PetsPublicationAdapter(this)
        petsPublicationRecyclerView.adapter = this.petsPublicationAdapter
        if(publicationFilterFragment==null){
            binding.lostPetsButton.performClick()
        }else{
            if(isLostButtonGreen){
                binding.lostPetsButton.setBackgroundColor(Color.GREEN)
                binding.adoptionPetsButton.setBackgroundColor(Color.parseColor("#FFCF9E70"))
                applyFilters(publicationFilterFragment!!.getFilters(Publication.LOST), Publication.LOST)
            }else{
                binding.adoptionPetsButton.setBackgroundColor(Color.GREEN)
                binding.lostPetsButton.setBackgroundColor(Color.parseColor("#FFCF9E70"))
                applyFilters(publicationFilterFragment!!.getFilters(Publication.ADOPTION), Publication.ADOPTION)
            }
        }
    }

    fun setPublicationFilterFragment(publicationFilterFragment : PublicationFilterFragment){
        this.publicationFilterFragment = publicationFilterFragment
    }

    fun setFragment(fragment: Fragment) = requireActivity().supportFragmentManager.beginTransaction().apply {
        replace(R.id.fl_wrapper, fragment)
        addToBackStack(null)
        commit()
    }

    private fun fetchPublicationsByStatus(status: String){
        petsPublicationAdapter.clearList(petsPublicationAdapter.itemCount)
        Firebase.firestore.collection("publications").get().addOnCompleteListener{ task->
            for(doc in task.result!!){
                val publication = doc.toObject(Publication::class.java)
                if(publication.status==status) {
                    petsPublicationAdapter.addPublication(publication)
                }
            }
        }
    }

    private fun applyFilters(filters: ArrayList<(Publication) -> Boolean>, status: String) {
        petsPublicationAdapter.clearList(petsPublicationAdapter.itemCount)
        var publicationList = ArrayList<Publication>()
        Firebase.firestore.collection("publications").get().addOnCompleteListener{ task->
            for(doc in task.result!!){
                val publication = doc.toObject(Publication::class.java)
                if(publication.status==status) {
                    publicationList.add(publication)
                }
            }
            publicationList = publicationList.filter { candidate -> filters.all { it(candidate) } } as ArrayList<Publication>
            for (publication in publicationList){
                petsPublicationAdapter.addPublication(publication)
            }
        }
    }
}