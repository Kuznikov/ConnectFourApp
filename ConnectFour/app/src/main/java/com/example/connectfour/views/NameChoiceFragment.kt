package com.example.connectfour.views

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.example.connectfour.R
import com.example.connectfour.data.AppDatabase
import com.example.connectfour.data.UserDao
import com.example.connectfour.databinding.FragmentAuthBinding
import com.example.connectfour.databinding.FragmentNameChoiceBinding
import com.example.connectfour.models.User
import com.example.connectfour.viewmodels.AuthManager
import com.example.connectfour.viewmodels.AuthViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NameChoiceFragment : Fragment() {
    private var _binding: FragmentNameChoiceBinding? = null
    private val mBinding get() = _binding!!
    private lateinit var userDao: UserDao
    private lateinit var authViewModel: AuthViewModel
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        userDao = AppDatabase.getInstance(context).userDao()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNameChoiceBinding.inflate(inflater,container,false)

        val navController = NavHostFragment.findNavController(this)
        mBinding.btnBack.setOnClickListener {
            navController.navigate(R.id.menuFragment)
        }
        mBinding.btnStart.setOnClickListener {
            val playerOneName = mBinding.txtAuthLogin.text.toString()
            val playerTwoName = mBinding.txtAuthPassword.text.toString()

            if (playerOneName.isNotEmpty() && playerTwoName.isNotEmpty()) {
                updatePlayerNames(playerOneName, playerTwoName)
                navController.navigate(R.id.twoPlayerFragment)
            } else {
                updatePlayerNames("Игрок 1", "Игрок 2")
                navController.navigate(R.id.twoPlayerFragment)
            }
        }

        return mBinding.root
    }

    private fun updatePlayerNames(playerOneName: String, playerTwoName: String) {
        authViewModel = ViewModelProvider(requireActivity()).get(AuthViewModel::class.java)
        val username = authViewModel.currentUser?.username ?: ""

        coroutineScope.launch {
            val user = userDao.getUserByUsername(username)

            if (user != null) {
                user.playerOne = playerOneName
                user.playerTwo = playerTwoName
                userDao.insertUser(user)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}