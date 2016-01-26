package com.scalaudio.unitgen.wavetable

import com.scalaudio.{ScalaudioConfig, AudioContext}
import com.scalaudio.engine.AudioTimepiece
import com.scalaudio.syntax.ScalaudioSyntaxHelpers
import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by johnmcgill on 1/25/16.
  */
class WavetableGenTest extends FlatSpec with Matchers with ScalaudioSyntaxHelpers {
  "Wavetable" should "play at original speed" in {
      implicit val audioContext = AudioContext()

    val wtGen = new WavetableGen(FileSample("/Users/johnmcgill/nocode/samples/Yamaha-TG100-Whistle-C5.wav")) with AudioTimepiece

//    val firstBuffer = wtGen.outputBuffers()
    wtGen.play(2000 buffers)
  }

  "Wavetable" should "play at double speed" in {
    implicit val audioContext = AudioContext() // PIECE: Try different values here

    val wtGen = new WavetableGen(FileSample("/Users/johnmcgill/nocode/samples/Yamaha-TG100-Whistle-C5.wav"), 2) with AudioTimepiece // PIECE : this glitch

    //    val firstBuffer = wtGen.outputBuffers()
    wtGen.play(2000 buffers)
  }
}
