package co.edu.icesi.dev.petscued.view

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import co.edu.icesi.dev.petscued.R
import co.edu.icesi.dev.petscued.model.Publication

class PetsPublicationView(itemView:View) : RecyclerView.ViewHolder(itemView) {

    //State
    var publication : Publication?=null

    var petImageView : ImageView = itemView.findViewById(R.id.petImageView)
    var nameTextView : TextView = itemView.findViewById(R.id.nameTextView)
    var breedTextView : TextView = itemView.findViewById(R.id.breedTextView)
    var ownerTextView : TextView = itemView.findViewById(R.id.ownerTextView)
    var locationTextView : TextView = itemView.findViewById(R.id.locationTextView)
}