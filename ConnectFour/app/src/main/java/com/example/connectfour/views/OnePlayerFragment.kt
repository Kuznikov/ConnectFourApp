package com.example.connectfour.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.example.connectfour.BoardAdapter
import com.example.connectfour.R
import com.example.connectfour.data.AppDatabase
import com.example.connectfour.databinding.FragmentOnePlayerBinding
import com.example.connectfour.models.User
import com.example.connectfour.viewmodels.AuthViewModel
import com.example.connectfour.viewmodels.GameViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class OnePlayerFragment : Fragment() {
    private var _binding: FragmentOnePlayerBinding? = null
    private val mBinding get() = _binding!!
    private lateinit var gameViewModel: GameViewModel
    private lateinit var boardAdapter: BoardAdapter
    private lateinit var authViewModel: AuthViewModel
    private val winnerTextView: TextView? get() = _binding?.statusText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnePlayerBinding.inflate(inflater, container, false)
        gameViewModel = ViewModelProvider(this).get(GameViewModel::class.java)
        val userDao = AppDatabase.getInstance(requireContext()).userDao()
        authViewModel = ViewModelProvider(requireActivity()).get(AuthViewModel::class.java)
        val username = authViewModel.currentUser?.username
            ?: "" // Имя пользователя, для которого нужно получить статистику

        GlobalScope.launch {
            val user = userDao.getUserByUsername(username)
            user?.let {
                // Обновление UI с использованием полученных данных пользователя
                withContext(Dispatchers.Main) {
                    updateCoinColor(user)
                }
            }
        }

        boardAdapter = BoardAdapter(winnerTextView, requireContext(), gameViewModel, username)
        mBinding.boardGridView.adapter = boardAdapter

        mBinding.resetButton.setOnClickListener {
            gameViewModel.resetGame()
            winnerTextView?.text = ""
        }

        gameViewModel.gameModel.observe(viewLifecycleOwner) { gameModel ->
            boardAdapter.notifyDataSetChanged()

            mBinding.resetButton.isEnabled = gameModel.gameOver
        }
        val navController = NavHostFragment.findNavController(this)
        mBinding.btnBack.setOnClickListener {
            navController.navigate(R.id.menuFragment)
        }

        return mBinding.root
    }

    private suspend fun updateCoinColor(user: User) {
        // Обновление текстовых полей с данными статистики
        withContext(Dispatchers.Main) {
            val coinColor = user?.coinColor ?: 0
            if (coinColor == 0) {
                mBinding.btnOnePlayerRed.visibility = View.VISIBLE
                mBinding.btnCpuBlue.visibility = View.VISIBLE
            } else if (coinColor == 1) {
                mBinding.btnOnePlayerBlue.visibility = View.VISIBLE
                mBinding.btnCpuRed.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


