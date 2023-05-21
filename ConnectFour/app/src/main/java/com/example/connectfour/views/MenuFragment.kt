package com.example.connectfour.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import com.example.connectfour.R
import com.example.connectfour.databinding.FragmentMenuBinding


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
        mBinding.btnBlueSettings.setOnClickListener {
            navController.navigate(R.id.settingsFragment)
        }
        mBinding.btnBlueStats.setOnClickListener {
            navController.navigate(R.id.statsFragment)
        }
        return mBinding.root
    }
}