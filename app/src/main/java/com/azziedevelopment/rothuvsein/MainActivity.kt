package com.azziedevelopment.rothuvsein

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.*
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import com.azziedevelopment.rothuvsein.ui.theme.RothuvseinTheme
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContent {
            RothuvseinTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PromptUser(this)
                }
            }
        }
    }
}


//function to prompt user for string
@Composable
fun PromptUser(context: Context) {
    var textState by remember {
        mutableStateOf(TextFieldValue())
    }
    var numberState by remember {
        mutableStateOf(0L)
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Enter a series of words of spelled out numbers.\n" +
                    "This app uses the rules of ROTHUVSEIN to convert to digits.",
            fontSize = 24.sp
        )
        Spacer(modifier = Modifier.height(20.dp))
        TextField(
            value = textState, onValueChange = {
                textState = it
                numberState = Calculate(value = it.text)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            textStyle = TextStyle(
                color = Color.White, // Set the text color to white
                fontSize = 24.sp
            )
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = {
            val phoneNumber: Long = numberState

            // Create an intent to dial the phone number
            val dialIntent = Intent(Intent.ACTION_DIAL)
            dialIntent.data = Uri.parse("tel:$phoneNumber")

            // Start the dialer activity
            context.startActivity(dialIntent)
                         },
            enabled = numberState != 0L
        ) {
            if (numberState == 0L) {
                Text("Enter a number to call")
            } else {
                Text("Call " + numberState.toString().replaceFirst("(\\d{3})(\\d{3})(\\d+)", "($1) $2-$3"))
            }
        }

    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview(context: Context) {
    RothuvseinTheme {
        PromptUser(context)
    }
}

//convert text using rothuvsein
fun Calculate(value: String): Long {
    var roth = "rothuvsein"
    var lowered = value.lowercase(Locale.ROOT)

    if (lowered.isNotEmpty()) {
// Split the input string into words using spaces as the delimiter
        val words = lowered.split(" ")
        var digits = ""
        val number: Long
        // Iterate through each word
        for (word in words) {
            if (word.isNotEmpty()) {
                var index = 6 % word.length
                var myChar: Char = word[index]
                if (roth.contains(myChar)) {
                    digits += roth.indexOf(myChar)
                }
            }

        }
        //return digits
        if (digits.isNotEmpty()) {
            number = digits.toLong()
            return number
        } else {
            return 0L
        }
    }
    return 0L
}

