package scalaudio.amp.immutable.synth

import scala.collection.immutable.{SortedMap, TreeMap}
import scalaudio.amp.immutable.control.{AdsrEnvelope, EnvelopeState, EnvelopeStateGen, PointEnvelope}
import scalaudio.amp.immutable.ugen.{OscState, OscStateGen}
import scalaudio.core.types.{AudioDuration, Pitch, Sample}
import scalaudio.core.{AudioContext, CoreSyntax}

/**
  * Created by johnmcgill on 5/29/16.
  */
case class MonosynthState(sample: Sample,
                          oscState: OscState,
                          pitchEnvState: EnvelopeState,
                          adsrEnvState: EnvelopeState)

// TODO: Make realtime by having state have a copy method that accepts a note-on?

object MonosynthStateGen extends CoreSyntax {
  def decodeInitialState(notes: SortedMap[AudioDuration, (Pitch, AdsrEnvelope)] = TreeMap.empty[AudioDuration, (Pitch, AdsrEnvelope)])
                        (implicit audioContext: AudioContext): MonosynthState = {
    val initPitchEnvState = EnvelopeState(
      0,
      notes.map(entry => entry._1 -> PointEnvelope(entry._2._1.freqInHz))
    )
    val initAdsrEnvState = EnvelopeState(
      0,
      notes.flatMap(entry => entry._2._2.asLinearEnvelopes(entry._1))
    )

    MonosynthState(0, //Dummy
      OscState(0, 440.Hz, 0), //Dummy
      initPitchEnvState,
      initAdsrEnvState
    )
  }

  def nextState(s: MonosynthState, o: OscStateGen)(implicit audioContext: AudioContext): MonosynthState = {
    val newPitchState = EnvelopeStateGen.nextState(s.pitchEnvState)
    val newAdsrState = EnvelopeStateGen.nextState(s.adsrEnvState)
    val newOscState = o.nextState(s.oscState.copy(pitch = newPitchState.value.Hz))

    s.copy(
      sample = newOscState.sample * newAdsrState.value,
      oscState = newOscState,
      pitchEnvState = newPitchState,
      adsrEnvState = newAdsrState
    )
  }
}
