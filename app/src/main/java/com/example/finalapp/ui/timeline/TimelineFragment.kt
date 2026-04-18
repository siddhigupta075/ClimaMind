package com.example.finalapp.ui.timeline

import android.os.Bundle
import android.view.*
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finalapp.databinding.FragmentTimelineBinding
import com.example.finalapp.viewmodel.WeatherViewModel

class TimelineFragment : Fragment() {

    private var _binding: FragmentTimelineBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: WeatherViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentTimelineBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel = ViewModelProvider(requireActivity())[WeatherViewModel::class.java]

        setupRecycler()
        observeCity()
        observeForecast()
        setupRefresh()
    }

    private fun setupRecycler() {
        binding.recyclerTimeline.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun observeCity() {
        viewModel.city.observe(viewLifecycleOwner) { city ->
            binding.loader.visibility = View.VISIBLE
            viewModel.fetchForecast(city)
        }
    }

    private fun observeForecast() {
        viewModel.forecast.observe(viewLifecycleOwner) { list ->

            binding.loader.visibility = View.GONE
            binding.swipeRefresh.isRefreshing = false

            if (list.isEmpty()) {
                binding.emptyText.visibility = View.VISIBLE
                binding.recyclerTimeline.visibility = View.GONE
                return@observe
            }

            binding.emptyText.visibility = View.GONE
            binding.recyclerTimeline.visibility = View.VISIBLE

            val userType = viewModel.userType.value ?: "student"

            binding.recyclerTimeline.adapter =
                TimelineAdapter(list, userType)
        }
    }

    private fun setupRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            val city = viewModel.city.value ?: "Lucknow"
            viewModel.fetchForecast(city)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}