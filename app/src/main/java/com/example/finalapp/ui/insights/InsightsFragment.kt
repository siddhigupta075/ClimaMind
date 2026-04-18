package com.example.finalapp.ui.insights

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.finalapp.databinding.FragmentInsightsBinding
import com.example.finalapp.viewmodel.WeatherViewModel
import kotlin.math.abs

class InsightsFragment : Fragment() {

    private var _binding: FragmentInsightsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: WeatherViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentInsightsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel = ViewModelProvider(requireActivity())[WeatherViewModel::class.java]

        viewModel.forecast.observe(viewLifecycleOwner) { list ->

            if (list.isEmpty()) return@observe

            val avgTemp = list.map { it.main.temp }.average()

            val bestSlot = list.minByOrNull { abs(it.main.temp - 25) }
            val worstSlot = list.maxByOrNull { it.main.temp }

            binding.avgTemp.text = "Average Temperature: %.1f°C".format(avgTemp)

            binding.bestTime.text =
                "Best Time: ${bestSlot?.dt_txt?.substring(11, 16)}"

            binding.worstTime.text =
                "Avoid Around: ${worstSlot?.dt_txt?.substring(11, 16)}"

            binding.insightText.text = getInsight(avgTemp)

            binding.recommendation.text = getRecommendation(avgTemp)
        }
    }

    private fun getInsight(avg: Double): String {
        return when {
            avg in 20.0..28.0 -> "The day is well balanced. You can plan both indoor and outdoor activities."
            avg > 32 -> "Heat dominates the day. Energy levels may drop outdoors."
            avg < 15 -> "Cool conditions dominate. Best suited for indoor focus."
            else -> "Moderate variations. Flexible planning works best."
        }
    }

    private fun getRecommendation(avg: Double): String {
        return when {
            avg in 20.0..28.0 -> "Ideal day to be productive and go outside."
            avg > 32 -> "Focus on indoor work, hydration is important."
            avg < 15 -> "Stay warm. Good time for study or deep work."
            else -> "Maintain a balanced routine today."
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}