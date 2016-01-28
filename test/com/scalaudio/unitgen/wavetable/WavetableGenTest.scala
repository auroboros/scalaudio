package com.scalaudio.unitgen.wavetable

import com.scalaudio.AudioContext
import com.scalaudio.engine.AudioTimepiece
import com.scalaudio.filter.mix.Splitter
import com.scalaudio.syntax.ScalaudioSyntaxHelpers
import com.scalaudio.unitgen.FuncGen
import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by johnmcgill on 1/25/16.
  */
class WavetableGenTest extends FlatSpec with Matchers with ScalaudioSyntaxHelpers {
  "Wavetable" should "play sample at original speed" in {
      implicit val audioContext = AudioContext()

    val wtGen = new WavetableGen(FileSample("/Users/johnmcgill/nocode/samples/Yamaha-TG100-Whistle-C5.wav")) with AudioTimepiece

//    val firstBuffer = wtGen.outputBuffers()
    wtGen.play(2000 buffers)
  }

  "Wavetable" should "play sample at double speed" in {
    implicit val audioContext = AudioContext() // PIECE: Try different values here

    val wtGen = new WavetableGen(FileSample("/Users/johnmcgill/nocode/samples/Yamaha-TG100-Whistle-C5.wav"), 2) with AudioTimepiece

    //    val firstBuffer = wtGen.outputBuffers()
    wtGen.play(2000 buffers)
  }

  "Wavetable" should "play sample at half speed" in {
    implicit val audioContext = AudioContext() // PIECE: Try different values here

    val wtGen = new WavetableGen(FileSample("/Users/johnmcgill/nocode/samples/Yamaha-TG100-Whistle-C5.wav"), .5) with AudioTimepiece

    //    val firstBuffer = wtGen.outputBuffers()
    wtGen.play(2000 buffers)
  }

  "Wavetable" should "play a sawtooth" in {
    implicit val audioContext = AudioContext() // PIECE: Try different values here

    val wtGen = new WavetableGen(Sawtooth(), 440)
    val splitter = Splitter(2)
    val funcGen = new FuncGen(() => {wtGen.outputBuffers() feed splitter.processBuffers}) with AudioTimepiece

    //    val firstBuffer = wtGen.outputBuffers()
    funcGen.play(2000 buffers)
  }

  "Wavetable" should "play a sine" in {
    implicit val audioContext = AudioContext() // PIECE: Try different values here

    val wtGen = new WavetableGen(Sine(), 440)
    val splitter = Splitter(2)
    val funcGen = new FuncGen(() => {wtGen.outputBuffers() feed splitter.processBuffers}) with AudioTimepiece

    //    val firstBuffer = wtGen.outputBuffers()
    funcGen.play(2000 buffers)
  }

  "Wavetable" should "play a square" in {
    implicit val audioContext = AudioContext() // PIECE: Try different values here

    val wtGen = new WavetableGen(Square(), 440)
    val splitter = Splitter(2)
    val funcGen = new FuncGen(() => {wtGen.outputBuffers() feed splitter.processBuffers}) with AudioTimepiece

    //    val firstBuffer = wtGen.outputBuffers()
    funcGen.play(2000 buffers)
  }
}
