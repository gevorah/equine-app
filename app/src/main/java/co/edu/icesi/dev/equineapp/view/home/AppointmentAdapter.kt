package co.edu.icesi.dev.equineapp.view.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import co.edu.icesi.dev.equineapp.R
import co.edu.icesi.dev.equineapp.model.Appointment

class AppointmentAdapter(private val fragment: Fragment) : RecyclerView.Adapter<AppointmentView>(){

    private var appointments = ArrayList<Appointment>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentView {
        val layoutInflater = LayoutInflater.from(parent.context)
        val appointment = layoutInflater.inflate(R.layout.appointment, parent, false)
        return AppointmentView(appointment)
    }

    override fun onBindViewHolder(holder: AppointmentView, position: Int) {
        val appointment = appointments[position]
        appointment.also { holder.appointment = it }
        //Firebase.storage.reference.child("publications").child(publication.image).downloadUrl.addOnSuccessListener {
        //    Glide.with(holder.petImageView).load(it).into(holder.petImageView)
        //}
        holder.nameTextView.text = appointment.horseId
        holder.addressTextView.text = appointment.address
        //holder.ownerTextView.text = appointment.owner
        holder.timeTextView.text = appointment.time
        holder.reasonTextView.text = appointment.reason
        holder.itemView.setOnClickListener{
            //val infoFragment = infoFragment(appointment)
            //fragment.setFragment(infoFragment)
        }
    }

    override fun getItemCount(): Int {
        return appointments.size
    }

    fun addAppointment(appointment: Appointment) {
        appointments.add(appointment)
        notifyItemInserted(appointments.size-1)
    }

}