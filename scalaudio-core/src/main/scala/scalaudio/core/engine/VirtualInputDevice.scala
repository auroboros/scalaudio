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

  val var1: DataLine.Info = new DataLine.Info(classOf[TargetDataLine], format)

  //  if (!AudioSystem.isLineSupported(var1)) JavaSoundAudioDevice.logger.severe("JavaSoundInputStream - not supported." + this.format)
  //  else try
  val line = getDataLine(var1, deviceId).asInstanceOf[TargetDataLine]
  val var2: Int = calculateBufferSize(0.1D, frameRate, samplesPerFrame)

  def startInput() = {
    line.open(format, var2)
    println("Input buffer size = " + var2 + " bytes.")
    line.start()
  }

  def stopInput() {
    if (line != null) {
      line.stop()
      line.flush()
      line.close()
      //      this.line = null
    }
    else new RuntimeException("AudioOutput stop attempted when no line created.").printStackTrace()
  }

  def read: Double = {
    val var1: Array[Double] = new Array[Double](1)
    read(var1, 0, 1)
    var1(0)
  }

  def read(var1: Array[Double]): Int = read(var1, 0, var1.length)

  def read(var1: Array[Double], var2: Int, var3: Int): Int = {
    if (bytes == null || bytes.length * 2 < var3) bytes = new Array[Byte](var3 * 2)
    val var4: Int = line.read(bytes, 0, bytes.length)
    var var5: Int = 0
    var var6: Int = 0
    while (var6 < var3) {
      var var7: Int = bytes({
        var5 += 1;
        var5 - 1
      }) & 255
      var7 += bytes({
        var5 += 1;
        var5 - 1
      }) << 8
      var1(var6 + var2) = var7.toDouble * 3.051850947599719E-5D

      var6 += 1
    }
    var4 / 4
  }
}
