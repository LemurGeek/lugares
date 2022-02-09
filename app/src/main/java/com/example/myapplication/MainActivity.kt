package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.myapplication.databinding.ActivityMainBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth;
    private lateinit var binding: ActivityMainBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater);
        setContentView(binding.root);
        FirebaseApp.initializeApp(this);
        auth = Firebase.auth;

        // Login
        binding.loginBtn.setOnClickListener {
            makeLogin();
        }

        binding.registerBtn.setOnClickListener {
            makeRegister();
        }
    }

    private fun makeRegister() {
        val email = binding.emailEt.text.toString();
        val pass = binding.passEt.text.toString();

        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener(this){ task ->
                if (task.isSuccessful){
                    Log.d("Creando usuario", "Registrado");
                    val user = auth.currentUser;
                    updateU(user);
                } else {
                    Log.d("Creando usuario", "Fallo al registrar");
                    Toast.makeText(baseContext, "Fallo el registro", Toast.LENGTH_LONG).show();
                    updateU(null);
                }
            }
    }

    private fun updateU(user: FirebaseUser?) {
        if(user != null){
            val intent = Intent(this, Principal::class.java);
            startActivity(intent);
        }
    }

    public override fun onStart() {
        super.onStart()
        val user = auth.currentUser;
        updateU(user);
    }


    private fun makeLogin() {
        val email = binding.emailEt.text.toString();
        val pass = binding.passEt.text.toString();

        auth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener(this){ task ->
                if (task.isSuccessful){
                    Log.d("Autenticando", "Autenticado");
                    val user = auth.currentUser;
                    updateU(user);
                } else {
                    Log.d("Autenticando", "Fallo al autenticar");
                    Toast.makeText(baseContext, "Fallo al autenticar", Toast.LENGTH_LONG).show();
                    updateU(null);
                }
            }
    }
}