package com.scalaudio.core.midi

/**
  * Created by johnmcgill on 6/1/16.
  *
  * adapted from Jsyn
  */
object MidiParser {
  def parse(var1: Array[Byte]) : MidiCommand = {
    val var2: Byte = var1(0)
    val var3: Int = var2 & 240
    val var4: Int = var2 & 15
    var3 match {
      case 128 =>
        NoteOff(var4, var1(1), var1(2))
      case 144 =>
        val var5: Byte = var1(2)
        if (var5 == 0) {
          NoteOff(var4, var1(1), var5)
        } else {
          NoteOn(var4, var1(1), var5)
        }
      case 176 =>
        CtrlChange(var4, var1(1), var1(2))
      case 224 =>
        val var6: Int = ((var1(2) & 127) << 7) + (var1(1) & 127)
        PitchBend(var4, var6)
    }
  }
}
