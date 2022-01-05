package com.entity.unity.ui.exercise

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.entity.unity.databinding.FragmentExerciseBinding

class ExerciseFragment : Fragment() {

    private lateinit var exerciseViewModel: ExerciseViewModel
    private var _binding: FragmentExerciseBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        exerciseViewModel =
            ViewModelProvider(this).get(ExerciseViewModel::class.java)

        _binding = FragmentExerciseBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textExercise
        exerciseViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}