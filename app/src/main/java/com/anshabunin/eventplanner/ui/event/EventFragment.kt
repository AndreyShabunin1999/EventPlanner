package com.anshabunin.eventplanner.ui.event

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.anshabunin.eventplanner.R
import com.anshabunin.eventplanner.core.data.model.getEnumStatus
import com.anshabunin.eventplanner.core.domain.model.ResourceState
import com.anshabunin.eventplanner.databinding.FragmentEventBinding
import com.anshabunin.eventplanner.di.Injectable
import com.anshabunin.eventplanner.domain.repository.EventRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class EventFragment : Fragment(), Injectable {

    private lateinit var binding: FragmentEventBinding

    @Inject
    lateinit var eventRepository: EventRepository

    private val args: EventFragmentArgs by navArgs()

    private val viewModel: EventViewModel by viewModels {
        EventViewModel.factory(eventRepository, args.idEvent, requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEventBinding.inflate(inflater, container, false)

        binding.apply {
            lifecycleOwner = this@EventFragment
            data = viewModel

            backButton.setOnClickListener {
                findNavController().popBackStack()
            }

            deleteButton.setOnClickListener{
                viewModel.removeEvent(args.idEvent)
            }

            editButton.setOnClickListener {
                val action = EventFragmentDirections.openEditEventFragment(args.idEvent)
                findNavController().navigate(action)
            }

            spinnerVisitedEvent.setOnSpinnerItemSelectedListener<String>{ _,_,_,_ ->
                updateEvent()
            }
        }

        return binding.root
    }

    private fun updateEvent() {
        val status = viewModel.eventStatus.get()
        if (status.isNullOrEmpty()) {
            Toast.makeText(context, getString(R.string.error_status_empty), Toast.LENGTH_SHORT).show()
            return
        }
        status.getEnumStatus(requireContext())?.let { viewModel.updateEvent(args.idEvent, it) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressbar.visibility = isLoading
        }

        lifecycleScope.launch {
            viewModel.eventResult.collect { result ->
                result?.let {
                    val message = when(it) {
                        is ResourceState.SUCCESS -> getString(R.string.success_remove_event)
                        else -> getString(R.string.error_remove_event)
                    }
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    if(it == ResourceState.SUCCESS) {
                        findNavController().popBackStack()
                    }

                }
            }
        }

        lifecycleScope.launch {
            viewModel.updateEventResult.collect { result ->
                result?.let {
                    val message = when(it) {
                        is ResourceState.SUCCESS -> getString(R.string.success_update_event)
                        else -> getString(R.string.error_update_event)
                    }
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    viewModel.clearUpdateResult()
                }
            }
        }

        lifecycleScope.launch {
            viewModel.weatherData.collect { result ->
                result?.let {
                    Log.e("ERRROR", it.toString())
                }
            }
        }
    }
}