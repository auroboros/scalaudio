package com.scalaudio.unitgen

import java.io.File

import com.jsyn.data.FloatSample
import com.jsyn.util.SampleLoader
import com.scalaudio.Config
import com.scalaudio.jsyn.util.{DoubleSample, AdaptedJavaSoundSampleLoader}

/**
  * Created by johnmcgill on 1/5/16.
  */
case class Sampler(filenames : List[String]) extends UnitGen {
  // Using collection breakout is supposed to prevent double-traversal
  val soundSamples : Map[String, DoubleSample] = filenames.map(s => (s -> AdaptedJavaSoundSampleLoader.loadDoubleSample(new File(s))))(collection.breakOut): Map[String, DoubleSample]
  var currentIndex = 0

  // TODO: Hm... how to read & how to read if its a diff sample rate (interpolation)
//  soundSample.

  // Can probably use this to pull out buffer or should just make new implementation of FloatSample...
  //public void read(float[] data) { read(0, data, 0, data.length / getChannelsPerFrame()); }
  // OR... can actualy use JSyn's API

  override def computeBuffer : List[Array[Double]] = {
    val ds : DoubleSample = soundSamples.head._2
    (0 to ds.audioBuffers.size - 1).toList.map { (channel: Int) =>
      (0 to Config.FramesPerBuffer - 1).toArray map {frame =>
        val sample : Double = ds.audioBuffers(channel)(currentIndex)
        currentIndex = (currentIndex + 1) % ds.length
        sample
      }
    }
  }
}