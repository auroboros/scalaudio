package scalaudio.core.math

import org.apache.commons.math3.transform.{DftNormalization, FastFourierTransformer, TransformType}
import org.scalatest.{FlatSpec, Matchers}

import scalaudio.core.{AudioContext, ScalaudioConfig}

/**
  * Created by johnmcgill on 6/5/16.
  */
class FftTest extends FlatSpec with Matchers {
  "Apache FFT" should "works its lil bunz off to get me a spectrum" in {

    // TODO: Put in buffer unit tests?

//    implicit val audioContext = AudioContext(ScalaudioConfig())
//
//    val ffter = new FastFourierTransformer(DftNormalization.STANDARD)
//    val sg = SineGen()
//
//    val complexOut = ffter.transform(sg.outputBuffers().head, TransformType.FORWARD)
//    println(complexOut)
  }
}
