package co.edu.icesi.dev.equineapp.view.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import co.edu.icesi.dev.equineapp.R
import co.edu.icesi.dev.equineapp.databinding.FragmentHomeBinding
import co.edu.icesi.dev.equineapp.model.Appointment
import co.edu.icesi.dev.equineapp.view.appointments.AppointmentFormFragment
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var publicationLayoutManager: GridLayoutManager
    private var appointmentAdapter: AppointmentAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.appointmentImageButton.setOnClickListener {
            val publicationFormFragment = AppointmentFormFragment()
            setFragment(publicationFormFragment)
        }

        binding.historyImageButton.setOnClickListener {
            //val publicationFormFragment = AppointmentFormFragment()
            //setFragment(publicationFormFragment)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.publicationLayoutManager = GridLayoutManager(context, 2)
        homePublicationRecyclerView.layoutManager = publicationLayoutManager
        homePublicationRecyclerView.setHasFixedSize(true)
        appointmentAdapter = AppointmentAdapter(this)
        homePublicationRecyclerView.adapter = appointmentAdapter
        loadPublicationsFromFirebase()
    }

    private fun loadPublicationsFromFirebase() {
        Firebase.firestore.collection("appointments").get().addOnCompleteListener { task ->
            for (doc in task.result!!) {
                val appointment = doc.toObject(Appointment::class.java)
                appointmentAdapter?.addPublication(appointment)
            }
        }
    }

    private fun setFragment(fragment: Fragment) =
        requireActivity().supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_wrapper, fragment)
            addToBackStack(null)
            commit()
        }
}