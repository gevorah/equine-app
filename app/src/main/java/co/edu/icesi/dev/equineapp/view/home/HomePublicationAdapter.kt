package co.edu.icesi.dev.equineapp.view.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import co.edu.icesi.dev.equineapp.R
import co.edu.icesi.dev.equineapp.model.Publication
import com.bumptech.glide.Glide
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class HomePublicationAdapter(private val homeFragment: HomeFragment) : RecyclerView.Adapter<HomePublicationView>(){

    private var publicationList = ArrayList<Publication>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomePublicationView {
        val layoutInflater = LayoutInflater.from(parent.context)
        val publication = layoutInflater.inflate(R.layout.publication, parent, false)
        return HomePublicationView(publication)
    }

    override fun onBindViewHolder(holder: HomePublicationView, position: Int) {
        val publication = publicationList[position]
        publication.also { holder.publication = it }
        Firebase.storage.reference.child("publications").child(publication.image).downloadUrl.addOnSuccessListener {
            Glide.with(holder.petImageView).load(it).into(holder.petImageView)
        }
        holder.nameTextView.text = publication.name
        holder.breedTextView.text = publication.breed
        holder.ownerTextView.text = publication.owner
        holder.locationTextView.text = publication.location
        holder.itemView.setOnClickListener{
            val petInfoFragment = PetInfoFragment(publication)
            homeFragment.setFragment(petInfoFragment)
        }
    }

    override fun getItemCount(): Int {
        return publicationList.size
    }

    fun addPublication(publication: Publication) {
        publicationList.add(publication)
        notifyItemInserted(publicationList.size-1)
    }
}