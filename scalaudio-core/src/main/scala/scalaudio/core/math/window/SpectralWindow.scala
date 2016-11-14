package scalaudio.core.math.window

/**
  * Created by johnmcgill on 1/12/16.
  */
trait SpectralWindow {
  def window : Array[Double]
}