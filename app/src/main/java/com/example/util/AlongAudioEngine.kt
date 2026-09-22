package com.example.util

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.util.Log
import com.example.data.model.WorldTheme
import kotlinx.coroutines.*
import kotlin.math.sin
import kotlin.random.Random

/**
 * AlongAudioEngine
 * 100% Offline, Procedural, Peaceful Sound & Lo-Fi Generator.
 * Zero external dependencies or copyright issues.
 * Generates peaceful harmonic chords and soothing ambient nature soundscapes.
 */
class AlongAudioEngine(private val context: Context) {

    private val sampleRate = 22050
    private var isPlaying = false
    private var job: Job? = null
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    private var audioTrack: AudioTrack? = null

    // Volume & toggle states (0.0 to 1.0)
    var isMusicEnabled: Boolean = false
    var isAmbientEnabled: Boolean = false
    var musicVolume: Float = 0.4f
    var ambientVolume: Float = 0.4f
    var currentTheme: WorldTheme = WorldTheme.SAKURA_GARDEN

    fun start() {
        if (isPlaying) return
        isPlaying = true

        val bufferSize = AudioTrack.getMinBufferSize(
            sampleRate,
            AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        ).coerceAtLeast(sampleRate / 4)

        try {
            audioTrack = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(sampleRate)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build()
                )
                .setBufferSizeInBytes(bufferSize)
                .setTransferMode(AudioTrack.MODE_STREAM)
                .build()

            audioTrack?.play()

            job = scope.launch {
                val chunkSize = 2048
                val buffer = ShortArray(chunkSize)
                var sampleIndex = 0L

                // Chord progression frequencies (peaceful major/minor 9th chords)
                // Rooted around warm C, G, Am, F or theme-specific scales
                val chordProgressions = mapOf(
                    WorldTheme.SAKURA_GARDEN to listOf(
                        doubleArrayOf(261.63, 329.63, 392.00, 493.88), // Cmaj7
                        doubleArrayOf(220.00, 261.63, 329.63, 392.00), // Am7
                        doubleArrayOf(174.61, 220.00, 261.63, 329.63), // Fmaj7
                        doubleArrayOf(196.00, 246.94, 293.66, 392.00)  // G6
                    ),
                    WorldTheme.FOREST_MORNING to listOf(
                        doubleArrayOf(196.00, 246.94, 293.66, 369.99), // Gmaj7
                        doubleArrayOf(164.81, 196.00, 246.94, 293.66), // Em7
                        doubleArrayOf(220.00, 261.63, 329.63, 392.00), // Am7
                        doubleArrayOf(146.83, 185.00, 220.00, 293.66)  // Dsus4
                    ),
                    WorldTheme.QUIET_OCEAN to listOf(
                        doubleArrayOf(174.61, 220.00, 261.63, 329.63), // Fmaj7
                        doubleArrayOf(130.81, 164.81, 196.00, 261.63), // C/E
                        doubleArrayOf(146.83, 174.61, 220.00, 261.63), // Dm7
                        doubleArrayOf(196.00, 246.94, 293.66, 349.23)  // G7sus
                    ),
                    WorldTheme.RAINY_WINDOW to listOf(
                        doubleArrayOf(220.00, 261.63, 329.63, 392.00), // Am7
                        doubleArrayOf(174.61, 220.00, 261.63, 329.63), // Fmaj7
                        doubleArrayOf(130.81, 164.81, 196.00, 261.63), // C
                        doubleArrayOf(164.81, 196.00, 246.94, 293.66)  // Em7
                    )
                )

                var currentChordIndex = 0
                var samplesInCurrentChord = 0
                val chordDurationSamples = sampleRate * 5 // 5 seconds per chord

                var wavePhase = 0.0
                var crackleTimer = 0

                while (isActive && isPlaying) {
                    val currentChords = chordProgressions[currentTheme] ?: chordProgressions[WorldTheme.SAKURA_GARDEN]!!
                    val chord = currentChords[currentChordIndex % currentChords.size]

                    val musicVol = if (isMusicEnabled) (musicVolume * 0.35f) else 0f
                    val ambientVol = if (isAmbientEnabled) (ambientVolume * 0.4f) else 0f

                    for (i in 0 until chunkSize) {
                        var combinedSample = 0.0

                        // 1. Peaceful Lo-Fi Harmonic Synthesis (Warm Electric Piano / Chimes)
                        if (musicVol > 0.001f) {
                            val chordProgress = samplesInCurrentChord.toDouble() / chordDurationSamples.toDouble()
                            // Smooth envelope (gentle attack, sustained, slow decay)
                            val envelope = when {
                                chordProgress < 0.15 -> chordProgress / 0.15
                                chordProgress > 0.85 -> (1.0 - chordProgress) / 0.15
                                else -> 1.0
                            }

                            var chordSample = 0.0
                            for (freq in chord) {
                                val t = (sampleIndex + i).toDouble() / sampleRate
                                // Fundamental sine + gentle second harmonic for warm Rhodes character
                                val tone = sin(2.0 * Math.PI * freq * t) * 0.7 +
                                        sin(4.0 * Math.PI * freq * t) * 0.25 +
                                        sin(6.0 * Math.PI * freq * t) * 0.05
                                chordSample += tone
                            }
                            chordSample /= chord.size
                            combinedSample += chordSample * envelope * musicVol
                        }

                        // 2. Procedural Ambient Nature Soundscapes
                        if (ambientVol > 0.001f) {
                            var ambientSample = 0.0
                            val t = (sampleIndex + i).toDouble() / sampleRate

                            when (currentTheme) {
                                WorldTheme.RAINY_WINDOW -> {
                                    // Soft pink-filtered rain murmur with occasional gentle droplet
                                    val noise = (Random.nextDouble() * 2.0 - 1.0) * 0.25
                                    var droplet = 0.0
                                    if (Random.nextInt(4000) == 0) {
                                        droplet = sin(2.0 * Math.PI * 800.0 * t) * 0.5
                                    }
                                    ambientSample = noise + droplet
                                }
                                WorldTheme.QUIET_OCEAN -> {
                                    // Slow swell waves: low frequency swell modulates soft white noise
                                    val swell = (sin(2.0 * Math.PI * 0.12 * t) + 1.0) * 0.5 // 8-second wave period
                                    val noise = (Random.nextDouble() * 2.0 - 1.0) * 0.35
                                    ambientSample = noise * swell
                                }
                                WorldTheme.FOREST_MORNING -> {
                                    // Gentle forest breeze rustle + occasional distant soft chime
                                    val breeze = (sin(2.0 * Math.PI * 0.2 * t) + 1.0) * 0.15
                                    val noise = (Random.nextDouble() * 2.0 - 1.0) * 0.1
                                    ambientSample = (noise * breeze)
                                }
                                WorldTheme.COZY_ROOM -> {
                                    // Soft fireplace crackle and low warm hearth rumble
                                    val rumble = sin(2.0 * Math.PI * 65.0 * t) * 0.1
                                    var pop = 0.0
                                    crackleTimer--
                                    if (crackleTimer <= 0) {
                                        pop = (Random.nextDouble() * 2.0 - 1.0) * 0.6
                                        crackleTimer = Random.nextInt(1500, 6000)
                                    }
                                    ambientSample = rumble + pop
                                }
                                else -> {
                                    // Gentle tranquil night breeze / soft wind
                                    val softWind = sin(2.0 * Math.PI * 0.08 * t) * 0.15
                                    val noise = (Random.nextDouble() * 2.0 - 1.0) * 0.1
                                    ambientSample = noise * softWind
                                }
                            }
                            combinedSample += ambientSample * ambientVol
                        }

                        // Clamp to 16-bit PCM range
                        val clamped = combinedSample.coerceIn(-1.0, 1.0)
                        buffer[i] = (clamped * 32767.0).toInt().toShort()

                        samplesInCurrentChord++
                        if (samplesInCurrentChord >= chordDurationSamples) {
                            samplesInCurrentChord = 0
                            currentChordIndex++
                        }
                    }

                    sampleIndex += chunkSize
                    audioTrack?.write(buffer, 0, chunkSize)
                }
            }
        } catch (e: Exception) {
            Log.e("AlongAudioEngine", "AudioTrack start failed: ${e.message}")
        }
    }

    fun stop() {
        isPlaying = false
        job?.cancel()
        job = null
        try {
            audioTrack?.stop()
            audioTrack?.release()
        } catch (e: Exception) {
            // Ignore
        }
        audioTrack = null
    }

    fun updateSettings(musicOn: Boolean, ambientOn: Boolean, musicVol: Float, ambientVol: Float, theme: WorldTheme) {
        this.isMusicEnabled = musicOn
        this.isAmbientEnabled = ambientOn
        this.musicVolume = musicVol.coerceIn(0f, 1f)
        this.ambientVolume = ambientVol.coerceIn(0f, 1f)
        this.currentTheme = theme

        if ((isMusicEnabled || isAmbientEnabled) && !isPlaying) {
            start()
        } else if (!isMusicEnabled && !isAmbientEnabled && isPlaying) {
            stop()
        }
    }
}
