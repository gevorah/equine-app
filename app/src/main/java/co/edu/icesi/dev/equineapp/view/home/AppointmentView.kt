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

    var nameTextView : TextView = itemView.findViewById(R.id.nameTextView)
    var addressTextView : TextView = itemView.findViewById(R.id.addressTextView)
    var ownerTextView : TextView = itemView.findViewById(R.id.ownerTextView)
    var timeTextView : TextView = itemView.findViewById(R.id.timeTextView)
    var reasonTextView : TextView = itemView.findViewById(R.id.reasonTextView)
}