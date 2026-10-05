package com.campuscompanion.app

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.campuscompanion.app.data.MockData
import com.campuscompanion.app.domain.CampusResolvers
import com.campuscompanion.app.domain.NextClassState
import com.campuscompanion.app.domain.NextMealState
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(colorScheme = lightColorScheme(primary = Color(0xFF1E56A0), secondary = Color(0xFF163172))) {
                CampusAppRoot()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CampusAppRoot() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(modifier = Modifier.width(300.dp)) {
                // Profile Drawer Header
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(24.dp)
                ) {
                    Column {
                        Text(text = MockData.student.name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        Text(text = MockData.student.regNo, color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                NavigationDrawerItem(label = { Text("🏠 Home") }, selected = false, onClick = { scope.launch { drawerState.close() }; navController.navigate("home") })
                NavigationDrawerItem(label = { Text("📚 My Classes") }, selected = false, onClick = { scope.launch { drawerState.close() }; navController.navigate("classes") })
                NavigationDrawerItem(label = { Text("🍽️ Mess Menu") }, selected = false, onClick = { scope.launch { drawerState.close() }; navController.navigate("mess") })
                NavigationDrawerItem(label = { Text("🎓 VTOP") }, selected = false, onClick = {
                    scope.launch { drawerState.close() }
                    CustomTabsIntent.Builder().build().launchUrl(context, Uri.parse("https://vtop.vitap.ac.in/vtop/login"))
                })
                NavigationDrawerItem(label = { Text("🤖 NOVA Assistant") }, selected = false, onClick = { scope.launch { drawerState.close() }; navController.navigate("nova") })
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                NavigationDrawerItem(label = { Text("👤 My Information") }, selected = false, onClick = { scope.launch { drawerState.close() }; navController.navigate("info") })
                NavigationDrawerItem(label = { Text("👨‍🏫 Mentor Information") }, selected = false, onClick = { scope.launch { drawerState.close() }; navController.navigate("mentor") })
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Campus Companion", fontWeight = FontWeight.SemiBold) },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { navController.navigate("nova") },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White,
                    shape = CircleShape
                ) {
                    Text("🤖", fontSize = 22.sp)
                }
            }
        ) { padding ->
            NavHost(navController = navController, startDestination = "home", modifier = Modifier.padding(padding)) {
                composable("home") { HomeScreen() }
                composable("classes") { ClassesScreen() }
                composable("mess") { MessScreen() }
                composable("nova") { NovaScreen() }
                composable("info") { StudentInfoScreen() }
                composable("mentor") { MentorScreen() }
            }
        }
    }
}

@Composable
fun HomeScreen() {
    val now = remember { LocalDateTime.now() }
    val greeting = remember { CampusResolvers.getGreeting(now.toLocalTime()) }
    val dateString = remember { now.format(DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy")) }
    val nextClass = remember { CampusResolvers.resolveNextClass(MockData.timetable, now) }
    val nextMeal = remember { CampusResolvers.resolveNextMeal(now) }

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        item {
            Text(text = "$greeting, Pratham 👋", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text(text = dateString, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            // Next Class Card
            Card(
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("📚 Next Class", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(6.dp))
                    when (nextClass) {
                        is NextClassState.Upcoming -> {
                            Text(nextClass.entry.courseName, fontWeight = FontWeight.SemiBold, fontSize = 17.sp)
                            Text("${nextClass.entry.startTime} – ${nextClass.entry.endTime} • Room ${nextClass.entry.room} (${nextClass.entry.block})")
                            Text("Starts in ${nextClass.startsInMinutes} minutes", color = Color(0xFF007A3D), fontWeight = FontWeight.Medium)
                        }
                        is NextClassState.Ongoing -> {
                            Text(nextClass.entry.courseName, fontWeight = FontWeight.SemiBold, fontSize = 17.sp)
                            Text("Ongoing now in Room ${nextClass.entry.room} • Ends in ${nextClass.endsInMinutes}m")
                        }
                        NextClassState.NoMoreToday -> {
                            Text("No more classes scheduled today 🎉", fontSize = 15.sp)
                        }
                    }
                }
            }
        }

        item {
            // Next Meal Card
            Card(
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("🍽️ Next Meal", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary)
                    Spacer(modifier = Modifier.height(6.dp))
                    when (nextMeal) {
                        is NextMealState.Available -> {
                            Text("${nextMeal.window.type.name} • ${nextMeal.window.start} - ${nextMeal.window.end}", fontWeight = FontWeight.SemiBold)
                            Text(nextMeal.window.menuPreview, style = MaterialTheme.typography.bodyMedium)
                        }
                        NextMealState.DayComplete -> {
                            Text("Mess service finished for today.", fontSize = 15.sp)
                        }
                    }
                }
            }
        }

        item {
            // Quote Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("💡 Daily Motivation", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("\"${MockData.offlineQuotes.first()}\"", fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)
                }
            }
        }
    }
}

@Composable
fun ClassesScreen() {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        item {
            Text("Complete Timetable", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))
        }
        items(MockData.timetable) { entry ->
            Card(
                modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                        Text(entry.courseCode, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        Text(entry.day.name, fontSize = 12.sp, color = Color.Gray)
                    }
                    Text(entry.courseName, fontWeight = FontWeight.SemiBold)
                    Text("${entry.startTime} – ${entry.endTime} | Room ${entry.room}, ${entry.block}", style = MaterialTheme.typography.bodyMedium)
                    Text("Faculty: ${entry.faculty}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                }
            }
        }
    }
}

@Composable
fun MessScreen() {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Mess Menu", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Regular Timings", fontWeight = FontWeight.Bold)
                Text("• Breakfast: 7:00 AM – 9:00 AM (7:15 AM Sun/Mon)")
                Text("• Lunch: 12:30 PM – 2:15 PM")
                Text("• Snacks: 4:30 PM – 6:15 PM")
                Text("• Dinner: 7:15 PM – 9:00 PM")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { /* Excel upload intent trigger */ }, modifier = Modifier.fillMaxWidth()) {
            Text("Import Updated Mess Excel Sheet")
        }
    }
}

@Composable
fun StudentInfoScreen() {
    val s = MockData.student
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Student Information", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))
        Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                InfoRow("Name", s.name)
                InfoRow("Reg No.", s.regNo)
                InfoRow("Application No.", s.appNo)
                InfoRow("Program", s.branch)
                InfoRow("School", s.school)
                InfoRow("Email", s.email)
                InfoRow("Mobile", s.mobile)
            }
        }
    }
}

@Composable
fun MentorScreen() {
    val m = MockData.mentor
    val context = LocalContext.current
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Mentor Information", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))
        Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                InfoRow("Faculty Name", m.facultyName)
                InfoRow("Faculty ID", m.facultyId)
                InfoRow("Designation", m.designation)
                InfoRow("Department", m.department)
                InfoRow("Cabin", m.cabin)
                InfoRow("Email", m.email)
                InfoRow("Phone", m.mobile)
                Spacer(modifier = Modifier.height(16.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(onClick = {
                        val callIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${m.mobile}"))
                        context.startActivity(callIntent)
                    }) {
                        Text("Call Mentor")
                    }
                    OutlinedButton(onClick = {
                        val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:${m.email}"))
                        context.startActivity(emailIntent)
                    }) {
                        Text("Send Email")
                    }
                }
            }
        }
    }
}

@Composable
fun NovaScreen() {
    var query by remember { mutableStateOf("") }
    var response by remember { mutableStateOf("Hi Pratham! I am NOVA 🤖. Ask me about your next class, meals, or schedule.") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("NOVA — Campus Assistant", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))
        Card(
            modifier = Modifier.fillMaxWidth().weight(1f),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                Text(response, style = MaterialTheme.typography.bodyLarge)
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                label = { Text("Ask NOVA...") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    val q = query.lowercase()
                    response = when {
                        "class" in q || "next" in q -> {
                            val next = CampusResolvers.resolveNextClass(MockData.timetable)
                            if (next is NextClassState.Upcoming) "Your next class is ${next.entry.courseName} in Room ${next.entry.room} (${next.entry.block})." else "No upcoming classes found today."
                        }
                        "lunch" in q || "mess" in q || "food" in q || "meal" in q -> "Mess menu for lunch: Rice, Dal, Mixed Veg, Curd."
                        "mentor" in q -> "Your mentor is ${MockData.mentor.facultyName}, located in Cabin ${MockData.mentor.cabin}."
                        else -> "I understand campus queries about your timetable, mess schedule, and mentor information!"
                    }
                    query = ""
                },
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Text("Send")
            }
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(label, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        Text(value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
    }
}
