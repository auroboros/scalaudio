package scalaudio.core.engine

import javax.sound.sampled._

/**
  * Created by johnmcgill on 12/7/16.
  */
case class VirtualOutputDevice(deviceId: Int, frameRate: Int, samplesPerFrame: Int) {

  import VirtualOutputDevice._

  var bytes: Array[Byte] = _

  val format = new AudioFormat(frameRate.toFloat, 16, samplesPerFrame, true, false)

  val desiredLineType: DataLine.Info = new DataLine.Info(classOf[SourceDataLine], format)

  //  if (!AudioSystem.isLineSupported(desiredLineType)) println("JavaSoundOutputStream - not supported." + format)
  //  else {
  val line = getDataLine(desiredLineType, deviceId).asInstanceOf[SourceDataLine]
  val bufferSize: Int = calculateBufferSize(0.1D, frameRate, samplesPerFrame)

  //TODO: How was suggested latency calculated?
  def startOutput() = {

    // thisJavaSoundAudioDevice.suggestedOutputLatency)
    line.open(format, bufferSize)
    println("Output buffer size = " + bufferSize + " bytes.")
    line.start()
  }

  def write(var1: Double) {
    val var3: Array[Double] = Array[Double](var1)
    this.write(var3, 0, 1)
  }

  def write(var1: Array[Double]) {
    this.write(var1, 0, var1.length)
  }

  def write(var1: Array[Double], var2: Int, var3: Int) {
    if (this.bytes == null || this.bytes.length * 2 < var3) this.bytes = new Array[Byte](var3 * 2)
    var var4: Int = 0
    var var5: Int = 0
    while (var5 < var3) {
      {
        val var6: Double = 32767.0D * var1(var5 + var2) + 32768.5D
        var var8: Int = var6.toInt - '耀'
        if (var8 > 32767) var8 = 32767
        else if (var8 < -32768) var8 = -32768
        this.bytes({
          var4 += 1
          var4 - 1
        }) = var8.toByte
        this.bytes({
          var4 += 1
          var4 - 1
        }) = (var8 >> 8).toByte
      }
      {
        var5 += 1
        var5
      }
    }
    this.line.write(this.bytes, 0, var4)
  }

  def stop() {
    if (this.line != null) {
      this.line.stop()
      this.line.flush()
      this.line.close()
//      this.line = null
    }
    else new RuntimeException("AudioOutput stop attempted when no line created.").printStackTrace()
  }
}

object VirtualOutputDevice {
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