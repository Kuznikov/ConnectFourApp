package com.example.connectfour.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.example.connectfour.R
import com.example.connectfour.data.AppDatabase
import com.example.connectfour.data.UserDao
import com.example.connectfour.databinding.FragmentStatsBinding
import com.example.connectfour.models.User
import com.example.connectfour.viewmodels.AuthViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StatsFragment : Fragment() {
    private var _binding: FragmentStatsBinding? = null
    private val mBinding get() = _binding!!
    private lateinit var authViewModel: AuthViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStatsBinding.inflate(layoutInflater, container, false)

        val navController = NavHostFragment.findNavController(this)
        mBinding.btnBlueMenu.setOnClickListener {
            navController.navigate(R.id.menuFragment)
        }
        mBinding.btnBlueSettings.setOnClickListener {
            navController.navigate(R.id.settingsFragment)
        }

        return mBinding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userDao = AppDatabase.getInstance(requireContext()).userDao()
        authViewModel = ViewModelProvider(requireActivity()).get(AuthViewModel::class.java)
        val username = authViewModel.currentUser?.username ?: "" // Здесь укажите имя пользователя, для которого нужно получить статистику

        // Получение пользователя из базы данных
        GlobalScope.launch {
            val user = userDao.getUserByUsername(username)
            user?.let {
                // Обновление UI с использованием полученных данных пользователя
                withContext(Dispatchers.Main) {
                    updateStatsUI(user)
                }
            }
        }
    }

    private suspend fun updateStatsUI(user: User) {
        // Обновление текстовых полей с данными статистики
        withContext(Dispatchers.Main) {
            mBinding.txtWin.text = user.wins.toString()
            mBinding.txtLose.text = user.losses.toString()

            // Рассчет и отображение процента побед
            val totalGames = user.wins + user.losses
            val winPercentage = if (totalGames != 0) ((user.wins.toFloat() / totalGames.toFloat()) * 100).toInt() else 0
            mBinding.txtWinProcs.text = "%d%%".format(winPercentage)
        }
    }

}
