package com.okre.howlinstagram.login

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.okre.howlinstagram.R

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    var id : MutableLiveData<String> = MutableLiveData("")
    var password : MutableLiveData<String> = MutableLiveData("")

    var showInputNumberActivity : MutableLiveData<Boolean> = MutableLiveData(false)
    var showFindIdActivity : MutableLiveData<Boolean> = MutableLiveData(false)

    // ViewModel -> AndroidViewModel 로 변경하여 context 받아온 이유
    // ViewModel(Controller)가 View(Activity)를 통제하는 개념을 강조하기 위해 context를 사용
    var context = getApplication<Application>().applicationContext
    var googleSignInClient : GoogleSignInClient

    init {
        var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(context, gso)
    }
    fun loginWithSignupEmail() { // 화면을 통제하는 Controller 코드는 ViewModel이 가지고 있는 것이 좋음
        println("Email")
        auth.createUserWithEmailAndPassword(id.value.toString(), password.value.toString()) // 꼭 value로 받아야 함
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    showInputNumberActivity.value = true
                }
            }
    }

    fun loginGoogle(view: View) {
        var i = googleSignInClient.signInIntent
        (view.context as? LoginActivity)?.googleLoginResult?.launch(i)
    }

    fun firebaseAuthWithGoogle(idToken: String?) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                showInputNumberActivity.value = true
            }
        }
    }
}
