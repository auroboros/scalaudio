package scalaudio.core.engine

import javax.sound.sampled._
import java.lang.{Short => JShort}

/**
  * Created by johnmcgill on 12/7/16.
  *
  * ReviewNote: This is based on JSyn's "JavaSoundAudioDevice" so parity should be checked periodically
  */
case class VirtualOutputDevice(frameRate: Int,
                               samplesPerFrame: Int,
                               deviceId: Int = ScalaudioDeviceManager.defaultOutputDeviceID) {

  import VirtualDevices._

  var bytes: Array[Byte] = _

  val format = new AudioFormat(frameRate.toFloat, 16, samplesPerFrame, true, false)

  val desiredLineType: DataLine.Info = new DataLine.Info(classOf[SourceDataLine], format)

  //  if (!AudioSystem.isLineSupported(desiredLineType)) println("JavaSoundOutputStream - not supported." + format)
  //  else {
  val sourceLine = getDataLine(desiredLineType, deviceId).asInstanceOf[SourceDataLine]
  val bufferSize: Int = calculateBufferSize(0.1D, frameRate, samplesPerFrame)

  //TODO: How was suggested latency calculated?
  def startOutput() = {

    // thisJavaSoundAudioDevice.suggestedOutputLatency)
    sourceLine.open(format, bufferSize)
    println("Output buffer size = " + bufferSize + " bytes.")
    sourceLine.start()
  }

  def write(value: Double) {
    val buffer: Array[Double] = Array[Double](value)
    write(buffer, 0, 1)
  }

  def write(buffer: Array[Double]) {
    write(buffer, 0, buffer.length)
  }

  def write(buffer: Array[Double], start: Int, count: Int) {
    // Allocate byte buffer if needed.
    if ((bytes == null) || ((bytes.length * 2) < count)) bytes = new Array[Byte](count * 2)

    // Convert float samples to LittleEndian bytes.
    var byteIndex: Int = 0

    (0 until count) foreach { i =>
        // Offset before casting so that we can avoid using floor().
        // Also round by adding 0.5 so that very small signals go to zero.
        val temp: Double = (32767.0 * buffer(i + start)) + 32768.5
        var sample: Int = temp.toInt - 32768
        if (sample > JShort.MAX_VALUE) sample = JShort.MAX_VALUE
        else if (sample < JShort.MIN_VALUE) sample = JShort.MIN_VALUE
        bytes(i*2) = sample.toByte // little end
        bytes((i*2)+1) = (sample >> 8).toByte // big end
    }

    sourceLine.write(bytes, 0, count * 2)
  }

  def stop() {
    if (sourceLine != null) {
      sourceLine.stop()
      sourceLine.flush()
      sourceLine.close()
//      this.line = null
    }
    else new RuntimeException("AudioOutput stop attempted when no line created.").printStackTrace()
  }
}

object VirtualDevices {
  //    TODO: See "JavaSoundOutputStream" in JavaSoundAudioDevice to see how to write

  @throws[LineUnavailableException]
  def getDataLine(desiredLineType: DataLine.Info, deviceId: Int): Line =
    if (deviceId >= 0) {
      val mixers: Array[Mixer.Info] = AudioSystem.getMixerInfo
      val selectedMixer: Mixer = AudioSystem.getMixer(mixers(deviceId))
      selectedMixer.getLine(desiredLineType)
    }
    else AudioSystem.getLine(desiredLineType)

  def calculateBufferSize(suggestedLatency: Double,
                          frameRate: Int,
                          samplesPerFrame: Int): Int =
    (suggestedLatency * frameRate.toDouble).toInt * samplesPerFrame * 2
}