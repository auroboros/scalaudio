package scalaudio.core

import javax.sound.sampled.Mixer.Info
import javax.sound.sampled._

import org.scalatest.{FlatSpec, Matchers}

import scalaudio.core.engine.ScalaudioDeviceManager

/**
  * Created by johnmcgill on 12/7/16.
  */
class AudioSystemTest extends FlatSpec with Matchers {

  "a" should "b" in {
    val infos: Array[Info] = AudioSystem.getMixerInfo

    var defaultInputDeviceID = -1
    var defaultOutputDeviceID = -1

    infos.zipWithIndex.foreach {
      case (info, index) =>
        println(s"$index. ${info.getName}")

        val mixer = AudioSystem.getMixer(infos(index))

        val targetLineInfo = mixer.getTargetLineInfo()
        val maxInputs = ScalaudioDeviceManager.scanMaxChannels(targetLineInfo)
        if (defaultInputDeviceID < 0 && maxInputs > 0) defaultInputDeviceID = index

        val sourceLineInfo = mixer.getSourceLineInfo()
        val maxOutputs = ScalaudioDeviceManager.scanMaxChannels(sourceLineInfo)
        if (defaultOutputDeviceID < 0 && maxOutputs > 0) defaultOutputDeviceID = index

        println(s"max inputs: $maxInputs, max outputs: $maxOutputs")

    }
  }
}
