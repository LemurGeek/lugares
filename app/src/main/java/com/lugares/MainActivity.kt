package com.lugares

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.lugares.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    companion object{
        private const val RC_SIGN_IN = 9001
    }

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Se establece el enlace con la vista xml mediante el objeto binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Se inicializa Firebase y se asigna el objeto para autenticación
        FirebaseApp.initializeApp(this)
        auth = Firebase.auth

        binding.btRegister.setOnClickListener { haceRegistro() }
        binding.btLogin.setOnClickListener { haceLogin() }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id_r))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso);
        binding.btGoogle.setOnClickListener { googleSignIn() }
    }

    private fun googleSignIn() {
        try {
            val signInIntent = googleSignInClient.signInIntent;
            startActivityForResult(signInIntent, RC_SIGN_IN)
        } catch (e: Exception) {
            Log.d("Error Google Login", e.toString())
        }

    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                   val user = auth.currentUser;
                   actualiza(user);
                } else {
                    actualiza(null);
                    Log.d("Error API2", "ERROR API LOGIN!!")
                }
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!);
            } catch (e: ApiException) {
                Log.d("Error API",e.toString())
            }
        }
    }

    private fun haceLogin() {
        val email = binding.etCorreo.text.toString()
        val clave = binding.etClave.text.toString()

        //Se usa la función para crear un usuario por medio de correo y contraseña
        auth.signInWithEmailAndPassword(email,clave)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    actualiza(user)
                } else {
                    Toast.makeText(baseContext,
                        getString(R.string.msg_fallo_login),
                        Toast.LENGTH_SHORT).show()
                    actualiza(null)
                }
            }
    }

    private fun haceRegistro() {
        val email = binding.etCorreo.text.toString()
        val clave = binding.etClave.text.toString()

        //Se usa la función para crear un usuario por medio de correo y contraseña
        auth.createUserWithEmailAndPassword(email,clave)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    actualiza(user)
                } else {
                    Toast.makeText(baseContext,
                        getString(R.string.msg_fallo_registro),
                        Toast.LENGTH_SHORT).show()
                    actualiza(null)
                }
            }
    }

    private fun actualiza(user: FirebaseUser?) {
        if (user!=null) {
            // paso a la pantalla principal
            val intent = Intent(this,Principal::class.java)
            startActivity(intent)
        }
    }

    public override fun onStart() {
        super.onStart()
        val user = auth.currentUser
        actualiza(user)
    }

}