package org.jzy3d.demos

import org.jzy3d.chart.{ChartLauncher, Settings}
import org.jzy3d.maths.Rectangle

object Launcher {
  def openDemo(demo: IDemo) {
    openDemo(demo, DEFAULT_WINDOW)
  }

  def openDemo(demo: IDemo, rectangle: Rectangle) {
    Settings.getInstance.setHardwareAccelerated(true)
    demo.init()
    val chart = demo.getChart
    ChartLauncher.instructions()
    ChartLauncher.openChart(chart, rectangle, demo.getName)
  }

  def openStaticDemo(demo: IDemo) {
    openStaticDemo(demo, DEFAULT_WINDOW)
  }

  def openStaticDemo(demo: IDemo, rectangle: Rectangle) {
    Settings.getInstance.setHardwareAccelerated(true)
    val chart = demo.getChart
    ChartLauncher.openStaticChart(chart, rectangle, demo.getName)
    ChartLauncher.screenshot(demo.getChart, "./data/screenshots/" + demo.getName + ".png")
  }

  def openStaticSWTDemo(demo: IDemo) {
//    Settings.getInstance.setHardwareAccelerated(true)
//    val chart = demo.getChart
//    val display = new Display
//    val shell = new Shell(display)
//    shell.setLayout(new FillLayout)
//    Bridge.adapt(shell, chart.getCanvas.asInstanceOf[Component])
//    shell.setText(demo.getName)
//    shell.setSize(800, 600)
//    shell.open()
//    while (!shell.isDisposed) {
//      if (!display.readAndDispatch) display.sleep
//    }
//    display.dispose()
  }

  protected var DEFAULT_CANVAS_TYPE: String = "awt"
  protected var DEFAULT_WINDOW: Rectangle = new Rectangle(0, 0, 600, 600)
}