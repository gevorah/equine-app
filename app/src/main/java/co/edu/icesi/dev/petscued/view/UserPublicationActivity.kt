package co.edu.icesi.dev.petscued.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import co.edu.icesi.dev.petscued.R
import co.edu.icesi.dev.petscued.model.Publication
import kotlinx.android.synthetic.main.activity_publication.*
import java.util.*
import kotlin.collections.ArrayList

class UserPublicationActivity : AppCompatActivity() {

    private lateinit var publicationLayoutManager: LinearLayoutManager
    private lateinit var userPublicationAdapter: UserPublicationAdapter
    private lateinit var publicationList: ArrayList<Publication>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_publication)

        publicationLayoutManager = LinearLayoutManager(this)
        userPublicationRecyclerView.layoutManager = publicationLayoutManager
        userPublicationRecyclerView.setHasFixedSize(true)

        userPublicationAdapter = UserPublicationAdapter()
        userPublicationRecyclerView.adapter = userPublicationAdapter

        lostPublicationButton.setOnClickListener{
            filterPublicationListByStatus(Publication.LOST)
        }

        adoptionPublicationButton.setOnClickListener{
            filterPublicationListByStatus(Publication.ADOPTION)
        }

        addFloatingActionButton.setOnClickListener {
            addHardcodedElements()
        }
    }

    private fun filterPublicationListByStatus(status: String){
        val filteredPublicationList: List<Publication> = publicationList.filter {
                publication -> publication.status.equals(status, ignoreCase = true)
        }
        userPublicationAdapter.setPublicationList(filteredPublicationList as ArrayList<Publication>)
    }

    private fun addHardcodedElements() {
        this.publicationList = ArrayList()
        publicationList.add(Publication(
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
            "Sí"))
        publicationList.add(Publication(
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
            "Sí"))
        publicationList.add(Publication(
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
            "Sí"))
        userPublicationAdapter.setPublicationList(publicationList)
    }
}