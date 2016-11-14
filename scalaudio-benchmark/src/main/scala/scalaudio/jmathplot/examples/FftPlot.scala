package scalaudio.jmathplot.examples

import com.scalaudio.amp.immutable.ugen.{OscState, SineStateGen}
import com.scalaudio.core.math.FftMath
import com.scalaudio.core.{AudioContext, CoreSyntax, ScalaudioConfig}

import scala.collection.mutable.ListBuffer
import scalaudio.jmathplot.ConvenientPlot

/**
  * Created by johnmcgill on 6/15/16.
  */
object FftPlot extends App with CoreSyntax {

  val experimentFftSize = 2048

  // make play while, and play until 1 frame is recieved?
  implicit val audioContext : AudioContext = AudioContext(ScalaudioConfig(fftSize = experimentFftSize))

  var oscState = OscState(0, 10000.Hz, 0)
  val yVals = ListBuffer.empty[Double]

  1 to experimentFftSize foreach {_ =>
    yVals += oscState.sample
    oscState = SineStateGen.nextState(oscState)
  }

  import FftMath._

  val spectrum = powerSpectrumFromFft(performFFT(yVals.toArray))

  val x = (1 to spectrum.length).map(_.toDouble).toArray

  ConvenientPlot.plot2d(x, spectrum)
}