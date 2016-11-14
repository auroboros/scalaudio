package scalaudio.jmathplot.examples

import scala.collection.mutable.ListBuffer
import scalaudio.amp.immutable.ugen.{OscState, SineStateGen}
import scalaudio.core.math.window._
import scalaudio.core.{AudioContext, CoreSyntax, ScalaudioConfig}
import scalaudio.jmathplot.ConvenientPlot

/**
  * Created by johnmcgill on 6/16/16.
  */
object WindowPlot extends App with CoreSyntax {
  implicit val audioContext: AudioContext = AudioContext(ScalaudioConfig(fftSize = 1024))

  var oscState = OscState(0, 440.Hz, 0)
  val yVals = ListBuffer.empty[Double]

  1 to 700 foreach { _ =>
    yVals += oscState.sample
    oscState = SineStateGen.nextState(oscState)
  }

  val windowedYVals = pad(
    applyWindow(
      yVals.toArray,
      new HannWindow(yVals.length).window
    ),
    audioContext.config.fftSize
  )

  val x = (1 to windowedYVals.length).map(_.toDouble).toArray

  ConvenientPlot.plot2d(x, windowedYVals)
}
