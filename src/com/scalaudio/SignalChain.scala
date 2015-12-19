package com.scalaudio

import com.scalaudio.unitgen.SigOut

/**
  * Created by johnmcgill on 12/19/15.
  */
case class SignalChain(val sigChain : List[SigOut]) {
  def play = {
    while (true) {
      AudioContext.audioOutput.write(sigChain(0).outputBuffer)
    }
  }
}