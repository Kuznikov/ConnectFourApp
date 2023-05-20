package com.example.connectfour.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import com.example.connectfour.R
import com.example.connectfour.databinding.FragmentStartBinding
import com.example.connectfour.viewmodels.StartFragmentViewModel


class StartFragment : Fragment() {
    private var _binding: FragmentStartBinding? = null
    private val mBinding get() = _binding!!
    private lateinit var mViewModel: StartFragmentViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentStartBinding.inflate(layoutInflater, container, false)
        val navController = NavHostFragment.findNavController(this)
        mBinding.btnReady.setOnClickListener {
            navController.navigate(R.id.authFragment)
        }
        return mBinding.root
    }
}