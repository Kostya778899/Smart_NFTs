package com.kostyhub.smartnfts

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit

class PhoneAuthenticationController(
    private val context: Context,
    private val activity: MainActivity,
    private val onRegistrationComplete: () -> Unit = { },
    private val onRegistrationFailed: () -> Unit = { },
) {
    /*var currentStep = AuthenticationSteps.InputPhone
        private set*/
    private val auth = FirebaseAuth.getInstance()
    private var storedVerificationId: String? = null

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
            Log.d(TAG, "onVerificationCompleted:$credential")
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.
            Log.w(TAG, "onVerificationFailed", e)

            if (e is FirebaseAuthInvalidCredentialsException) {
                // Invalid request
            } else if (e is FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
            }

            // Show a message and update the UI
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            Log.d(TAG, "onCodeSent:$verificationId")

            // Save verification ID and resending token so we can use them later
            storedVerificationId = verificationId
            //val resendToken = token
        }
    }

    init {
        auth.useAppLanguage()
    }

    fun sendSmsCode(phone: String) {
        //val phone = regionNumber.text + phone.text.trim()

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phone)              // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS)  // Timeout and unit
            .setActivity(activity)              // Activity (for callback binding)
            .setCallbacks(callbacks)// OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun checkSmsCode(code: String) {
        if (storedVerificationId == null || code.isEmpty()) {
            onRegistrationFailed()
            return
        }

        val credential = PhoneAuthProvider.getCredential(storedVerificationId!!, code)
        signInWithPhoneAuthCredential(credential)
    }

    fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")

                    onRegistrationComplete()

                    val user = task.result?.user
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                    onRegistrationFailed()
                    // Update UI
                }
            }
    }
}
enum class AuthenticationSteps {
    InputPhone, SendSmsCode, InputSmsCode, CheckSmsCode, Complete
}

@Composable
fun PhoneAuthentication_01(

) {
    val context = LocalContext.current
    val activity = context as MainActivity

    var currentStep by remember { mutableStateOf(AuthenticationSteps.InputPhone) }
    val controller by remember {
        mutableStateOf(PhoneAuthenticationController(
            context = context,
            activity = activity,
            onRegistrationComplete = { currentStep = AuthenticationSteps.Complete },
            onRegistrationFailed = { currentStep = AuthenticationSteps.InputPhone },
        ))
    }

    //var regionNumber by remember { mutableStateOf(TextFieldValue("")) }
    var phone by remember { mutableStateOf(TextFieldValue("+79174631217")) }
    var smsCode by remember { mutableStateOf(TextFieldValue("")) }

    Column(
        modifier = Modifier.wrapContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        if (currentStep == AuthenticationSteps.Complete) {
            Text("Complete",
                modifier = Modifier.fillMaxWidth(),
                color = Color.Green,
                fontSize = 30.sp
            )
        }

        if (currentStep == AuthenticationSteps.InputPhone) {
            TextField_00(value = phone, onValueChange = { phone = it }, title = "Phone")
            Button_00(
                onClick = {
                    controller.sendSmsCode(phone.text)
                    currentStep = AuthenticationSteps.InputSmsCode
                },
                title = "Continue"
            )
        }

        if (currentStep != AuthenticationSteps.InputPhone) {
            Text("Phone: ${phone.text}")
        }

        /*if (currentStep == AuthenticationSteps.SendSmsCode) {
            Text("Load...")
            Button_00(
                onClick = { currentStep = AuthenticationSteps.InputSmsCode },
                title = "End"
            )
        }*/

        if (currentStep == AuthenticationSteps.InputSmsCode) {
            TextField_00(value = smsCode, onValueChange = { smsCode = it }, title = "Code")
            Button_00(
                onClick = {
                    currentStep = AuthenticationSteps.CheckSmsCode
                    controller.checkSmsCode(smsCode.text)
                },
                title = "Check code"
            )
            Button_00(
                onClick = {
                    controller.sendSmsCode(phone.text)
                    smsCode = TextFieldValue("")
                    currentStep = AuthenticationSteps.InputSmsCode
                },
                title = "Send code again"
            )
            Button_00(
                onClick = {
                    smsCode = TextFieldValue("")
                    currentStep = AuthenticationSteps.InputPhone
                },
                title = "Phone number entered incorrectly"
            )
        }

        /*if (currentStep == AuthenticationSteps.CheckSmsCode) {
            Text("Load...")
            Button_00(
                onClick = { currentStep = AuthenticationSteps.InputPhone },
                title = "Back"
            )
        }*/
    }
}