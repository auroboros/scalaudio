package scalaudio.jmathplot.examples

import scalaudio.amp.immutable.ugen.{OscState, SineStateGen}
import scalaudio.core.{CoreSyntax, DefaultAudioContext}
import scalaudio.jmathplot.ConvenientPlot

/**
  * Created by johnmcgill on 6/15/16.
  */
object ScanLeftChainPlot extends App with CoreSyntax with DefaultAudioContext {

  val initOscState = OscState(0, 440.Hz, 0)

  val samples = (1 to 300).scanLeft(initOscState){(curr, acc) =>
    SineStateGen.nextState(curr)
  }.map(_.sample * 2)

  ConvenientPlot.plot2d((1 to samples.length).map(_.toDouble).toArray, samples.toArray)
}