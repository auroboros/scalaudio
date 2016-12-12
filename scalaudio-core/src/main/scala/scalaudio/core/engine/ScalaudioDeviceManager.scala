package scalaudio.core.engine

import javax.sound.sampled.{AudioFormat, AudioSystem, DataLine, Line}
import javax.sound.sampled.Mixer.Info

/**
  * Created by johnmcgill on 12/7/16.
  */
object ScalaudioDeviceManager {

  val infos: Array[Info] = AudioSystem.getMixerInfo

  // TODO: This can be done immutably obv
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

  def scanMaxChannels(lines: Array[Line.Info]): Int =
    lines.map {
      case line: DataLine.Info => scanDataLineMaxChannels(line)
      case _ => 0
    }.reduceOption(_ max _).getOrElse(0)

  // ReviewNote: http://stackoverflow.com/questions/10922237/scala-min-max-with-optiont-for-possibly-empty-seq
  def scanDataLineMaxChannels(info: DataLine.Info): Int = {
    info.getFormats.map(_.getChannels).reduceOption(_ max _).getOrElse(0)
  }
}
