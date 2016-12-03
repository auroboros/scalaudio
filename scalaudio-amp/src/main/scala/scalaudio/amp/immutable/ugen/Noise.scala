package scalaudio.amp.immutable.ugen

/**
  * Created by johnmcgill on 6/16/16.
  */
object Noise {
  def generateSample = Math.random() * 2 - 1

  def generateFrame(nChannels: Int) = Array.fill(nChannels)(generateSample)
}