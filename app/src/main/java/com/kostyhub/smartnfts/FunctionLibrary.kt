package com.kostyhub.smartnfts

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.database.FirebaseDatabase

@Composable
fun TextField_00(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    title: String
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray)
            .border(2.dp, Color.DarkGray)
            .padding(5.dp),
        textStyle = TextStyle(fontSize = 20.sp)
    ) {
        it()
        if (value.text.isEmpty()) {
            Text(title, fontSize = 20.sp)
        }
    }
}

@Composable
fun Button_00(
    onClick: () -> Unit,
    title: String
) {
    Button(
        onClick = onClick,
        modifier = Modifier.width(250.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.LightGray,
            contentColor = Color.Black
        ),
        border = BorderStroke(2.dp, Color.DarkGray)
    ) {
        Text(title, fontSize = 25.sp)
    }
}