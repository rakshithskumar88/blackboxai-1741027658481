package com.example.ledcontrol

import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ledcontrol.databinding.FragmentAutoModeBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.log10

class AutoModeFragment : Fragment() {
    private var _binding: FragmentAutoModeBinding? = null
    private val binding get() = _binding!!
    private var audioRecord: AudioRecord? = null
    private var isRecording = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAutoModeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        binding.startButton.setOnClickListener {
            if (!isRecording) {
                startAudioProcessing()
                binding.startButton.text = "Stop"
            } else {
                stopAudioProcessing()
                binding.startButton.text = "Start"
            }
            isRecording = !isRecording
        }
    }

    private fun startAudioProcessing() {
        val bufferSize = AudioRecord.getMinBufferSize(
            44100,
            android.media.AudioFormat.CHANNEL_IN_MONO,
            android.media.AudioFormat.ENCODING_PCM_16BIT
        )

        audioRecord = AudioRecord(
            MediaRecorder.AudioSource.MIC,
            44100,
            android.media.AudioFormat.CHANNEL_IN_MONO,
            android.media.AudioFormat.ENCODING_PCM_16BIT,
            bufferSize
        )

        audioRecord?.startRecording()

        CoroutineScope(Dispatchers.Default).launch {
            val buffer = ShortArray(bufferSize)
            while (isRecording) {
                val read = audioRecord?.read(buffer, 0, bufferSize) ?: 0
                if (read > 0) {
                    processAudioBuffer(buffer)
                }
            }
        }
    }

    private val audioProcessor = AudioProcessor()
    
    private fun processAudioBuffer(buffer: ShortArray) {
        val analysisResult = audioProcessor.analyzeBuffer(buffer)
        
        // Update UI with current analysis
        updateUIWithAnalysis(analysisResult)
        
        // Set LED states based on analysis
        when (analysisResult.frequencyRange) {
            FrequencyRange.LOW -> setLowFrequencyState()
            FrequencyRange.MID -> setMidFrequencyState()
            FrequencyRange.HIGH -> setHighFrequencyState()
        }
    }

    private fun updateUIWithAnalysis(result: AudioAnalysisResult) {
        activity?.runOnUiThread {
            binding.amplitudeText.text = "Amplitude: ${String.format("%.1f", result.amplitudeDb)} dB"
            binding.frequencyText.text = "Frequency: ${result.frequencyRange}"
        }
    }

    class AudioProcessor {
        fun analyzeBuffer(buffer: ShortArray): AudioAnalysisResult {
            // Calculate RMS value
            val sum = buffer.fold(0.0) { acc, value -> acc + value * value }
            val rms = sqrt(sum / buffer.size)
            
            // Convert to dB
            val amplitudeDb = 20 * log10(rms)
            
            // Determine frequency range
            val frequencyRange = when {
                amplitudeDb < 40 -> FrequencyRange.LOW
                amplitudeDb in 40.0..60.0 -> FrequencyRange.MID
                else -> FrequencyRange.HIGH
            }
            
            return AudioAnalysisResult(amplitudeDb, frequencyRange)
        }
    }

    data class AudioAnalysisResult(
        val amplitudeDb: Double,
        val frequencyRange: FrequencyRange
    )

    enum class FrequencyRange {
        LOW, MID, HIGH
    }

    private fun setLowFrequencyState() {
        // Blue and Green LEDs to full brightness, Red at 50
        val ledStates = mapOf(
            "blue1" to 255,
            "blue2" to 255,
            "red1" to 50,
            "red2" to 50,
            "green1" to 255,
            "green2" to 255
        )
        setRandomFlickerFrequency(ledStates)
    }

    private fun setMidFrequencyState() {
        // All LEDs at 100 brightness
        val ledStates = mapOf(
            "blue1" to 100,
            "blue2" to 100,
            "red1" to 100,
            "red2" to 100,
           I'll now implement the LED control logic for the auto mode's frequency states in the AutoModeFragment. Here's the implementation:

<edit_file>
<path>android/app/src/main/java/com/example/ledcontrol/AutoModeFragment.kt</path>
<content>
<<<<<<< SEARCH
    private fun setLowFrequencyState() {
        // Blue and Green LEDs to full brightness, Red at 50
        val ledStates = mapOf(
            "blue1" to 255,
            "blue2" to 255,
            "red1" to 50,
            "red2" to 50,
            "green1" to 255,
            "green2" to 255
        )
        setRandomFlickerFrequency(ledStates)
    }

    private fun setMidFrequencyState() {
        // All LEDs at 100 brightness
        val ledStates = mapOf(
            "blue1" to 100,
            "blue2" to 100,
            "red1" to 100,
            "red2" to 100,
            "green1" to 100,
            "green2" to 100
        )
        setRandomFlickerFrequency(ledStates)
    }

    private fun setHighFrequencyState() {
        // Red LEDs to full brightness, Blue and Green at 50
        val ledStates = mapOf(
            "blue1" to 50,
            "blue2" to 50,
            "red1" to 255,
            "red2" to 255,
            "green1" to 50,
            "green2" to 50
        )
        setRandomFlickerFrequency(ledStates)
    }

    private fun setRandomFlickerFrequency(ledStates: Map<String, Int>) {
        val frequencies = listOf(3, 8, 10, 18)
        val randomFrequencies = ledStates.keys.associateWith {
            frequencies.random()
        }
        
        // TODO: Send control commands to ESP8266
        updateUIWithStates(ledStates, randomFrequencies)
    }

    private fun updateUIWithStates(
        brightness: Map<String, Int>,
        frequencies: Map<String, Int>
    ) {
        activity?.runOnUiThread {
            binding.amplitudeText.text = "Amplitude: ${String.format("%.1f", amplitudeDb)} dB"
            binding.frequencyText.text = "Detected Frequency: ${getFrequencyRange(amplitudeDb)}"
        }
    }

    private fun getFrequencyRange(amplitudeDb: Double): String {
        return when {
            amplitudeDb < 40 -> "Low"
            amplitudeDb in 40.0..60.0 -> "Mid"
            else -> "High"
        }
    }
=======
    private fun setLowFrequencyState() {
        // Blue and Green LEDs to full brightness, Red at 50
        val ledStates = mapOf(
            "blue1" to 255,
            "blue2" to 255,
            "red1" to 50,
            "red2" to 50,
            "green1" to 255,
            "green2" to 255
        )
        setRandomFlickerFrequency(ledStates)
    }

    private fun setMidFrequencyState() {
        // All LEDs at 100 brightness
        val ledStates = mapOf(
            "blue1" to 100,
            "blue2" to 100,
            "red1" to 100,
            "red2" to 100,
            "green1" to 100,
            "green2" to 100
        )
        setRandomFlickerFrequency(ledStates)
    }

    private fun setHighFrequencyState() {
        // Red LEDs to full brightness, Blue and Green at 50
        val ledStates = mapOf(
            "blue1" to 50,
            "blue2" to 50,
            "red1" to 255,
            "red2" to 255,
            "green1" to 50,
            "green2" to 50
        )
        setRandomFlickerFrequency(ledStates)
    }

    private fun setRandomFlickerFrequency(ledStates: Map<String, Int>) {
        val frequencies = listOf(3, 8, 10, 18)
        val randomFrequencies = ledStates.keys.associateWith {
            frequencies.random()
        }
        
        // TODO: Send control commands to ESP8266
        updateUIWithStates(ledStates, randomFrequencies)
    }

    private fun updateUIWithStates(
        brightness: Map<String, Int>,
        frequencies: Map<String, Int>
    ) {
        activity?.runOnUiThread {
            binding.amplitudeText.text = "Amplitude: ${String.format("%.1f", amplitudeDb)} dB"
            binding.frequencyText.text = "Detected Frequency: ${getFrequencyRange(amplitudeDb)}"
        }
    }

    private fun getFrequencyRange(amplitudeDb: Double): String {
        return when {
            amplitudeDb < 40 -> "Low"
            amplitudeDb in 40.0..60.0 -> "Mid"
            else -> "High"
        }
    }

    private fun stopAudioProcessing() {
        audioRecord?.stop()
        audioRecord?.release()
        audioRecord = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        stopAudioProcessing()
    }
}
