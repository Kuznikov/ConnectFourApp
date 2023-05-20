package com.example.connectfour.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.connectfour.databinding.FragmentOnePlayerBinding



class OnePlayerFragment : Fragment() {
    private var _binding: FragmentOnePlayerBinding? = null
    private val mBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnePlayerBinding.inflate(layoutInflater, container, false)
        val gridView = mBinding.boardGridView
        gridView.adapter = BoardAdapter(requireContext())
        gridView.numColumns = 7
        gridView.adapter = BoardAdapter(requireContext())

        return mBinding.root
    }
}


