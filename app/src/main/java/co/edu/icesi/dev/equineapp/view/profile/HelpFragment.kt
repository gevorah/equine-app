package co.edu.icesi.dev.equineapp.view.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import co.edu.icesi.dev.equineapp.databinding.FragmentHelpBinding
import co.edu.icesi.dev.equineapp.model.Questions
import kotlinx.android.synthetic.main.fragment_help.*

class HelpFragment : Fragment() {

    private lateinit var binding: FragmentHelpBinding
    val questioList = ArrayList<Questions>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHelpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        setRecyclerView()
    }

    private fun setRecyclerView() {
        val questionAdapter = QuestionsAdapter(questioList)
        recycler.adapter = questionAdapter
        recycler.setHasFixedSize(true)
    }

    private fun initData() {
        questioList.add(
            Questions(
                "¿Cómo creo una historia clínica?",
                "Desde la pantalla de inicio, en la parte superior hacer clic en el boton que dice \"Agendar historia clínica\"."
            )
        )
        questioList.add(
            Questions(
                "¿Cómo agendo una cita?",
                "Desde la pantalla de inicio, en la parte superior hacer clic en el boton que dice \"Crear historia clínica\"."
            )
        )
        questioList.add(
            Questions(
                "¿Cómo cancelo una cita?",
                "Desde la pantalla de perfil, en la parte superior hacer clic en la opción que dice \"Centro de citas\", busca la cita que quieres cancelar y dale al boton de cancelar."
            )
        )

    }
}