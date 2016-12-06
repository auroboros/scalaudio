package scalaudio.core.math

/**
  * Created by johnmcgill on 1/5/16.
  */
object Interleaver {
  def interleave(buffers : List[Array[Double]]) : Array[Double] = {
    val tBuffers = buffers.transpose
    tBuffers.tail.foldLeft(tBuffers.head)((r,c) => r ++ c).toArray
  }

  def deinterleave(buffer : Array[Double], nChannels : Int) : List[Array[Double]] = {
    buffer.grouped(nChannels).toArray.transpose.toList
  }
}