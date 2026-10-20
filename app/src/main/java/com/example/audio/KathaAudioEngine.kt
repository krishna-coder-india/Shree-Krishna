package com.example.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.os.Build
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import java.util.Locale
import kotlin.math.sin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class KathaAudioEngine(private val context: Context) : TextToSpeech.OnInitListener {

  private var tts: TextToSpeech? = null
  private val scope = CoroutineScope(Dispatchers.Default)

  var isInitialized by mutableStateOf(false)
    private set

  var isSpeaking by mutableStateOf(false)
    private set

  var isPaused by mutableStateOf(false)
    private set

  var speechRate by mutableFloatStateOf(1.0f)
    private set

  var isAmbientMusicOn by mutableStateOf(true)
    private set

  var currentDayPlaying by mutableStateOf<Int?>(null)
    private set

  var currentSpeechTitle by mutableStateOf("")
    private set

  private var ambientTrack: AudioTrack? = null
  private var ambientJob: Job? = null

  private var currentStoryText: String = ""
  private var textChunks: List<String> = emptyList()
  private var currentChunkIndex = 0

  init {
    tts = TextToSpeech(context.applicationContext, this)
  }

  override fun onInit(status: Int) {
    if (status == TextToSpeech.SUCCESS) {
      val hindi = Locale("hi", "IN")
      val result = tts?.setLanguage(hindi)
      if (
        result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED
      ) {
        tts?.language = Locale.getDefault()
      }
      tts?.setSpeechRate(speechRate)
      tts?.setOnUtteranceProgressListener(
        object : UtteranceProgressListener() {
          override fun onStart(utteranceId: String?) {
            isSpeaking = true
            isPaused = false
          }

          override fun onDone(utteranceId: String?) {
            currentChunkIndex++
            if (currentChunkIndex < textChunks.size && isSpeaking) {
              speakNextChunk()
            } else {
              isSpeaking = false
              isPaused = false
              stopAmbientDrone()
            }
          }

          override fun onError(utteranceId: String?) {
            isSpeaking = false
            isPaused = false
            stopAmbientDrone()
          }
        }
      )
      isInitialized = true
    }
  }

  fun playKatha(dayNumber: Int, title: String, text: String) {
    if (!isInitialized) return
    currentDayPlaying = dayNumber
    currentSpeechTitle = title
    currentStoryText = cleanTextForSpeech(text)

    // Split text into manageable paragraphs/sentences for natural narration
    textChunks =
      currentStoryText
        .split("\n\n")
        .map { it.replace("#", "").trim() }
        .filter { it.isNotBlank() }

    currentChunkIndex = 0

    if (isAmbientMusicOn) {
      startAmbientDrone()
    }
    speakNextChunk()
  }

  private fun speakNextChunk() {
    if (currentChunkIndex in textChunks.indices) {
      val chunk = textChunks[currentChunkIndex]
      val params = android.os.Bundle()
      params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "bhagwat_$currentChunkIndex")
      tts?.speak(chunk, TextToSpeech.QUEUE_FLUSH, params, "bhagwat_$currentChunkIndex")
      isSpeaking = true
      isPaused = false
    } else {
      isSpeaking = false
      isPaused = false
      stopAmbientDrone()
    }
  }

  fun pause() {
    if (isSpeaking) {
      tts?.stop()
      isPaused = true
      isSpeaking = false
      stopAmbientDrone()
    }
  }

  fun resume() {
    if (isPaused && textChunks.isNotEmpty()) {
      if (isAmbientMusicOn) {
        startAmbientDrone()
      }
      speakNextChunk()
    }
  }

  fun stop() {
    tts?.stop()
    isSpeaking = false
    isPaused = false
    currentDayPlaying = null
    currentChunkIndex = 0
    stopAmbientDrone()
  }

  fun setRate(rate: Float) {
    speechRate = rate
    tts?.setSpeechRate(rate)
  }

  fun toggleAmbientMusic() {
    isAmbientMusicOn = !isAmbientMusicOn
    if (isAmbientMusicOn && isSpeaking) {
      startAmbientDrone()
    } else if (!isAmbientMusicOn) {
      stopAmbientDrone()
    }
  }

  private fun cleanTextForSpeech(raw: String): String {
    return raw
      .replace(Regex("#+\\s*"), "")
      .replace(Regex("[*_\\[\\]]"), "")
      .replace("॥", "।")
  }

  // Generates a soft, soothing meditative drone (Tanpura C# and G harmomics)
  private fun startAmbientDrone() {
    stopAmbientDrone()
    ambientJob =
      scope.launch(Dispatchers.Default) {
        try {
          val sampleRate = 22050
          val bufferSize =
            AudioTrack.getMinBufferSize(
              sampleRate,
              AudioFormat.CHANNEL_OUT_MONO,
              AudioFormat.ENCODING_PCM_16BIT,
            ).coerceAtLeast(sampleRate * 2)

          ambientTrack =
            AudioTrack.Builder()
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

          ambientTrack?.setVolume(0.18f)
          ambientTrack?.play()

          val shortBuffer = ShortArray(bufferSize / 2)
          var phase1 = 0.0
          var phase2 = 0.0
          val freq1 = 138.59 // C#3 root
          val freq2 = 207.65 // G#3 panchama

          while (isActive) {
            for (i in shortBuffer.indices) {
              val sample1 = sin(phase1)
              val sample2 = sin(phase2) * 0.5
              val mixed = (sample1 + sample2) / 1.5 * 0.15
              shortBuffer[i] = (mixed * Short.MAX_VALUE).toInt().toShort()

              phase1 += 2 * Math.PI * freq1 / sampleRate
              if (phase1 > 2 * Math.PI) phase1 -= 2 * Math.PI
              phase2 += 2 * Math.PI * freq2 / sampleRate
              if (phase2 > 2 * Math.PI) phase2 -= 2 * Math.PI
            }
            ambientTrack?.write(shortBuffer, 0, shortBuffer.size)
            delay(10)
          }
        } catch (_: Exception) {
          // Graceful catch if audio track unavailable
        }
      }
  }

  private fun stopAmbientDrone() {
    ambientJob?.cancel()
    ambientJob = null
    try {
      ambientTrack?.stop()
      ambientTrack?.release()
    } catch (_: Exception) {}
    ambientTrack = null
  }

  fun shutdown() {
    stop()
    try {
      tts?.shutdown()
    } catch (_: Exception) {}
    tts = null
  }
}
