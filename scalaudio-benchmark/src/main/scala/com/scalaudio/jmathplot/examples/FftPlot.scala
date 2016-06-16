package com.scalaudio.jmathplot.examples

import com.scalaudio.amp.immutable.ugen.{OscState, SineStateGen}
import com.scalaudio.core.math.FFTMath
import com.scalaudio.core.{AudioContext, CoreSyntax, ScalaudioConfig}
import com.scalaudio.jmathplot.ConvenientPlot

import scala.collection.mutable.ListBuffer

/**
  * Created by johnmcgill on 6/15/16.
  */
object FftPlot extends App with CoreSyntax {
  // make play while, and play until 1 frame is recieved?
  implicit val audioContext : AudioContext = AudioContext(ScalaudioConfig(fftBufferSize = 1024))

  var oscState = OscState(0, 10000.Hz, 0)
  val yVals = ListBuffer.empty[Double]

  1 to 1024 foreach {_ =>
    yVals += oscState.sample
    oscState = SineStateGen.nextState(oscState)
  }

  import FFTMath._

  val spectrum = powerSpectrumFromFft(performFFT(yVals.toArray))

  val x = (1 to spectrum.length).map(_.toDouble).toArray

  ConvenientPlot.plot2d(x, spectrum)
}