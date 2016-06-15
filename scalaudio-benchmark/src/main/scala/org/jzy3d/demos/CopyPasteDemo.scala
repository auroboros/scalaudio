package org.jzy3d.demos

import org.jzy3d.chart.AWTChart
import org.jzy3d.colors.colormaps.ColorMapRainbow
import org.jzy3d.colors.{Color, ColorMapper}
import org.jzy3d.maths.Range
import org.jzy3d.plot3d.builder.concrete.OrthonormalGrid
import org.jzy3d.plot3d.builder.{Builder, Mapper}
import org.jzy3d.plot3d.rendering.canvas.Quality
/**
  * Created by johnmcgill on 6/15/16.
  */
object CopyPasteDemo extends App {
  // Define a function to plot
  val mapper = new Mapper() {
    override def f(x : Double, y : Double) : Double = {
      10 * Math.sin(x / 10) * Math.cos(y / 20)
    }
  }

  // Define range and precision for the function to plot
  val range = new Range(-150, 150)
  val steps = 50

  // Create a surface drawing that function
  val surface = Builder.buildOrthonormal(new OrthonormalGrid(range, steps, range, steps), mapper)
  surface.setColorMapper(new ColorMapper(new ColorMapRainbow(), surface.getBounds.getYRange))
  surface.setFaceDisplayed(true)
  surface.setWireframeDisplayed(false)
  surface.setWireframeColor(Color.BLACK)

  // Create a chart and add the surface
  val chart = new AWTChart(Quality.Advanced)
  chart.add(surface)
  chart.open("Jzy3d Demo", 600, 600)
}
