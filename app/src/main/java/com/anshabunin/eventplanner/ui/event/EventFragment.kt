package com.anshabunin.eventplanner.ui.event

import android.os.Bundle
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
import com.anshabunin.eventplanner.databinding.FragmentCreateEventBinding
import com.anshabunin.eventplanner.databinding.FragmentEvemtBinding
import com.anshabunin.eventplanner.di.Injectable
import com.anshabunin.eventplanner.domain.repository.EventRepository
import com.anshabunin.eventplanner.ui.events.EventsFragmentDirections
import com.anshabunin.eventplanner.ui.events.EventsViewModel
import com.anshabunin.hotelsapplication.core.domain.model.ResourceState
import kotlinx.coroutines.launch
import javax.inject.Inject

class EventFragment : Fragment(), Injectable {

    private lateinit var binding: FragmentEvemtBinding

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
        binding = FragmentEvemtBinding.inflate(inflater, container, false)

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
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressbar.visibility = isLoading
        }

        lifecycleScope.launch {
            viewModel.eventResult.collect { result ->
                result?.let {
                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                    if(it == requireContext().getString(R.string.success_remove_event)){
                        findNavController().popBackStack()
                    }
                }
            }
        }
    }
}