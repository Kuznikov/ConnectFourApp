package com.example.connectfour.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.connectfour.databinding.FragmentOnePlayerBinding
import com.example.connectfour.models.BoardModel
import com.example.connectfour.viewmodels.BoardViewModel

class OnePlayerFragment : Fragment(), BoardModel.OnBoardChangeListener {
    private var _binding: FragmentOnePlayerBinding? = null
    private val mBinding get() = _binding!!
    private lateinit var boardAdapter: BoardAdapter
    private lateinit var viewModel: BoardViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnePlayerBinding.inflate(inflater, container, false)
        val gridView = mBinding.boardGridView

        val boardModel = BoardModel(requireContext(), this)
        viewModel = BoardViewModel(requireContext(), boardModel)

        boardAdapter = BoardAdapter(requireContext(), viewModel)
        gridView.adapter = boardAdapter
        gridView.numColumns = 7

        return mBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onBoardChanged() {
        boardAdapter.notifyDataSetChanged()
    }
}

