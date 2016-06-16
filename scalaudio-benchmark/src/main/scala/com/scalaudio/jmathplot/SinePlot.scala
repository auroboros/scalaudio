package com.scalaudio.jmathplot

import javax.swing.JFrame

import com.scalaudio.amp.immutable.ugen.{OscState, SineStateGen}
import com.scalaudio.core.{AudioContext, CoreSyntax}
import org.math.plot.Plot2DPanel

import scala.collection.mutable.ListBuffer

/**
  * Created by johnmcgill on 6/15/16.
  */
object SinePlot extends App with CoreSyntax {
  implicit val audioContext : AudioContext = AudioContext()

  var oscState = OscState(0, 440.Hz, 0)
  val yVals = ListBuffer.empty[Double]

  1 to 300 foreach {_ =>
    yVals += oscState.sample
    oscState = SineStateGen.nextState(oscState)
  }

  val x = (1 to yVals.length).map(_.toDouble).toArray

  // create your PlotPanel (you can use it as a JPanel)
  val plot = new Plot2DPanel()

  // add a line plot to the PlotPanel
  plot.addLinePlot("my plot", x, yVals.toArray)

  // put the PlotPanel in a JFrame, as a JPanel
  val frame = new JFrame("a plot panel")
  frame.setContentPane(plot)
  frame.setBounds(500,200,750,500)
  frame.setVisible(true)
}
