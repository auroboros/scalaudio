package scalaudio.amp.immutable.synth

import scala.collection.immutable.SortedMap
import scalaudio.amp.immutable.control.{AdsrEnvelope, EnvelopeState, EnvelopeStateGen}
import scalaudio.amp.immutable.ugen.{OscState, Osc}
import scalaudio.core.AudioContext
import scalaudio.core.types.{AudioDuration, Pitch, Sample}

/**
  * Created by johnmcgill on 5/30/16.
  */
case class PolysynthState(sample: Sample,
                          remainingNotes: SortedMap[AudioDuration, List[(Pitch, AdsrEnvelope)]],
                          voicesPlaying: List[PolysynthVoiceState])

case class PolysynthVoiceState(sample: Sample,
                               osc: Osc,
                               oscState: OscState,
                               envState: EnvelopeState) {
  def finished : Boolean = envState.remainingEvents.isEmpty
}

object PolysynthStateGen {
  def nextState(s: PolysynthState, o: Osc)(implicit audioContext: AudioContext): PolysynthState = {
    // add new voices
    val newVoices : List[PolysynthVoiceState] = s.remainingNotes.takeWhile(_._1 <= audioContext.currentTime)
      .flatMap{noteList => noteList._2.map(note => newVoice(o, note._1, note._2))}.toList
    // remove finished voices
    val liveVoices = (newVoices ::: s.voicesPlaying).filterNot(_.finished)
    // update live voice states
    val updatedLiveVoices = liveVoices.map(nextVoiceState)

    PolysynthState(
      updatedLiveVoices.map(_.sample).sum, // get sample per voice & sum
      s.remainingNotes.dropWhile(_._1 <= audioContext.currentTime),
      updatedLiveVoices
    )
  }

  def newVoice(osc: Osc, pitch: Pitch, adsrEnvelope: AdsrEnvelope)(implicit audioContext: AudioContext) =
    PolysynthVoiceState(0,
      osc,
      OscState(0, pitch, 0),
      EnvelopeState(0, adsrEnvelope.asLinearEnvelopes(AudioDuration(audioContext.State.currentSample))))

  def nextVoiceState(s: PolysynthVoiceState)(implicit audioContext: AudioContext): PolysynthVoiceState = {
    val newOscState = s.osc.nextState(s.oscState)
    val newEnvState = EnvelopeStateGen.nextState(s.envState)

    s.copy(
      sample = newOscState.sample * newEnvState.value,
      oscState = newOscState,
      envState = newEnvState
    )
  }
}
