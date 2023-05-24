package com.example.connectfour.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.example.connectfour.DuoBoardAdapter
import com.example.connectfour.R
import com.example.connectfour.data.AppDatabase
import com.example.connectfour.databinding.FragmentTwoPlayerBinding
import com.example.connectfour.viewmodels.AuthViewModel
import com.example.connectfour.viewmodels.GameViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class TwoPlayerFragment : Fragment() {
    private var _binding: FragmentTwoPlayerBinding? = null
    private val mBinding get() = _binding!!

    private lateinit var gameViewModel: GameViewModel
    private lateinit var duoBoardAdapter: DuoBoardAdapter
    private lateinit var authViewModel: AuthViewModel
    private var playerOneName: String = ""
    private var playerTwoName: String = ""
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private val winnerTextView: TextView? get() = _binding?.statusText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTwoPlayerBinding.inflate(inflater, container, false)

        gameViewModel = ViewModelProvider(this).get(GameViewModel::class.java)

        authViewModel = ViewModelProvider(requireActivity()).get(AuthViewModel::class.java)
        val username = authViewModel.currentUser?.username ?: ""

        val userDao = AppDatabase.getInstance(requireContext()).userDao()
        coroutineScope.launch {
            val user = userDao.getUserByUsername(username)
            playerOneName = user?.playerOne ?: ""
            playerTwoName = user?.playerTwo ?: ""
            updatePlayerNames()
        }

        duoBoardAdapter = DuoBoardAdapter(
            winnerTextView,
            requireContext(),
            gameViewModel,
            mBinding.imgQueue,
            mBinding.imgQueue2
        )
        mBinding.boardGridView.adapter = duoBoardAdapter

        mBinding.resetButton.setOnClickListener {
            gameViewModel.resetGame()
            winnerTextView?.text = ""
        }

        gameViewModel.gameModel.observe(viewLifecycleOwner) { gameModel ->
            duoBoardAdapter.notifyDataSetChanged()

            mBinding.resetButton.isEnabled = gameModel.gameOver
        }

        val navController = NavHostFragment.findNavController(this)
        mBinding.btnBack.setOnClickListener {
            navController.navigate(R.id.menuFragment)
        }

        return mBinding.root
    }

    private fun updatePlayerNames() {
        mBinding.txtPlayerOne.text = "1: $playerOneName"
        mBinding.txtPlayerTwo.text = "2: $playerTwoName"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}