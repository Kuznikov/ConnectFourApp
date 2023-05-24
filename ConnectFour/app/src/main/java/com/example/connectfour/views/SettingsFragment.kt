package com.example.connectfour.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.example.connectfour.R
import com.example.connectfour.data.AppDatabase
import com.example.connectfour.data.UserDao

import com.example.connectfour.databinding.FragmentSettingsBinding
import com.example.connectfour.models.User
import com.example.connectfour.viewmodels.AuthViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val mBinding get() = _binding!!

    private lateinit var appDatabase: AppDatabase
    private lateinit var userDao: UserDao
    private lateinit var authViewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(layoutInflater, container, false)

        appDatabase = AppDatabase.getInstance(requireContext())
        userDao = appDatabase.userDao()

        val navController = NavHostFragment.findNavController(this)
        mBinding.btnBlueMenu.setOnClickListener {
            navController.navigate(R.id.menuFragment)
        }
        mBinding.btnBlueStats.setOnClickListener {
            navController.navigate(R.id.statsFragment)
        }

        // Устанавливаем состояние переключателя для coinColor
        lifecycleScope.launch {
            val user = getCurrentUser()
            user?.let {
                mBinding.switchCoin.isChecked = it.coinColor == 1
            }
        }

        // Устанавливаем состояние переключателя для timer
        lifecycleScope.launch {
            val user = getCurrentUser()
            user?.let {
                mBinding.switchTime.isChecked = it.timer == 1
            }
        }

        // Устанавливаем состояние переключателя для mode
        lifecycleScope.launch {
            val user = getCurrentUser()
            user?.let {
                mBinding.switchMode.isChecked = it.mode == 1
            }
        }

        mBinding.switchCoin.setOnCheckedChangeListener { _, isChecked ->
            lifecycleScope.launch {
                val user = getCurrentUser() ?: return@launch
                user.coinColor = if (isChecked) 1 else 0
                updateUser(user)
            }
        }

        mBinding.switchTime.setOnCheckedChangeListener { _, isChecked ->
            lifecycleScope.launch {
                val user = getCurrentUser() ?: return@launch
                user.timer = if (isChecked) 1 else 0
                updateUser(user)
            }
        }

        mBinding.switchMode.setOnCheckedChangeListener { _, isChecked ->
            lifecycleScope.launch {
                val user = getCurrentUser() ?: return@launch
                user.mode = if (isChecked) 1 else 0
                updateUser(user)
            }
        }

        return mBinding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private suspend fun getCurrentUser(): User? {
        authViewModel = ViewModelProvider(requireActivity()).get(AuthViewModel::class.java)
        val username = authViewModel.currentUser?.username ?: ""
        return userDao.getUserByUsername(username)
    }

    private fun updateUser(user: User) {
        GlobalScope.launch {
            userDao.updateUser(user)
        }
    }
}