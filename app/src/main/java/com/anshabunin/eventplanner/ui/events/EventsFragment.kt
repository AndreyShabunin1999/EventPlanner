package com.anshabunin.eventplanner.ui.events

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.anshabunin.eventplanner.core.data.model.EventStatus
import com.anshabunin.eventplanner.core.database.entity.EventEntity
import com.anshabunin.eventplanner.databinding.FragmentEventsBinding
import com.anshabunin.eventplanner.di.Injectable
import com.anshabunin.eventplanner.domain.repository.EventRepository
import com.anshabunin.eventplanner.ui.events.adapter.EventAdapter
import com.anshabunin.eventplanner.ui.events.adapter.EventsListener
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class EventsFragment : Fragment(), Injectable {

    private lateinit var binding: FragmentEventsBinding

    @Inject
    lateinit var eventRepository: EventRepository

    private val viewModel: EventsViewModel by viewModels {
        EventsViewModel.factory(eventRepository,)
    }

    private val adapterUpcoming by lazy { EventAdapter(eventsListener) }
    private val adapterAttended by lazy { EventAdapter(eventsListener) }
    private val adapterMissed by lazy { EventAdapter(eventsListener) }

    private val eventsListener = object : EventsListener {
        override fun openEventDetail(idEvent: Int) {
            val action = EventsFragmentDirections.openEventFragment(idEvent)
            findNavController().navigate(action)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEventsBinding.inflate(inflater, container, false)

        binding.apply {
            lifecycleOwner = this@EventsFragment
            data = viewModel
            recyclerViewUpcoming.adapter = adapterUpcoming
            recyclerViewAttended.adapter = adapterAttended
            recyclerViewMissed.adapter = adapterMissed

            createButton.setOnClickListener {
                findNavController().navigate(EventsFragmentDirections.openCreateEventFragment())
            }
        }
        tabListener()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.events.onEach { events ->
            adapterUpcoming.updateEvents(viewModel.getUpcomingEvents())
            adapterAttended.updateEvents(viewModel.getAttendedEvents())
            adapterMissed.updateEvents(viewModel.getMissedEvents())
            viewModel.visibilityEmptyView.set(updateEmptyViewVisible(viewModel.selectedTab.value ?: 0))
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.selectedTab.observe(viewLifecycleOwner) { tabIndex ->
            binding.tabLayout.getTabAt(tabIndex)?.select()
        }
    }

    private fun tabListener() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewModel.updateSelectedTab(tab.position)
                viewModel.visibilityEmptyView.set(updateEmptyViewVisible(tab.position))
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    private fun updateEmptyViewVisible(tabPosition: Int): Boolean {
        return when(tabPosition){
            0 -> adapterUpcoming.itemCount == 0
            1 -> adapterAttended.itemCount == 0
            2 -> adapterMissed.itemCount == 0
            else -> {true}
        }
    }
}