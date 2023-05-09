package co.edu.icesi.dev.petscued.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import co.edu.icesi.dev.petscued.R
import co.edu.icesi.dev.petscued.model.Publication
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_pets.*
import kotlinx.android.synthetic.main.activity_publication.*
import java.util.*
import kotlin.collections.ArrayList

class PetsPublicationActivity : AppCompatActivity() {

    private lateinit var publicationLayoutManager: GridLayoutManager
    private lateinit var petsPublicationAdapter: PetsPublicationAdapter
    private lateinit var publicationList: ArrayList<Publication>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pets)

        this.publicationLayoutManager = GridLayoutManager(this, 2)
        petsPublicationRecyclerView.layoutManager = publicationLayoutManager
        petsPublicationRecyclerView.setHasFixedSize(true)

        petsPublicationAdapter = PetsPublicationAdapter()
        petsPublicationRecyclerView.adapter = petsPublicationAdapter

        addHardcodedElements()
        lostPetsButton.setOnClickListener{
            filterPublicationListByStatus(Publication.LOST)
        }

        adoptionPetsButton.setOnClickListener{
            filterPublicationListByStatus(Publication.ADOPTION)
        }

        filterPetsButton.setOnClickListener{
            val intent = Intent(this, FilterPublicationActivity::class.java)
            startActivity(intent)
        }
    }

    private fun filterPublicationListByStatus(status: String){
        val filteredPublicationList: List<Publication> = publicationList.filter {
                publication -> publication.status.equals(status, ignoreCase = true)
        }
        petsPublicationAdapter.setPublicationList(filteredPublicationList as ArrayList<Publication>)
    }

    private fun addHardcodedElements() {
        this.publicationList = ArrayList()
        publicationList.add(
            Publication(
            UUID.randomUUID(),
            "path",
            "Laila",
            "Mestiza",
            "Male",
            "José Castro",
            "dog",
            "Perdido",
            "Cali, Santa Mónica",
            "7 años",
            "cafe",
            "sin detalles adiciones.",
            "3152942393",
            "Sí")
        )
        publicationList.add(
            Publication(
            UUID.randomUUID(),
            "path",
            "Cat",
            "Mestiza",
            "Male",
            "José Castro",
            "dog",
            "Adopción",
            "Cali, Santa Mónica",
            "7 años",
            "cafe",
            "sin detalles adiciones.",
            "3152942393",
            "Sí")
        )
        publicationList.add(
            Publication(
            UUID.randomUUID(),
            "path",
            "Duck",
            "Mestiza",
            "Male",
            "José Castro",
            "dog",
            "Adopción",
            "Cali, Santa Mónica",
            "7 años",
            "cafe",
            "sin detalles adiciones.",
            "3152942393",
            "Sí")
        )
        petsPublicationAdapter.setPublicationList(publicationList)
    }
}