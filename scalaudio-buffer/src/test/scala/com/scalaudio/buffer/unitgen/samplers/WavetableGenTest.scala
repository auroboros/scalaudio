package com.scalaudio.buffer.unitgen.samplers

import com.scalaudio.buffer.BufferSyntax
import com.scalaudio.buffer.filter.mix.Splitter
import com.scalaudio.buffer.unitgen.FuncGen
import com.scalaudio.core.AudioContext
import com.scalaudio.core.unitgen.samplers._
import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by johnmcgill on 1/25/16.
  */
class WavetableGenTest extends FlatSpec with Matchers with BufferSyntax {
  "Wavetable" should "play sample at original speed" in {
      implicit val audioContext = AudioContext()

    val wtGen = new WavetableGen(FileSample("/Users/johnmcgill/nocode/samples/Yamaha-TG100-Whistle-C5.wav"))

//    val firstBuffer = wtGen.outputBuffers()
    wtGen.play(2000 buffers)
  }

  "Wavetable" should "play sample at double speed" in {
    implicit val audioContext = AudioContext() // PIECE: Try different values here

    val wtGen = new WavetableGen(FileSample("/Users/johnmcgill/nocode/samples/Yamaha-TG100-Whistle-C5.wav"), 2)

    //    val firstBuffer = wtGen.outputBuffers()
    wtGen.play(2000 buffers)
  }

  "Wavetable" should "play sample at half speed" in {
    implicit val audioContext = AudioContext() // PIECE: Try different values here

    val wtGen = new WavetableGen(FileSample("/Users/johnmcgill/nocode/samples/Yamaha-TG100-Whistle-C5.wav"), .5)

    //    val firstBuffer = wtGen.outputBuffers()
    wtGen.play(2000 buffers)
  }

  "Wavetable" should "play a sawtooth" in {
    implicit val audioContext = AudioContext() // PIECE: Try different values here

    val wtGen = new WavetableGen(Sawtooth(), 440)
    val splitter = Splitter(2)
    val funcGen = new FuncGen(() => {wtGen.outputBuffers() chain splitter.processBuffers})

    //    val firstBuffer = wtGen.outputBuffers()
    funcGen.play(2000 buffers)
  }

  "Wavetable" should "play a sine" in {
    implicit val audioContext = AudioContext() // PIECE: Try different values here

    val wtGen = new WavetableGen(Sine(), 440)
    val splitter = Splitter(2)
    val funcGen = new FuncGen(() => {wtGen.outputBuffers() chain splitter.processBuffers})

    //    val firstBuffer = wtGen.outputBuffers()
    funcGen.play(2000 buffers)
  }

  "Wavetable" should "play a square" in {
    implicit val audioContext = AudioContext() // PIECE: Try different values here

    val wtGen = new WavetableGen(Square(), 440)
    val splitter = Splitter(2)
    val funcGen = new FuncGen(() => {wtGen.outputBuffers() chain splitter.processBuffers})

    //    val firstBuffer = wtGen.outputBuffers()
    funcGen.play(2000 buffers)
  }
}
