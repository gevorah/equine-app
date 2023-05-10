package co.edu.icesi.dev.equineapp.view.pets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import co.edu.icesi.dev.equineapp.databinding.FragmentPublicationFilterBinding
import co.edu.icesi.dev.equineapp.model.Publication

class PublicationFilterFragment(private val petsFragment: PetsFragment) : Fragment() {

    private lateinit var binding: FragmentPublicationFilterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentPublicationFilterBinding.inflate(inflater, container, false)

        binding.backButton.setOnClickListener {
            clearFilters()
            requireActivity().supportFragmentManager.popBackStackImmediate()
        }
        binding.applyFilterButton.setOnClickListener {
            activity?.supportFragmentManager?.popBackStackImmediate()
        }
        binding.clearFilterButton.setOnClickListener {
            clearFilters()
        }
        petsFragment.setPublicationFilterFragment(this)
        return binding.root
    }

    private fun clearFilters(){
        binding.editTextType.text.clear()
        binding.editTextColor.text.clear()
        binding.editTextBreed.text.clear()
        binding.editTextLocation.text.clear()
        binding.editTextAge.text.clear()
        binding.radioGroupSex.clearCheck()
    }

    fun getFilters(status: String): ArrayList<(Publication) -> Boolean> {
        val type = binding.editTextType.text.toString()
        val color = binding.editTextColor.text.toString()
        val breed = binding.editTextBreed.text.toString()
        val location = binding.editTextLocation.text.toString()
        val age = binding.editTextAge.text.toString()
        val sex = sexRadioGroup(binding.radioGroupSex.checkedRadioButtonId)

        val filters = ArrayList<(Publication) -> Boolean>()

        if (status.isNotEmpty() && status.isNotBlank()) {
            filters.add{ status.contains(it.status) }
        }
        if (type.isNotEmpty() && type.isNotBlank()) {
            filters.add{ type.contains(it.type, true) }
        }
        if (color.isNotEmpty() && color.isNotBlank()) {
            filters.add{ color.contains(it.color, true) }
        }
        if (breed.isNotEmpty() && breed.isNotBlank()) {
            filters.add{ breed.contains(it.breed, true) }
        }
        if (location.isNotEmpty() && location.isNotBlank()) {
            filters.add{ location.contains(it.location, true) }
        }
        if (age.isNotEmpty() && age.isNotBlank()) {
            filters.add{ age.contains(it.age, true) }
        }
        if (sex.isNotEmpty() && sex.isNotBlank()) {
            filters.add{ sex.contains(it.sex, true) }
        }
        return filters
    }

    private fun sexRadioGroup(checkedId: Int): String {
        when(checkedId) {
            binding.radioButton10.id -> {
                return binding.radioButton10.text.toString()
            }
            binding.radioButton11.id -> {
                return binding.radioButton11.text.toString()
            }
        }
        return ""
    }
}