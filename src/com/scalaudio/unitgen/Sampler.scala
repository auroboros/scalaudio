package com.scalaudio.unitgen

import java.io.File

import com.jsyn.data.FloatSample
import com.jsyn.util.SampleLoader
import com.scalaudio.jsyn.util.{DoubleSample, AdaptedJavaSoundSampleLoader}

/**
  * Created by johnmcgill on 1/5/16.
  */
case class Sampler(filenames : List[String]) {
  val soundSamples : List[DoubleSample] = filenames.map(s => AdaptedJavaSoundSampleLoader.loadDoubleSample(new File(s)))

  // TODO: Hm... how to read & how to read if its a diff sample rate (interpolation)
//  soundSample.

  // Can probably use this to pull out buffer or should just make new implementation of FloatSample...
  //public void read(float[] data) { read(0, data, 0, data.length / getChannelsPerFrame()); }
  // OR... can actualy use JSyn's API
}