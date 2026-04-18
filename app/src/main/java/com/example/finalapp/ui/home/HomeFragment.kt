package com.example.finalapp.ui.home

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.finalapp.databinding.FragmentHomeBinding
import com.example.finalapp.utils.SuggestionEngine
import com.example.finalapp.utils.WeatherScorer
import com.example.finalapp.viewmodel.WeatherViewModel
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: WeatherViewModel
    private var currentTemp = 0.0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel = ViewModelProvider(requireActivity())[WeatherViewModel::class.java]

        binding.loader.visibility = View.VISIBLE

        // Observe city
        viewModel.city.observe(viewLifecycleOwner) { city ->
            binding.cityText.text = city.replaceFirstChar { it.uppercase() }
            viewModel.fetchWeather(city)
        }

        // Observe weather
        viewModel.weather.observe(viewLifecycleOwner) {

            binding.loader.visibility = View.GONE

            currentTemp = it.main.temp

            binding.tempText.text = "${it.main.temp}°C"
            binding.feelsLike.text = "Feels like ${it.main.feels_like}°C"

            val condition = it.weather.firstOrNull()?.description ?: "Clear"
            binding.weatherCondition.text = condition

            // Mood Score
            val score = WeatherScorer.calculateScore(currentTemp)
            val mood = WeatherScorer.getMood(score)
            binding.moodScore.text = "Mood: $score/100 • $mood"

            // Time
            val time = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
            binding.lastUpdated.text = "Updated at $time"
        }

        // Suggestion button
        binding.btnSuggest.setOnClickListener {

            val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
            val userType = viewModel.userType.value ?: "student"

            val suggestion = SuggestionEngine.getSuggestion(
                currentTemp,
                hour,
                userType
            )

            binding.suggestionText.text = suggestion
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}