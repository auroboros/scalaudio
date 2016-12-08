package scalaudio.core.engine

import javax.sound.sampled.{AudioFormat, AudioSystem, DataLine, Line}
import javax.sound.sampled.Mixer.Info

/**
  * Created by johnmcgill on 12/7/16.
  */
object ScalaudioDeviceManager {
  private def scanMaxChannels(var1: Array[Line.Info]): Int = {
    var var2: Int = 0
    val var3: Array[Line.Info] = var1
    val var4: Int = var1.length
    var var5: Int = 0
    while (var5 < var4) {
      val var6: Line.Info = var3(var5)
      if (var6.isInstanceOf[DataLine.Info]) {
        val var7: Int = scanDataLineMaxChannels(var6.asInstanceOf[DataLine.Info])
        if (var7 > var2) var2 = var7
      }

      var5 += 1
    }
    var2
  }

  private def scanDataLineMaxChannels(var1: DataLine.Info): Int = {
    var var2: Int = 0
    val var3: Array[AudioFormat] = var1.getFormats
    val var4: Int = var3.length
    var var5: Int = 0
    while (var5 < var4) {
      val var6: AudioFormat = var3(var5)
      val var7: Int = var6.getChannels
      if (var7 > var2) var2 = var7

      var5 += 1
    }
    var2
  }

  val infos: Array[Info] = AudioSystem.getMixerInfo

  var defaultInputDeviceID = -1
  var defaultOutputDeviceID = -1

  infos.zipWithIndex.foreach {
    case (info, index) =>
      println(s"$index. ${info.getName}")

      val mixer = AudioSystem.getMixer(infos(index))

      val targetLineInfo = mixer.getTargetLineInfo()
      val maxInputs = scanMaxChannels(targetLineInfo)
      if (defaultInputDeviceID < 0 && maxInputs > 0) defaultInputDeviceID = index

      val sourceLineInfo = mixer.getSourceLineInfo()
      val maxOutputs = scanMaxChannels(sourceLineInfo)
      if (defaultOutputDeviceID < 0 && maxOutputs > 0) defaultOutputDeviceID = index

      println(s"max inputs: $maxInputs, max outputs: $maxOutputs")
  }
}
