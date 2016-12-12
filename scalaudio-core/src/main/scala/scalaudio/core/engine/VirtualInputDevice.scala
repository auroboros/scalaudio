package scalaudio.core.engine

import javax.sound.sampled.{AudioFormat, DataLine, TargetDataLine}

/**
  * Created by johnmcgill on 12/7/16.
  */
case class VirtualInputDevice(frameRate: Int,
                              samplesPerFrame: Int,
                              deviceId: Int = ScalaudioDeviceManager.defaultInputDeviceID) {

  import VirtualDevices._

  var bytes: Array[Byte] = _

  val format = new AudioFormat(frameRate.toFloat, 16, samplesPerFrame, true, false)

  val desiredTargetLineType: DataLine.Info = new DataLine.Info(classOf[TargetDataLine], format)

  //  if (!AudioSystem.isLineSupported(desiredTargetLineType)) JavaSoundAudioDevice.logger.severe("JavaSoundInputStream - not supported." + this.format)
  //  else try
  val targetLine = getDataLine(desiredTargetLineType, deviceId).asInstanceOf[TargetDataLine]
  val bufferSize: Int = calculateBufferSize(0.1D, frameRate, samplesPerFrame)

  def startInput() = {
    targetLine.open(format, bufferSize)
    println("Input buffer size = " + bufferSize + " bytes.")
    targetLine.start()
  }

  def stopInput() {
    if (targetLine != null) {
      targetLine.stop()
      targetLine.flush()
      targetLine.close()
      //      this.line = null
    }
    else new RuntimeException("AudioOutput stop attempted when no line created.").printStackTrace()
  }

  def read: Double = {
    val buffer: Array[Double] = new Array[Double](1)
    read(buffer, 0, 1)
    buffer(0)
  }

  def read(buffer: Array[Double]): Int = read(buffer, 0, buffer.length)

  def read(buffer: Array[Double], start: Int, count: Int): Int = {
    // Allocate byte buffer if needed.
    if ((bytes == null) || ((bytes.length * 2) < count)) bytes = new Array[Byte](count * 2)
    val bytesRead: Int = targetLine.read(bytes, 0, bytes.length)

    // Convert BigEndian bytes to float samples
    (0 until count) foreach {i =>
      var sample: Int = bytes(i*2) & 0x00FF // little end
      sample = sample + (bytes((i*2)+1) << 8) // big end
      buffer(i + start) = sample * (1.0 / 32767.0)
    }

    bytesRead / 4
  }
}
