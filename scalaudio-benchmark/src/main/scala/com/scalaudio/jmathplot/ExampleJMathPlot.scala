package com.scalaudio.jmathplot

import javax.swing.JFrame

import org.math.plot.Plot2DPanel

/**
  * Created by johnmcgill on 6/15/16.
  */
object ExampleJMathPlot extends App {
  val x = Array(.1, .2, .3, .4, .5, .6, .7, .8, .9, 1)
  val y = Array(.3, .32, .31, .33, .34, .32, .33, .21, .42, .31)

  // create your PlotPanel (you can use it as a JPanel)
  val plot = new Plot2DPanel()

  // add a line plot to the PlotPanel
  plot.addLinePlot("my plot", x, y)

  // put the PlotPanel in a JFrame, as a JPanel
  val frame = new JFrame("a plot panel")
  frame.setContentPane(plot)
  frame.setBounds(500,200,750,500)
  frame.setVisible(true)
}
