package com.kostyhub.smartnfts

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.ktx.Firebase
import java.util.concurrent.TimeUnit

@Composable
fun PhoneNumberAuthentication(

) {
    val context = LocalContext.current
    val activity = context as MainActivity
    val auth = FirebaseAuth.getInstance()
    var phoneNumber by remember { mutableStateOf("+7 917 463-12-17".trim()) }
    var verificationId by remember { mutableStateOf("") }
    var token: PhoneAuthProvider.ForceResendingToken? = null


    fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    val user = task.result?.user
                    //Toast.makeText(context, "Successful!", Toast.LENGTH_SHORT).show()
                } else {
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {

                    }

                }
            }
    }

    @Composable
    fun Verification(
        verificationId: String
        /*token: PhoneAuthProvider.ForceResendingToken*/
    ) {
        var verificationCode by remember { mutableStateOf("") }


        BasicTextField(
            value = verificationCode,
            onValueChange = {
                verificationCode = it
                if (verificationCode.length == 6) {
                    val credential = PhoneAuthProvider.getCredential(
                        verificationId, verificationCode
                    )
                    signInWithPhoneAuthCredential(credential)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray)
                .border(2.dp, Color.DarkGray)
                .padding(5.dp),
            textStyle = TextStyle(fontSize = 20.sp)
        ) {
            it()
            if (verificationCode.isEmpty()) {
                Text("Code from SMS", fontSize = 20.sp)
            }
        }
    }

    Column(
        modifier = Modifier
            .wrapContentSize()
            .padding(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Button(
            onClick = {
                if (phoneNumber.length < 10) {
                    Toast.makeText(context, "Phone number is incorrect", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                        signInWithPhoneAuthCredential(credential)
                    }
                    override fun onVerificationFailed(e: FirebaseException) {
                        Log.e(TAG, e.toString())
                        if (e is FirebaseAuthInvalidCredentialsException) {

                        } else if (e is FirebaseTooManyRequestsException) {

                        }
                    }
                    override fun onCodeSent(
                        _verificationId: String,
                        _token: PhoneAuthProvider.ForceResendingToken
                    ) {
                        token = _token
                        verificationId = _verificationId
                    }
                }

                val options = PhoneAuthOptions.newBuilder(auth)
                    .setPhoneNumber(phoneNumber)
                    .setTimeout(60L, TimeUnit.SECONDS)
                    .setActivity(activity)
                    .setCallbacks(callbacks)
                    .build()
                PhoneAuthProvider.verifyPhoneNumber(options)
            }
        ) {
            Text("Continue")
        }

        if (verificationId.isNotEmpty()) {
            Verification(verificationId/*, token!!*/)
        }
    }
}