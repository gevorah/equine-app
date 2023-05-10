package co.edu.icesi.dev.equineapp.view.pets

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import co.edu.icesi.dev.equineapp.R
import co.edu.icesi.dev.equineapp.model.Publication
import com.bumptech.glide.Glide
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class PetsPublicationAdapter(private val petsFragment: PetsFragment) : RecyclerView.Adapter<PetsPublicationView>(){

    private var publicationList = ArrayList<Publication>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetsPublicationView {
        val layoutInflater = LayoutInflater.from(parent.context)
        val publication = layoutInflater.inflate(R.layout.publication, parent, false)
        return PetsPublicationView(publication)
    }

    override fun onBindViewHolder(holder: PetsPublicationView, position: Int) {
        val publication: Publication = publicationList[position]
        holder.publication = publication
        Firebase.storage.reference.child("publications").child(publication.image).downloadUrl.addOnSuccessListener {
            Glide.with(holder.petImageView).load(it).into(holder.petImageView)
        }
        holder.nameTextView.text = publication.name
        holder.breedTextView.text = publication.breed
        holder.ownerTextView.text = publication.owner
        holder.locationTextView.text = publication.location
        holder.itemView.setOnClickListener{
            val petInfoFragment = PetInfoFragment(publication)
            petsFragment.setFragment(petInfoFragment)
        }
    }

    override fun getItemCount(): Int {
        return publicationList.size
    }

    fun addPublication(publication: Publication) {
        publicationList.add(publication)
        notifyItemInserted(publicationList.size-1)
    }

    fun clearList(oldSize : Int) {
        publicationList.clear()
        notifyItemRangeRemoved(0, oldSize)
    }
}