package scalaudio.amp

import scalaudio.units.control.AdsrEnvelope
import scalaudio.core.types.Pitch

/**
  * Created by johnmcgill on 5/31/16.
  */
trait AmpSyntax {
  // Synth note helper // TODO: Can put in some companion that will allow it to be found instead of syntax helper?
  implicit def note2NoteList(note: (Pitch, AdsrEnvelope)): List[(Pitch, AdsrEnvelope)] = List(note)
}
