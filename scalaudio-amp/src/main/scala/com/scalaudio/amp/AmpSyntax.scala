package com.scalaudio.amp

import com.scalaudio.amp.immutable.control.AdsrEnvelope
import com.scalaudio.core.types.Pitch

/**
  * Created by johnmcgill on 5/31/16.
  */
trait AmpSyntax {
  // Synth note helper
  implicit def note2NoteList(note: (Pitch, AdsrEnvelope)): List[(Pitch, AdsrEnvelope)] = List(note)
}
