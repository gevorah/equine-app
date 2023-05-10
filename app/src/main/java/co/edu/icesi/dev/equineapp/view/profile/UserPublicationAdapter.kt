package co.edu.icesi.dev.equineapp.view.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import co.edu.icesi.dev.equineapp.R
import co.edu.icesi.dev.equineapp.model.Publication
import co.edu.icesi.dev.equineapp.view.pets.PetInfoFragment
import com.bumptech.glide.Glide
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class UserPublicationAdapter(private val userPublicationsFragment: UserPublicationsFragment) : RecyclerView.Adapter<UserPublicationView>(),
    UserPublicationView.OnPublicationEdit,
    UserPublicationView.OnPublicationDelete {

    private var publicationList = ArrayList<Publication>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserPublicationView {
        val layoutInflater = LayoutInflater.from(parent.context)
        val publication = layoutInflater.inflate(R.layout.publication_buttons, parent, false)
        val userPublicationView = UserPublicationView(publication)
        userPublicationView.editListener = this
        userPublicationView.deleteListener = this
        return userPublicationView
    }

    override fun onBindViewHolder(holder: UserPublicationView, position: Int) {
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
            userPublicationsFragment.setFragment(petInfoFragment)
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

    override fun onEdit(publication: Publication) {
        if(publication.status==Publication.LOST){
            val publicationEditFormFragment = PublicationEditFormFragment(publication)
            userPublicationsFragment.setFragment(publicationEditFormFragment)
        }else {
            val publicationEditFormFragment = PublicationEditFormFragment(publication)
            userPublicationsFragment.setFragment(publicationEditFormFragment)
        }
    }

    override fun onDelete(publication: Publication) {
        Firebase.storage.reference.child("publications").child(publication.image).delete()
        Firebase.firestore.collection("publications").document(publication.id).delete()
        val index = publicationList.indexOf(publication)
        publicationList.removeAt(index)
        notifyItemRemoved(index)
    }
}