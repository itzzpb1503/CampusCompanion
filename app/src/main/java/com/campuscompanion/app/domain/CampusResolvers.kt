package com.campuscompanion.app.domain

import com.campuscompanion.app.data.MealType
import com.campuscompanion.app.data.TimetableEntry
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit

sealed interface NextClassState {
    data class Upcoming(val entry: TimetableEntry, val startsInMinutes: Long) : NextClassState
    data class Ongoing(val entry: TimetableEntry, val endsInMinutes: Long) : NextClassState
    object NoMoreToday : NextClassState
}

data class MealWindow(
    val type: MealType,
    val start: LocalTime,
    val end: LocalTime,
    val menuPreview: String
)

sealed interface NextMealState {
    data class Available(val window: MealWindow, val isOngoing: Boolean, val minutesUntil: Long) : NextMealState
    object DayComplete : NextMealState
}

object CampusResolvers {

    fun resolveNextClass(schedule: List<TimetableEntry>, now: LocalDateTime = LocalDateTime.now()): NextClassState {
        val today = now.dayOfWeek
        val currentTime = now.toLocalTime()

        val dailyClasses = schedule.filter { it.day == today }.sortedBy { it.startTime }

        val active = dailyClasses.firstOrNull { !currentTime.isBefore(it.startTime) && currentTime.isBefore(it.endTime) }
        if (active != null) {
            return NextClassState.Ongoing(active, ChronoUnit.MINUTES.between(currentTime, active.endTime))
        }

        val next = dailyClasses.firstOrNull { it.startTime.isAfter(currentTime) }
        if (next != null) {
            return NextClassState.Upcoming(next, ChronoUnit.MINUTES.between(currentTime, next.startTime))
        }

        return NextClassState.NoMoreToday
    }

    fun resolveNextMeal(now: LocalDateTime = LocalDateTime.now()): NextMealState {
        val today = now.dayOfWeek
        val currentTime = now.toLocalTime()

        val isWeekendShift = today == DayOfWeek.SUNDAY || today == DayOfWeek.MONDAY
        val bStart = if (isWeekendShift) LocalTime.of(7, 15) else LocalTime.of(7, 0)
        val bEnd = if (isWeekendShift) LocalTime.of(9, 15) else LocalTime.of(9, 0)

        val mealWindows = listOf(
            MealWindow(MealType.BREAKFAST, bStart, bEnd, "Poha • Idli • Sambar • Tea/Coffee"),
            MealWindow(MealType.LUNCH, LocalTime.of(12, 30), LocalTime.of(14, 15), "Rice • Dal • Mixed Veg • Curd"),
            MealWindow(MealType.SNACKS, LocalTime.of(16, 30), LocalTime.of(18, 15), "Samosa • Tea • Biscuits"),
            MealWindow(MealType.DINNER, LocalTime.of(19, 15), LocalTime.of(21, 0), "Roti • Paneer/Dal • Jeera Rice • Sweet")
        )

        for (meal in mealWindows) {
            if (currentTime.isBefore(meal.end)) {
                val ongoing = !currentTime.isBefore(meal.start)
                val diff = if (ongoing) 0 else ChronoUnit.MINUTES.between(currentTime, meal.start)
                return NextMealState.Available(meal, ongoing, diff)
            }
        }
        return NextMealState.DayComplete
    }

    fun getGreeting(now: LocalTime = LocalTime.now()): String = when (now.hour) {
        in 5..11 -> "Good Morning 🌅"
        in 12..16 -> "Good Afternoon ☀️"
        in 17..21 -> "Good Evening 🌇"
        else -> "Good Night 🌙"
    }
}
