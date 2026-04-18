package com.example.finalapp.ui.settings

import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.finalapp.databinding.FragmentSettingsBinding
import com.example.finalapp.viewmodel.WeatherViewModel

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: WeatherViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel = ViewModelProvider(requireActivity())[WeatherViewModel::class.java]

        // Observe user type
        viewModel.userType.observe(viewLifecycleOwner) { type ->
            when (type) {
                "student" -> binding.student.isChecked = true
                "fitness" -> binding.fitness.isChecked = true
                else -> binding.general.isChecked = true
            }
        }

        // Save user type
        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            val type = when (checkedId) {
                binding.student.id -> "student"
                binding.fitness.id -> "fitness"
                else -> "general"
            }
            viewModel.saveUserType(type)
        }

        // Observe city
        viewModel.city.observe(viewLifecycleOwner) { city ->
            binding.currentCity.text = city.replaceFirstChar { it.uppercase() }
        }

        // Change city
        binding.changeCityBtn.setOnClickListener {

            val input = EditText(requireContext())
            input.hint = "Enter city"
            input.setText(binding.currentCity.text.toString())

            AlertDialog.Builder(requireContext())
                .setTitle("Change City")
                .setView(input)
                .setPositiveButton("Save") { _, _ ->

                    val newCity = input.text.toString().trim()

                    if (newCity.isEmpty()) {
                        Toast.makeText(requireContext(), "City cannot be empty", Toast.LENGTH_SHORT).show()
                        return@setPositiveButton
                    }

                    viewModel.saveCity(newCity)

                    Toast.makeText(
                        requireContext(),
                        "City updated to ${newCity.replaceFirstChar { it.uppercase() }}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}