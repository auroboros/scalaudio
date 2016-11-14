package scalaudio.jmathplot

import javax.swing.JFrame

import org.math.plot.Plot2DPanel

/**
  * Created by johnmcgill on 6/16/16.
  */
object ConvenientPlot {
  def plot2d(xVals: Array[Double], yVals: Array[Double]) = {
    // create your PlotPanel (you can use it as a JPanel)
    val plot = new Plot2DPanel()

    // add a line plot to the PlotPanel
    plot.addLinePlot("my plot", xVals, yVals.toArray)

    // put the PlotPanel in a JFrame, as a JPanel
    val frame = new JFrame("a plot panel")
    frame.setContentPane(plot)
    frame.setBounds(500, 200, 750, 500)
    frame.setVisible(true)
  }
}
