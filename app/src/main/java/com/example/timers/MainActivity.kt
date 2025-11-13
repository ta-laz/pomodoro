package com.example.timers

import android.os.Bundle
import com.example.timers.ui.theme.Saira
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.timers.ui.theme.TimersTheme
import androidx.compose.runtime.getValue
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TimersTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background),
                    color = MaterialTheme.colorScheme.background
                ){
                    App()
                }
            }
        }
    }
}

@Composable
fun App(){
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"
    ){
        composable("home"){
            LandingPage(navController = navController)
        }
        composable("pomodoro"){
            Pomodoro(navController = navController)
        }
        composable("custom"){
            Custom(navController = navController)
        }
    }
}

@Composable
fun LandingPage(navController: NavController) {
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(40.dp)
        .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Spacer(modifier = Modifier.weight(0.8f))

        Text(
            text = "Countdown Timers",
            fontFamily = Saira,
            fontWeight = FontWeight.Bold,
            fontSize = 44.sp,
            lineHeight = 44.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 48.dp).fillMaxWidth()
        )
        Button(
            onClick = { navController.navigate("pomodoro") },
            modifier = Modifier
                .width(200.dp)
                .padding(vertical = 8.dp)
        ) {
            Text("Pomodoro",
                fontFamily = Saira,
                fontSize = 22.sp)
        }
        Button(
            onClick = { navController.navigate("custom") },
            modifier = Modifier
                .width(200.dp)
                .padding(vertical = 8.dp)
        ) {
            Text("Custom",
                fontFamily = Saira,
                fontSize = 22.sp)
        }
        Spacer(modifier = Modifier.weight(0.2f))

        Image(
            painter = painterResource(id = R.drawable.main_tomato),
            contentDescription = "Tomato mascot",
            modifier = Modifier.size(260.dp)
        )
    }
}

@Composable
fun Pomodoro(navController: NavController, viewModel: TimerViewModel = viewModel()){

    if (viewModel.totalMillis == 0L){
        viewModel.selectPomodoro()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(40.dp)
    ) {
            IconButton(
                onClick = { navController.navigate("home") },
                modifier = Modifier.size(48.dp).align(Alignment.TopStart)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(32.dp)
                )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(40.dp)
                .align(Alignment.Center)
                .padding(bottom = 24.dp)
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = { viewModel.selectPomodoro() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .padding(bottom = 8.dp)
            ) {
                Text(
                    "Pomodoro",
                    fontFamily = Saira,
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = { viewModel.selectShortBreak() },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        "Short Break",
                        fontFamily = Saira,
                        fontSize = 16.sp
                    )
                }

                Button(
                    onClick = { viewModel.selectLongBreak() },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        "Long Break",
                        fontFamily = Saira,
                        fontSize = 16.sp
                    )
                }
            }

            Text(
                text = formatMillis(viewModel.remainingMillis),
                fontFamily = Saira,
                fontWeight = FontWeight.Bold,
                fontSize = 48.sp,
                modifier = Modifier.padding(vertical = 32.dp),
            )

            Row(
                modifier = Modifier
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        if (viewModel.isRunning) {
                            viewModel.pause()
                        } else {
                            viewModel.start()
                        }
                    },
                    modifier = Modifier.width(150.dp)
                ) {
                    Text(
                        if (viewModel.isRunning) "Pause" else "Start",
                        fontFamily = Saira,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )
                }

                IconButton(
                    onClick = { viewModel.reset() },
                    modifier = Modifier
                        .size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Refresh,
                        contentDescription = "Reset Timer",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }


            Image(
                painter = painterResource(id = R.drawable.pomodoro_man),
                contentDescription = "Tomato mascot",
                modifier = Modifier.size(260.dp).align(Alignment.BottomCenter)
            )
        }
    }


@Composable
fun Custom(navController: NavController, viewModel: TimerViewModel = viewModel()) {
    var inputMinutes by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(40.dp)
    ) {
        IconButton(
            onClick = { navController.navigate("home") },
            modifier = Modifier
                .size(48.dp)
                .align(Alignment.TopStart)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(40.dp)
                .align(Alignment.Center)
                .padding(bottom = 32.dp)
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Custom Timer",
                fontFamily = Saira,
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Input field
            TextField(
                value = inputMinutes,
                onValueChange = { inputMinutes = it },
                label = { Text("Enter minutes") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            Text(
                text = formatMillis(viewModel.remainingMillis),
                fontFamily = Saira,
                fontWeight = FontWeight.Bold,
                fontSize = 48.sp,
                modifier = Modifier.padding(vertical = 32.dp)
            )

            Row(
                modifier = Modifier
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        val mins = inputMinutes.toLongOrNull()
                        if (mins != null && !viewModel.isRunning) {
                            viewModel.startCustom(mins)
                        } else if (viewModel.isRunning) {
                            viewModel.pause()
                        }
                    },
                    modifier = Modifier.width(150.dp)
                ) {
                    Text(
                        if (viewModel.isRunning) "Pause" else "Start",
                        fontFamily = Saira,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )
                }

                IconButton(
                    onClick = { viewModel.reset() },
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Refresh,
                        contentDescription = "Reset Timer",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
        Image(
            painter = painterResource(id = R.drawable.custom_pomodoro),
            contentDescription = "Tomato mascot",
            modifier = Modifier.size(260.dp).align(Alignment.BottomCenter)
        )

    }
}


@Preview(showBackground = true)
@Composable
fun LandingPagePreview() {
    TimersTheme {
        val nav = rememberNavController()
        LandingPage(navController = nav)
    }
}

fun formatMillis(ms: Long): String {
    val totalSeconds = ms / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format("%02d:%02d", minutes, seconds)
}
