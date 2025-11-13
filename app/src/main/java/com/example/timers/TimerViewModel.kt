package com.example.timers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.max
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue


class TimerViewModel : ViewModel() {

    var remainingMillis by mutableStateOf(0L)
        private set

    var totalMillis by mutableStateOf(0L)
        private set

    var isRunning by mutableStateOf(false)
        private set

    var mode by mutableStateOf(Mode.Pomodoro)
        private set

    private var endEpochMillis: Long? = null
    private var pausedRemainingMillis: Long? = null
    private var tickerJob: Job? = null

    private val pomodoroMin = 25
    private val shortBreakMin = 5
    private val longBreakMin = 15

    enum class Mode { Pomodoro, ShortBreak, LongBreak, Custom}

    fun setDurationMinutes(minutes: Int){
        totalMillis = minutes * 60000L
        remainingMillis = totalMillis
        isRunning = false
        endEpochMillis = null
        pausedRemainingMillis = null
        stopTicker()
    }

    private fun now(): Long = System.currentTimeMillis()
    private fun computeRemaining(currentEpochMillis: Long) : Long{
        val end = endEpochMillis ?: return remainingMillis
       val remain = end -currentEpochMillis
        return max(remain, 0L)
    }

    fun selectPomodoro(){
        mode = Mode.Pomodoro
        setDurationMinutes(pomodoroMin)
    }
    fun selectShortBreak(){
        mode = Mode.ShortBreak
        setDurationMinutes(shortBreakMin)
    }
    fun selectLongBreak(){
        mode = Mode.LongBreak
        setDurationMinutes(longBreakMin)
    }

    fun start(){
        if (isRunning) return
        isRunning = true

        val now = now()
        endEpochMillis = if (pausedRemainingMillis != null){
            now + pausedRemainingMillis!!
        } else{
            now + totalMillis
        }

        pausedRemainingMillis = null
        startTicker()
    }

    fun pause(){
        if (!isRunning) return
        isRunning = false

        pausedRemainingMillis = computeRemaining(now())
        remainingMillis = pausedRemainingMillis!!
        stopTicker()
    }

    fun reset(){
        isRunning = false
        endEpochMillis = null
        pausedRemainingMillis = null
        if (mode == Mode.Custom) {
            totalMillis = 0L
            remainingMillis = 0L
        } else {
            remainingMillis = totalMillis
        }
        stopTicker()
    }

    fun startCustom(minutes: Long) {
        mode = Mode.Custom
        totalMillis = minutes * 60000L
        remainingMillis = totalMillis
        start()
    }

    private fun startTicker(){
        stopTicker()
        tickerJob = viewModelScope.launch{
            while (isRunning){
                remainingMillis = computeRemaining(now())

                if (remainingMillis <= 0L){
                    remainingMillis = 0L
                    isRunning = false
                    stopTicker()

                    when(mode){
                        Mode.Pomodoro -> {
                            selectShortBreak()
                            start()
                        }
                        Mode.ShortBreak -> {
                            selectPomodoro()
                            start()
                        }
                        else -> Unit
                    }
                    break
                }

                delay(1000L)
            }
        }
    }
    private fun stopTicker(){
        tickerJob?.cancel()
        tickerJob = null
    }
}