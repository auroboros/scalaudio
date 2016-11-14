package scalaudio.core.math.window

/**
  * Created by johnmcgill on 1/12/16.
  *
  * Adapted from JSyn
  */
/** Traditional Hamming Window with alpha = 25/46 and beta = 21/46 */
class HammingWindow(val length : Int, val alpha : Double = 25.0/46.0, val beta : Double = 21.0/46.0) extends SpectralWindow {
  val window : Array[Double] = construct(length, alpha, beta)

  /** Construct a generalized Hamming Window */
  def construct(length: Int, alpha: Double, beta: Double) : Array[Double] = {
    val scaler: Double = 2.0 * Math.PI / (length - 1)
    (0 until length map (i => alpha - (beta * Math.cos(i * scaler)))).toArray
  }
}