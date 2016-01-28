package com.scalaudio.filter

import com.scalaudio.AudioContext
import com.scalaudio.engine.BufferComputer

/**
  * Created by johnmcgill on 12/19/15.
  */
trait Filter extends BufferComputer {
  def processBuffers(inBuffers : List[Array[Double]]) : List[Array[Double]]
}

trait ControllableFilter extends Filter {
  def processBuffersWithSignal(inBuffers : List[Array[Double]], controlSigBuffers : List[Array[Double]]) : List[Array[Double]]

  def processBuffersWithControl(inBuffers : List[Array[Double]], controlVal : Double) : List[Array[Double]]
}

trait SampleIndependentControllableFilter extends ControllableFilter {
  def defaultCtrlParam : Double

  def processSample(sig : Double, ctrl : Double): Double

  final override def processBuffers(inBuffers : List[Array[Double]]) : List[Array[Double]] =
    processBuffersWithControl(inBuffers, defaultCtrlParam)

  final override def processBuffersWithSignal(inBuffers : List[Array[Double]], controlSigBuffers : List[Array[Double]]) : List[Array[Double]] =
    if (controlSigBuffers.size == 1) {
      inBuffers map (b => (b, controlSigBuffers.head).zipped map processSample)
    } else if (inBuffers.size == controlSigBuffers.size) {
      val temp: List[(Array[Double], Array[Double])] = (inBuffers, controlSigBuffers).zipped.toList
      val bufferTuples: List[Array[(Double, Double)]] = temp.map{case (sigBuff : Array[Double],ctrlBuff: Array[Double]) => (sigBuff,ctrlBuff).zipped.toArray}
      bufferTuples.map(_ map {case (sigSample : Double, ctrlSample : Double) => processSample(sigSample, ctrlSample)})
    } else {
      throw new Exception(s"Control buffer size must be 1 or same as inBuffers List. InBuff: {$inBuffers.size}, Control {$controlSigBuffers.size}")
    }

  // TODO: This control val & maybe also gain in constructor should be given as List (gain vals for each channel or single that is applied to all)
  // Maybe there is some even better generic way to expand signal if necessary so that this 1 applies to all or value for each channel pattern
  // can be replicated across many filters?
  final override def processBuffersWithControl(inBuffers : List[Array[Double]], controlVal : Double) : List[Array[Double]] =
    inBuffers map (_ map (s => processSample(s,controlVal)))
}