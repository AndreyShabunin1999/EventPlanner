package com.anshabunin.eventplanner.ui.events

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.anshabunin.eventplanner.R
import com.anshabunin.eventplanner.databinding.FragmentEventsBinding
import com.anshabunin.eventplanner.ui.events.adapter.EventsAdapter
import com.anshabunin.eventplanner.ui.events.adapter.EventsListener
import com.anshabunin.eventplanner.ui.events.adapter.EventsViewModel

class EventsFragment : Fragment() {

    private lateinit var binding: FragmentEventsBinding

    //private val viewModel: EventsViewModel by viewModels {
    //    EventsViewModel.factory()
    //}

    private val eventsAdapter by lazy { EventsAdapter() }

    private val eventsListener = object : EventsListener {
        override fun openEventDetail(idEvent: Int) {
           // val action = HotelListFragmentDirections.openHotelDetailFragment(idEvent)
         //   findNavController().navigate(action)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEventsBinding.inflate(inflater, container, false)

        return binding.root
    }
}