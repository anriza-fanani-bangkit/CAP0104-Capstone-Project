package com.example.angkoot.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.angkoot.databinding.FragmentOrderingBinding

class ProfileFragment : Fragment() {
    private var _binding: FragmentOrderingBinding? = null
    private val binding get() = _binding

    private var _view: View? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOrderingBinding.inflate(inflater, container, false)
        _view = _binding?.root
        return _view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            //
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _view = null
    }
}