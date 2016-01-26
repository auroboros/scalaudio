package com.scalaudio.unitgen.wavetable

import com.scalaudio.{ScalaudioConfig, AudioContext}
import com.scalaudio.engine.AudioTimepiece
import com.scalaudio.syntax.ScalaudioSyntaxHelpers
import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by johnmcgill on 1/25/16.
  */
class WavetableGenTest extends FlatSpec with Matchers with ScalaudioSyntaxHelpers {
  "Wavetable" should "blah" in {
      implicit val audioContext = AudioContext(ScalaudioConfig(FramesPerBuffer = 2000)) // PIECE: Try different values here

    val wtGen = new WavetableGen(FileSample("/Users/johnmcgill/nocode/samples/Yamaha-TG100-Whistle-C5.wav")) with AudioTimepiece

//    val firstBuffer = wtGen.outputBuffers()
    wtGen.play(1000 buffers)
  }
}
