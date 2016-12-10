package scalaudio.jmathplot.examples

import scala.collection.mutable.ListBuffer
import scalaudio.core.math.FftMath._
import scalaudio.core.{AudioContext, CoreSyntax, ScalaudioConfig}
import scalaudio.jmathplot.ConvenientPlot
import scalaudio.units.ugen.{OscState, ImmutableSine}
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
    oscState = ImmutableSine.nextState(oscState)
  }

  val spectrum = powerSpectrumFromFft(performFFT(yVals.toArray))

  val x = (1 to spectrum.length).map(_.toDouble).toArray

  ConvenientPlot.plot2d(x, spectrum)
}