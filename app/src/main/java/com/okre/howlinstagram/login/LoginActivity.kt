package com.okre.howlinstagram.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.okre.howlinstagram.R
import com.okre.howlinstagram.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    lateinit var binding : ActivityLoginBinding
    val loginViewModel : LoginViewModel by viewModels() // lateinit var loginViewModel: LoginViewModel 과 같다.(androidx.activity를 build.gradle에 implementation 해줘야 사용할 수 있음)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        //loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java] // lateinit을 사용했을 경우,,viewModel이 Activity와 생명주기를 함께 하기 위해서
        binding.viewModel = loginViewModel // dataBinding data variable viewModel 과 loginViewModel 연결
        binding.activity = this // dataBinding data variable activity 와 LoginActivity 연결
        binding.lifecycleOwner = this // binding이 Activity와 생명주기를 함께 하기 위해서
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
    
    fun findId() {
        println("findId")
        loginViewModel.showFindIdActivity.value = true
    }

    // 구글 로그인 성공한 결과 값 받는 함수
    var googleLoginResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val data = result.data
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        val account = task.getResult(ApiException::class.java)
        loginViewModel.firebaseAuthWithGoogle(account.idToken) // idToken : 로그인한 사용자 정보를 암호화한 값
    }
}