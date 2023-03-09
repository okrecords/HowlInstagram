package com.okre.howlinstagram.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.okre.howlinstagram.R
import com.okre.howlinstagram.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    lateinit var binding : ActivityLoginBinding
    val loginViewModel : LoginViewModel by viewModels() // lateinit var loginViewModel: LoginViewModel 과 같다.(androidx.activity를 build.gradle에 implementation 해줘야 사용할 수 있음)
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        //loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java] // lateinit을 사용했을 경우,,viewModel이 Activity와 생명주기를 함께 하기 위해서
        binding.viewModel = loginViewModel // dataBinding data variable viewModel 과 loginViewModel 연결
        binding.activity = this // dataBinding data variable activity 와 LoginActivity 연결
        binding.lifecycleOwner = this // binding이 Activity와 생명주기를 함께 하기 위해서
        auth = FirebaseAuth.getInstance()
        setObserve()
    }

    fun setObserve() {
        loginViewModel.showInputNumberActivity.observe(this) {
            if (it) {
                finish()
                startActivity(Intent(this, InputNumberActivity::class.java))
            }
        }
        loginViewModel.showFindIdActivity.observe(this) {
            if (it) {
                startActivity(Intent(this, FindIdActivity::class.java))
            }
        }
    }

    fun loginWithSignupEmail() {
        println("Email")
        auth.createUserWithEmailAndPassword(loginViewModel.id.value.toString(), loginViewModel.password.value.toString()) // 꼭 value로 받아야 함
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    loginViewModel.showInputNumberActivity.value = true
                }
            }
    }

    fun findId() {
        println("findId")
        loginViewModel.showFindIdActivity.value = true
    }
}