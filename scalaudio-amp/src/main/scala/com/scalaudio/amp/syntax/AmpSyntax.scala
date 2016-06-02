package com.scalaudio.amp.syntax

import com.scalaudio.amp.immutable.envelope.AdsrEnvelope
import com.scalaudio.core.syntax.Pitch

/**
  * Created by johnmcgill on 5/31/16.
  */
trait AmpSyntax {
  // Synth note helper
  implicit def note2NoteList(note: (Pitch, AdsrEnvelope)): List[(Pitch, AdsrEnvelope)] = List(note)
}
