package com.anshabunin.eventplanner.ui.editevent

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.anshabunin.eventplanner.databinding.FragmentEditEventBinding
import com.anshabunin.eventplanner.di.Injectable
import com.anshabunin.eventplanner.domain.repository.EventRepository
import javax.inject.Inject


class EditEventFragment : Fragment(), Injectable {

    private lateinit var binding: FragmentEditEventBinding

    @Inject
    lateinit var eventRepository: EventRepository

    private val args: EditEventFragmentArgs by navArgs()

    private val viewModel: EditEventViewModel by viewModels {
        EditEventViewModel.factory(eventRepository, args.idEvent)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditEventBinding.inflate(inflater, container, false)

        return binding.root
    }

}