package com.scalaudio.core.io

import com.jsyn.devices.javasound.JavaSoundAudioDevice
import com.jsyn.devices.{AudioDeviceManager, AudioDeviceOutputStream}
import com.scalaudio.core.syntax.ScalaudioSyntaxHelpers
import com.scalaudio.core.unitgen.{NoiseGen, SineGen}
import com.scalaudio.core.{AudioContext, ScalaudioConfig}
import org.scalatest.{FlatSpec, Matchers}

class AudioOutputTest extends FlatSpec with Matchers with ScalaudioSyntaxHelpers {

  "Audio Output" should "play buffers" in {
    System.out.println("Test mono output.")
    val FRAMES_PER_BUFFER: Int = 128
    val SAMPLES_PER_FRAME: Int = 1
    val buffer: Array[Double] = new Array[Double](FRAMES_PER_BUFFER * SAMPLES_PER_FRAME)
    val audioDevice: AudioDeviceManager = new JavaSoundAudioDevice
    val audioOutput: AudioDeviceOutputStream = audioDevice.createOutputStream(audioDevice.getDefaultOutputDeviceID, 44100, SAMPLES_PER_FRAME)

    val noiseBuffer = buffer.map(x => Math.random * 2 - 1)
    val sinBuffer = buffer.zipWithIndex.map{case (x, i) => Math.sin(i * Math.PI * 2.0 / FRAMES_PER_BUFFER)}

//    buffer.zipWithIndex map ((x : Double,i : Int) => (Math.sin(i * Math.PI * 2.0) / FRAMES_PER_BUFFER))

    audioOutput.start()

    1 to 1000 foreach {_ => audioOutput.write(sinBuffer)} // Use buffer.map(x => Math.random * 2 - 1) here to get actual noise
    1 to 1000 foreach {_ => audioOutput.write(noiseBuffer)}
    1 to 1000 foreach {_ => audioOutput.write(sinBuffer.zip(noiseBuffer).map{case (s,n) => s + n})}
    // (otherwise there is some periodicity resulting from buffer size)

    audioOutput.stop()
  }

  "Noise Generator" should "create buffer of random noise on every call" in {
    implicit val audioContext = AudioContext(ScalaudioConfig())

    val noiseGen = NoiseGen()
    noiseGen.play(1000.buffers)
  }

  "Sine Generator" should "create buffer of sine on every call" in {
    implicit val audioContext = AudioContext(ScalaudioConfig(nOutChannels = 1))

    val sineGen = SineGen(220 Hz)
    sineGen.play(1000 buffers)
  }

//  "Signal chain" should "play noise" in {
//    val chain = SignalChain(List(new NoiseGen))
//    chain.play
//    Thread.sleep(5000)
//    AudioContext.audioOutput.stop
//  }
}