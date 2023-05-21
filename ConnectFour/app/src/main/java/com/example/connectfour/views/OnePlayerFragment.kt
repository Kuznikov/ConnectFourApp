package com.example.connectfour.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.example.connectfour.R
import com.example.connectfour.databinding.FragmentOnePlayerBinding
import com.example.connectfour.viewmodels.GameViewModel


class OnePlayerFragment : Fragment() {
    private var _binding: FragmentOnePlayerBinding? = null
    private val mBinding get() = _binding!!
    private lateinit var gameViewModel: GameViewModel
    private lateinit var boardAdapter: BoardAdapter
    private val winnerTextView: TextView? get() = _binding?.statusText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnePlayerBinding.inflate(inflater, container, false)
        gameViewModel = ViewModelProvider(this).get(GameViewModel::class.java)

        boardAdapter = BoardAdapter(winnerTextView,requireContext(), gameViewModel)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


