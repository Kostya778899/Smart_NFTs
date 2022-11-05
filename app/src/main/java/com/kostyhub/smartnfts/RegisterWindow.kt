package com.kostyhub.smartnfts

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

//@Preview(showBackground = true)
@Composable
fun RegisterWindow(
    onRegistered: (User) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        var name by remember { mutableStateOf(TextFieldValue()) }
        var email by remember { mutableStateOf(TextFieldValue()) }


        PhoneAuthentication_01()

        /*Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            TextField_00(
                value = name,
                onValueChange = { name = it },
                title = "Name"
            )
            TextField_00(
                value = email,
                onValueChange = { email = it },
                title = "Email"
            )
        }
        Spacer(modifier = Modifier.height(15.dp))

        Button_00(
            onClick = {
                if (name.text.isEmpty() || email.text.isEmpty()) return@Button_00

                val database = FirebaseDatabase.getInstance().getReference("User")
                val user = User(database.key!!, name.text, email.text)
                database.push().setValue(user)

                onRegistered(user)
            },
            title = "Continue"
        )*/
    }
}