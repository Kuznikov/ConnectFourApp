package com.example.connectfour.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import com.example.connectfour.R
import com.example.connectfour.databinding.FragmentMenuBinding
import com.example.connectfour.databinding.FragmentStartBinding
import com.example.connectfour.screens.start.StartFragmentViewModel


class MenuFragment : Fragment() {
    private var _binding: FragmentMenuBinding? = null
    private val mBinding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMenuBinding.inflate(layoutInflater, container, false)
        val navController = NavHostFragment.findNavController(this)
        mBinding.btnOnePlayer.setOnClickListener {
            navController.navigate(R.id.onePlayerFragment)
        }
        return mBinding.root
    }
}