package scalaudio.jmathplot.examples

import com.scalaudio.jmathplot.ConvenientPlot

import scalaudio.jmathplot.ConvenientPlot

/**
  * Created by johnmcgill on 6/15/16.
  */
object ExampleJMathPlot extends App {
  val x = Array(.1, .2, .3, .4, .5, .6, .7, .8, .9, 1)
  val y = Array(.3, .32, .31, .33, .34, .32, .33, .21, .42, .31)

  ConvenientPlot.plot2d(x,y)
}