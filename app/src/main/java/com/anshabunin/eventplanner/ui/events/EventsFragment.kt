package com.anshabunin.eventplanner.ui.events

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.anshabunin.eventplanner.core.data.model.EventStatus
import com.anshabunin.eventplanner.core.database.entity.EventEntity
import com.anshabunin.eventplanner.databinding.FragmentEventsBinding
import com.anshabunin.eventplanner.di.Injectable
import com.anshabunin.eventplanner.domain.repository.EventRepository
import com.anshabunin.eventplanner.ui.events.adapter.EventAdapter
import com.anshabunin.eventplanner.ui.events.adapter.EventsListener
import com.google.android.material.tabs.TabLayout
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
           // val action = HotelListFragmentDirections.openHotelDetailFragment(idEvent)
         //   findNavController().navigate(action)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEventsBinding.inflate(inflater, container, false)

        // Создаем тестовые данные
        val event3 = EventEntity(
            id = 1,
            title = "Событие 1",
            description = "Описание события 1",
            date = "01.07.2024",
            location = "Место проведения 1",
            city = "Город 1",
            imageUrl = "URL_изображения_1",
            status = EventStatus.MISSED
        )
        val event4 = EventEntity(
            id = 2,
            title = "Событие 2",
            description = "Описание события 2",
            date = "02.07.2024",
            location = "Место проведения 2",
            city = "Город 2",
            imageUrl = "URL_изображения_2",
            status = EventStatus.MISSED
        )

        val event5 = EventEntity(
            id = 1,
            title = "Событие 1",
            description = "Описание события 1",
            date = "01.07.2024",
            location = "Место проведения 1",
            city = "Город 1",
            imageUrl = "URL_изображения_1",
            status = EventStatus.MISSED
        )
        val event6 = EventEntity(
            id = 2,
            title = "Событие 2",
            description = "Описание события 2",
            date = "02.07.2024",
            location = "Место проведения 2",
            city = "Город 2",
            imageUrl = "URL_изображения_2",
            status = EventStatus.MISSED
        )

        val event7 = EventEntity(
            id = 1,
            title = "Событие 1",
            description = "Описание события 1",
            date = "01.07.2024",
            location = "Место проведения 1",
            city = "Город 1",
            imageUrl = "URL_изображения_1",
            status = EventStatus.MISSED
        )
        val event8 = EventEntity(
            id = 2,
            title = "Событие 2",
            description = "Описание события 2",
            date = "02.07.2024",
            location = "Место проведения 2",
            city = "Город 2",
            imageUrl = "URL_изображения_2",
            status = EventStatus.MISSED
        )

        val event1 = EventEntity(
            id = 1,
            title = "Событие 1",
            description = "Описание события 1",
            date = "01.07.2024",
            location = "Место проведения 1",
            city = "Город 1",
            imageUrl = "URL_изображения_1",
            status = EventStatus.MISSED
        )
        val event2 = EventEntity(
            id = 2,
            title = "Событие 2",
            description = "Описание события 2",
            date = "02.07.2024",
            location = "Место проведения 2",
            city = "Город 2",
            imageUrl = "URL_изображения_2",
            status = EventStatus.MISSED
        )

        // Добавляем тестовые данные в адаптер
        adapterUpcoming.updateEvents(listOf(event1, event2, event3, event6,event4, event5, event7, event8, event1, event2, event3, event6,event4, event5, event7, event8))
        adapterAttended.updateEvents(listOf(event4, event5, event7, event8))
        adapterMissed.updateEvents(listOf())

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
        /*
        viewModel.getTextFromService()
        viewModel.getUpcomingEvents().onEach { adapterUpcoming.updateEvents(it) }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.getAttendedEvents().onEach { adapterAttended.updateEvents(it) }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.getMissedEvents().onEach { adapterMissed.updateEvents(it) }
            .launchIn(viewLifecycleOwner.lifecycleScope)
            
         */
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