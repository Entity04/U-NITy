package com.entity.unity.ui.health

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.entity.unity.CreateFeed
import com.entity.unity.databinding.FragmentHealthBinding

class HealthFragment : Fragment() {

    private lateinit var healthViewModel: HealthViewModel
    private var _binding: FragmentHealthBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        healthViewModel =
            ViewModelProvider(this).get(HealthViewModel::class.java)
        _binding = FragmentHealthBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.addFeed.setOnClickListener {
            val intent= Intent(requireActivity(),CreateFeed::class.java)
            startActivity(intent)
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}