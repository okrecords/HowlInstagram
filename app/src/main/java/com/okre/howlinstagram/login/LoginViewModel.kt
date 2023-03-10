package com.okre.howlinstagram.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel : ViewModel() {

    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    var id : MutableLiveData<String> = MutableLiveData("")
    var password : MutableLiveData<String> = MutableLiveData("")

    var showInputNumberActivity : MutableLiveData<Boolean> = MutableLiveData(false)
    var showFindIdActivity : MutableLiveData<Boolean> = MutableLiveData(false)

    fun loginWithSignupEmail() { // 화면을 통제하는 Controller 코드는 ViewModel이 가지고 있는 것이 좋음
        println("Email")
        auth.createUserWithEmailAndPassword(id.value.toString(), password.value.toString()) // 꼭 value로 받아야 함
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    showInputNumberActivity.value = true
                }
            }
    }
}
