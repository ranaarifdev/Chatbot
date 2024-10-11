package com.example.chatbot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatbot.Constants.ColorModelMessage
import com.example.chatbot.Constants.ColorUserMessage
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel = ChatViewModel()
        setContent {
            MyApplicationTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    content = { innerPadding ->
                        ChatPage(
                            modifier = Modifier.padding(innerPadding),
                            viewModel = viewModel
                        )
                    }
                )
            }
        }
    }
}

// Define the app theme
@Composable
fun MyApplicationTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme( // Use lightColorScheme() instead of ColorScheme.light()
            primary = Color(0xFF6200EE),
            secondary = Color(0xFF03DAC5)
        ),
        content = content
    )
}

@Composable
fun ChatPage(modifier: Modifier = Modifier, viewModel: ChatViewModel) {
    Column(modifier = modifier) {
        AppHeader()
        MessageList(
            modifier = Modifier.weight(1f),
            messageList = viewModel.messageList
        )
        MessageInput(
            onMessageSend = {
                viewModel.sendMessage(it)
            }
        )
    }
}

@Composable
fun MessageList(modifier: Modifier = Modifier, messageList: List<MessageModel>) {
    if (messageList.isEmpty()) {
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Ask me anything", fontSize = 22.sp)
        }
    } else {
        LazyColumn(
            modifier = modifier,
            reverseLayout = true
        ) {
            items(messageList.reversed()) {
                MessageRow(messageModel = it)
            }
        }
    }
}

@Composable
fun MessageRow(messageModel: MessageModel) {
    val isModel = messageModel.role == "model"
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = if (isModel) Arrangement.Start else Arrangement.End,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .padding(
                    start = if (isModel) 8.dp else 70.dp,
                    end = if (isModel) 70.dp else 8.dp,
                    top = 8.dp,
                    bottom = 8.dp
                )
                .clip(RoundedCornerShape(16.dp))
                .background(if (isModel) ColorModelMessage else ColorUserMessage)
                .padding(16.dp)
        ) {
            SelectionContainer {
                Text(
                    text = messageModel.message,
                    fontWeight = FontWeight.W500,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun MessageInput(onMessageSend: (String) -> Unit) {
    var message by remember { mutableStateOf("") }
    Row(
        modifier = Modifier.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            modifier = Modifier.weight(1f),
            value = message,
            onValueChange = { message = it },
            label = { Text("Enter your message") }
        )
        IconButton(onClick = {
            if (message.isNotEmpty()) {
                onMessageSend(message)
                message = ""
            }
        }) {
            Icon(imageVector = Icons.Default.Send, contentDescription = "Send")
        }
    }
}

@Composable
fun AppHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Text(
            modifier = Modifier.padding(16.dp),
            text = "RANA... CHATBOT",
            fontWeight = FontWeight.ExtraBold,
            color = Color.White,
            fontSize = 22.sp
        )
    }
}

class ChatViewModel : ViewModel() {
    val messageList = mutableStateListOf<MessageModel>()

    fun sendMessage(question: String) {
        val myQuestion = question.trim()
        viewModelScope.launch {
            try {
                // Add the user's question to the message list
                messageList.add(MessageModel(myQuestion, "user"))

                // Simulating a response based on user input
                val responseText = when {
                    myQuestion.contains("hello", ignoreCase = true) -> "Hi there! How can I help you?"
                    myQuestion.contains("what is your name", ignoreCase = true) -> "I am CHATBOT, your friendly chatbot!"
                    myQuestion.contains("where do you live", ignoreCase = true) -> "I exist in the digital realm, ready to assist you anytime!"
                    myQuestion.contains("what can you do", ignoreCase = true) -> "I can answer your questions, provide information, and assist you with various tasks!"
                    myQuestion.contains("who created you", ignoreCase = true) -> "I was created by talented developer RANA ARIF to assist users like you!"
                    myQuestion.contains("do you have feelings", ignoreCase = true) -> "I don't have feelings like humans, but I'm here to help and support you!"
                    myQuestion.contains("tell me a joke", ignoreCase = true) -> "Why did the computer go to the doctor? Because it had a virus!"
                    myQuestion.contains("what is kotlin", ignoreCase = true) -> "Kotlin is a modern programming language used for Android development. It is known for its concise syntax and interoperability with Java."
                    myQuestion.contains("how are you", ignoreCase = true) -> "I'm just a program, but thanks for asking!"
                    myQuestion.contains("bye", ignoreCase = true) -> "Goodbye! Have a great day!"
                    myQuestion.contains("rana arif", ignoreCase = true) -> "\"Rana Arif is a programmer who developed me by Hard work. \" +\n" +
                            "                    \"He is a student of Cyber Security in the evening at Emerson University in Multan. \" +\n" +
                            "                    \"Rana is passionate about technology and enjoys learning about computer security. \" +\n" +
                            "                    \"In addition to his studies, he works on various programming projects. \" +\n" +
                            "                    \"He is known for his problem-solving skills and creativity in coding. \" +\n" +
                            "                    \"Rana loves participating in hackathons and tech meetups. \" +\n" +
                            "                    \"His goal is to make a significant impact in the field of cybersecurity. \" +\n" +
                            "                    \"He believes in continuous learning and frequently updates his skills. \" +\n" +
                            "                    \"Rana is also interested in mentoring other students who share his passion.\""
                    else -> "Sorry, you get information after link chatbot  with AI i am showing just given cammands ."
                }

                // Add the model's response to the message list
                messageList.add(MessageModel(responseText, "model"))
            } catch (e: Exception) {
                messageList.add(MessageModel("Error: ${e.message}", "model"))
            }
        }
    }
}

data class MessageModel(
    val message: String,
    val role: String
)

object Constants {
    val ColorModelMessage = Color(0xFF6200EE)
    val ColorUserMessage = Color(0xFF03DAC5)
}
