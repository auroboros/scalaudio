package org.jzy3d.demos

import org.jzy3d.chart.Chart

trait IDemo {
  def getName: String

  def getPitch: String

  def getChart: Chart

  def init()

  def isInitialized: Boolean
}