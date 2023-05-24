package com.example.connectfour.views


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.example.connectfour.R
import com.example.connectfour.data.AppDatabase
import com.example.connectfour.databinding.FragmentAuthBinding
import com.example.connectfour.models.User
import com.example.connectfour.viewmodels.AuthManager
import com.example.connectfour.viewmodels.AuthViewModel
import kotlinx.coroutines.launch

class AuthFragment : Fragment() {
    private var _binding: FragmentAuthBinding? = null
    private val mBinding get() = _binding!!
    private lateinit var authManager: AuthManager
    private lateinit var authViewModel: AuthViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userDao = AppDatabase.getInstance(requireContext()).userDao()
        authManager = AuthManager(userDao)
        authViewModel = ViewModelProvider(requireActivity()).get(AuthViewModel::class.java)

        mBinding.btnLogin.setOnClickListener {
            val username = mBinding.txtAuthLogin.text.toString()
            val password = mBinding.txtAuthPassword.text.toString()
            loginUser(username, password)
        }

        mBinding.btnRegister.setOnClickListener {
            val username = mBinding.txtAuthLogin.text.toString()
            val password = mBinding.txtAuthPassword.text.toString()
            registerUser(username, password)
        }
    }

    private fun loginUser(username: String, password: String) {
        if (username.isNotEmpty() && password.isNotEmpty()) {
            viewLifecycleOwner.lifecycleScope.launch {
                try {
                    val navController = NavHostFragment.findNavController(this@AuthFragment)
                    val user = authManager.login(username, password)
                    if (user != null) {
                        // Аутентификация успешна, выполните соответствующие действия
                        showToast("Вход выполнен")
                        navController.navigate(R.id.menuFragment)
                        authViewModel.currentUser = user
                    } else {
                        // Аутентификация не удалась, покажите сообщение об ошибке
                        showToast("Ошибка аутентификации")
                    }
                } catch (e: Exception) {
                    // Обработка ошибок при выполнении аутентификации
                    showToast("Ошибка аутентификации: ${e.message}")
                }
            }
        } else {
            // Проверка на пустые значения для username и password
            showToast("Введите имя пользователя и пароль")
        }
    }

    private fun registerUser(username: String, password: String) {
        if (username.isNotEmpty() && password.isNotEmpty()) {
            // Проверка наличия существующего пользователя
            lifecycleScope.launch {
                val navController = NavHostFragment.findNavController(this@AuthFragment)
                val existingUser = authManager.getUserByUsername(username)
                if (existingUser != null) {
                    showToast("Пользователь с таким именем уже существует")
                    return@launch
                }

                val user = User(username = username, password = password, wins = 0, losses = 0, playerOne = "Player 1", playerTwo = "Player 2")
                val userId = authManager.register(user)
                if (userId != -1L) {
                    showToast("Регистрация успешна")
                    // Проверка нового пользователя в базе данных
                    val newUser = authManager.getUserByUsername(username)
                    if (newUser != null) {
                        showToast("Новый пользователь добавлен в базу данных: ${newUser.username}")
                        showToast("Вход выполнен")
                        navController.navigate(R.id.menuFragment)
                        authViewModel.currentUser = user
                    }
                } else {
                    showToast("Ошибка регистрации")
                }
            }
        } else {
            showToast("Введите имя пользователя и пароль")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
