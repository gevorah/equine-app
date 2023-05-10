package co.edu.icesi.dev.equineapp.view.profile

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import co.edu.icesi.dev.equineapp.R
import co.edu.icesi.dev.equineapp.model.Publication

class UserPublicationView(itemView:View) : RecyclerView.ViewHolder(itemView) {

    //State
    var publication : Publication?=null

    //Listeners
    var editListener : OnPublicationEdit?=null
    var deleteListener : OnPublicationDelete?=null

    var petImageView : ImageView = itemView.findViewById(R.id.petImageView)
    var nameTextView : TextView = itemView.findViewById(R.id.nameTextView)
    var breedTextView : TextView = itemView.findViewById(R.id.breedTextView)
    var ownerTextView : TextView = itemView.findViewById(R.id.ownerTextView)
    var locationTextView : TextView = itemView.findViewById(R.id.locationTextView)
    var editButton : Button = itemView.findViewById(R.id.editButton)
    var deleteButton : Button = itemView.findViewById(R.id.deleteButton)

    init {
        editButton.setOnClickListener{
            publication?.let{
                editListener?.onEdit(it)
            }
        }
        deleteButton.setOnClickListener {
            publication?.let{
                deleteListener?.onDelete(it)
            }
        }
    }

    interface OnPublicationEdit{
        fun onEdit(publication: Publication)
    }

    interface OnPublicationDelete{
        fun onDelete(publication: Publication)
    }
}