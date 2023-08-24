package pe.idat.flickrlistadatos.view

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import pe.idat.flickrlistadatos.MainActivity
import pe.idat.flickrlistadatos.R
import pe.idat.flickrlistadatos.databinding.ActivityLoginBinding
import java.lang.Exception

class LoginActivity : AppCompatActivity() {
    lateinit var binding:ActivityLoginBinding
    lateinit var firebaseAuth:FirebaseAuth
    lateinit var googleLauncher: ActivityResultLauncher<Intent>

    private fun authFirebaseWithGoogle(idToken: String?) {

        val authCredentiales = GoogleAuthProvider.getCredential( idToken!!,null)
        firebaseAuth.signInWithCredential(authCredentiales)
            .addOnCompleteListener(this) {
            task ->
                if(task.isSuccessful){

                    val user = firebaseAuth.currentUser

                    goToMainActivity()

                }else{

                    showToast("error al iniciar")
                }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)

        firebaseAuth = FirebaseAuth.getInstance()

        googleLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
            if(result.resultCode == RESULT_OK){
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)

                try {
                    val account = task.getResult(ApiException::class.java)
                    authFirebaseWithGoogle(account.idToken)

                }
                catch(e: Exception){
                    showToast("Verifique la Cuenta")

                }
            }
        }

        binding.buttonLogin.setOnClickListener {
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()
            if (email.isEmpty() || password.isEmpty()) {
                showToast("Debes completar los campos")
                return@setOnClickListener
            }
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        showToast("Bienvenido ${email}")
                        goToMainActivity()

                    } else {
                        showToast("Verifique la Cuenta")
                    }
                }

        }

        binding.buttonLoginWithGoogle.setOnClickListener {
            signInWithGoogle()
        }

        binding.btncrearcuenta.setOnClickListener {

            signInWithFirebase()

        }


        setContentView(binding.root)
    }

    private fun signInWithGoogle() {

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_client_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(this, gso)
        val intent = googleSignInClient.signInIntent
        googleLauncher.launch(intent)

    }

    private fun signInWithFirebase() {
        val email = binding.editTextEmail.text.toString()
        val password = binding.editTextPassword.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            showToast("Debes completar los campos")
            return
        }

        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    showToast("Usuario creado exitosamente")
                } else {
                    showToast("Error al crear usuario: ${task.exception?.message}")
                }
            }    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun goToMainActivity() {
        val email = binding.editTextEmail.text.toString()
        val prefs = getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString("email", email)
        editor.apply()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}