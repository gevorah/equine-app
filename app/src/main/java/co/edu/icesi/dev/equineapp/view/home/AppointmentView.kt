package co.edu.icesi.dev.equineapp.view.home

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import co.edu.icesi.dev.equineapp.R
import co.edu.icesi.dev.equineapp.model.Appointment
import co.edu.icesi.dev.equineapp.model.Publication

class AppointmentView(itemView:View) : RecyclerView.ViewHolder(itemView) {

    // State
    var appointment : Appointment?=null

    var picImageView : ImageView = itemView.findViewById(R.id.petImageView)
    var nameTextView : TextView = itemView.findViewById(R.id.nameTextView)
    var addressTextView : TextView = itemView.findViewById(R.id.breedTextView)
    var ownerTextView : TextView = itemView.findViewById(R.id.ownerTextView)
    var timeTextView : TextView = itemView.findViewById(R.id.locationTextView)
    var reasonTextView : TextView = itemView.findViewById(R.id.locationTextView)
}