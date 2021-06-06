package com.uvg.signon2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider


class MainActivity : AppCompatActivity() {

    private lateinit var auth:FirebaseAuth
    private lateinit var googlesSignInClient: GoogleSignInClient
    private val TAG: String = "Sign In"
    private val RC_SIGN_IN: Int = 120


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("AIzaSyDfklgGP9B6w4AKSP8c52IdG0PknBl8wEg")
            .requestEmail()
            .build()

        googlesSignInClient = GoogleSignIn.getClient(this,gso)

        var btnIniciar = findViewById<Button>(R.id.btnIniciarSesion)
        btnIniciar.setOnClickListener { signIn() }
    }

    private fun signIn(){
        val signInIntent = googlesSignInClient.signInIntent
        startActivityForResult(signInIntent,RC_SIGN_IN)

        Toast.makeText(this,"Sign in",Toast.LENGTH_SHORT).show()
        //val MapaIntent = Intent(this,Mapa::class.java)
        //startActivity(MapaIntent)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Toast.makeText(this," Result",Toast.LENGTH_SHORT).show()

        if (requestCode == RC_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            Toast.makeText(this,"request code",Toast.LENGTH_SHORT).show()
            try {
                Toast.makeText(this,"Success0",Toast.LENGTH_SHORT).show()
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG,"firebaseAutWithGoogle"+account.id)
                firebaseAuthWithGoogle(account.idToken!!)
                Toast.makeText(this,"Success1",Toast.LENGTH_SHORT).show()

            }
            catch (e: ApiException){
                Log.w(TAG,"Google sign in failed",e)
                Toast.makeText(this,"fail",Toast.LENGTH_SHORT).show()
                //val MapaIntent = Intent(this,Mapa::class.java)
                //startActivity(MapaIntent)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken:String){
        Toast.makeText(this,"Success00",Toast.LENGTH_SHORT).show()
        val credential = GoogleAuthProvider.getCredential(idToken,null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this){task ->
                if (task.isSuccessful){
                    Log.d(TAG,"signInWithCredentials:Success")
                    Toast.makeText(this,"Success2",Toast.LENGTH_SHORT).show()
                    val MapaIntent = Intent(this,Mapa::class.java)
                    startActivity(MapaIntent)
                    //finish()
                }
                else {
                    Log.w(TAG,"SignIInWithCredentials:Failure",task.exception)
                }
            }
    }
}



