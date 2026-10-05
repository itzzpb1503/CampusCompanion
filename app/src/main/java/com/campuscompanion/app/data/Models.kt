package com.campuscompanion.app.data

import java.time.DayOfWeek
import java.time.LocalTime

enum class ClassType { THEORY, LAB }
enum class MealType { BREAKFAST, LUNCH, SNACKS, DINNER }

data class TimetableEntry(
    val day: DayOfWeek,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val courseName: String,
    val courseCode: String,
    val faculty: String,
    val room: String,
    val block: String,
    val classType: ClassType
)

data class StudentProfile(
    val name: String = "Pratham Bhardwaj",
    val regNo: String = "26BCS7099",
    val appNo: String = "2026328514",
    val branch: String = "B.Tech - Computer Science and Engineering (Cyber Security)",
    val school: String = "School of Computer Science & Engineering",
    val email: String = "pratham.26bcs7099@vitapstudent.ac.in",
    val mobile: String = "+91 9625307542"
)

data class MentorProfile(
    val facultyName: String = "Palacharla Ravi Kumar",
    val facultyId: String = "70727",
    val designation: String = "Assistant Professor Sr. Grade-2",
    val department: String = "Dept. of Networking and Security, SCOPE",
    val cabin: String = "CB-615 - N",
    val email: String = "ravikumar.p@vitap.ac.in",
    val mobile: String = "+91 9000456003"
)

object MockData {
    val student = StudentProfile()
    val mentor = MentorProfile()

    val timetable = listOf(
        // Monday
        TimetableEntry(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(9, 50), "Calculus for Engineers", "MAT1001", "A. Ramesh", "220", "AB-2", ClassType.THEORY),
        TimetableEntry(DayOfWeek.MONDAY, LocalTime.of(11, 1), LocalTime.of(11, 50), "Fundamentals of Electrical and Electronics Engineering", "ECE1002", "Pothi Reddy Krishna Mohan Reddy", "417", "AB-1", ClassType.THEORY),
        TimetableEntry(DayOfWeek.MONDAY, LocalTime.of(12, 1), LocalTime.of(12, 50), "Fundamentals of Electrical and Electronics Engineering", "ECE1002", "Pothi Reddy Krishna Mohan Reddy", "417", "AB-1", ClassType.THEORY),
        // Tuesday
        TimetableEntry(DayOfWeek.TUESDAY, LocalTime.of(9, 0), LocalTime.of(9, 50), "Calculus for Engineers", "MAT1001", "A. Ramesh", "220", "AB-2", ClassType.THEORY),
        TimetableEntry(DayOfWeek.TUESDAY, LocalTime.of(11, 1), LocalTime.of(11, 50), "Problem Solving using Python", "CSE1012", "Sonia Das", "137", "AB-1", ClassType.THEORY),
        // Wednesday
        TimetableEntry(DayOfWeek.WEDNESDAY, LocalTime.of(10, 0), LocalTime.of(10, 50), "Modern Physics", "PHY1008", "Nilanjan Roy", "411", "AB-2", ClassType.THEORY)
    )

    val offlineQuotes = listOf(
        "Small progress every day leads to big results.",
        "Consistency is what transforms average into excellence.",
        "Focus on the step in front of you, not the whole staircase.",
        "Code, learn, iterate, succeed."
    )
}
