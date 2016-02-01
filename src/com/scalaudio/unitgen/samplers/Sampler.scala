package com.scalaudio.unitgen.samplers

import java.io.File

import com.scalaudio.AudioContext
import com.scalaudio.jsyn.util.{AdaptedJavaSoundSampleLoader, DoubleSample}
import com.scalaudio.syntax.UnitParams
import com.scalaudio.unitgen.UnitGen
import com.scalaudio.unitgen.samplers.SamplerUtils._

/**
  * Created by johnmcgill on 1/5/16.
  */
case class Sampler(val rawSamples : List[WavetableType])(implicit audioContext: AudioContext) extends UnitGen {

  val soundSamples : Map[String, (SoundSample, SampleState)] = (rawSamples.zipWithIndex map {case (x,i) => (i.toString, (wavetableMode2Sample(x), new SampleState()))}).toMap

  override def computeBuffer(params : Option[UnitParams] = None) = {
    ???
//    val ds : DoubleSample = soundSamples.head._2
//    // TODO: Refactor for efficiency now that there is no return
//    internalBuffers = ds.audioBuffers.indices.toList.map { (channel: Int) =>
//      (0 until audioContext.config.FramesPerBuffer).toArray map {frame =>
//        val sample : Double = ds.audioBuffers(channel)(currentIndex)
//        currentIndex = (currentIndex + 1) % ds.length
//        sample
//      }
//    }
  }

  def activateSample(sampleId : String) =
    soundSamples.get(sampleId) foreach (_._2.active = true)
}

//TODO: incrementRate needs to be dynamically determined a la playbackRate * (sample.samplingFreq / audioContext.config.SamplingRate)
class SampleState(var incrementRate : Double = 1, var active : Boolean = false, var position : Int = 0)