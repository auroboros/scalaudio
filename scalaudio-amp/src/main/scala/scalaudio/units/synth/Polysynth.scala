package scalaudio.units.synth

import scala.collection.immutable.SortedMap
import scalaudio.units.control.{AdsrEnvelope, EnvelopeState, Envelope}
import scalaudio.units.ugen.{OscState, ImmutableOsc}
import scalaudio.core.AudioContext
import scalaudio.core.types.{AudioDuration, Pitch, Sample}

/**
  * Created by johnmcgill on 5/30/16.
  */
object Polysynth {
  def immutable(osc: ImmutableOsc) = new Polysynth(osc)

  // "private" (conceptually, even if not actually designated)
  private def newVoice(osc: ImmutableOsc, pitch: Pitch, adsrEnvelope: AdsrEnvelope)(implicit audioContext: AudioContext) =
  PolysynthVoiceState(0,
    osc,
    OscState(0, pitch, 0),
    EnvelopeState(0, adsrEnvelope.asLinearEnvelopes(AudioDuration(audioContext.State.currentSample))))

  private def nextVoiceState(s: PolysynthVoiceState)(implicit audioContext: AudioContext): PolysynthVoiceState = {
    val newOscState = s.osc.nextState(s.oscState)
    val newEnvState = Envelope.immutable.nextState(s.envState)

    s.copy(
      sample = newOscState.sample * newEnvState.value,
      oscState = newOscState,
      envState = newEnvState
    )
  }
}

case class PolysynthState(sample: Sample,
                          remainingNotes: SortedMap[AudioDuration, List[(Pitch, AdsrEnvelope)]],
                          voicesPlaying: List[PolysynthVoiceState])

case class PolysynthVoiceState(sample: Sample,
                               osc: ImmutableOsc,
                               oscState: OscState,
                               envState: EnvelopeState) {
  def finished : Boolean = envState.remainingEvents.isEmpty
}

class Polysynth(val osc: ImmutableOsc) {
  import Polysynth._

  def nextState(s: PolysynthState)(implicit audioContext: AudioContext): PolysynthState = {
    // add new voices
    val newVoices : List[PolysynthVoiceState] = s.remainingNotes.takeWhile(_._1 <= audioContext.currentTime)
      .flatMap{noteList => noteList._2.map(note => newVoice(osc, note._1, note._2))}.toList
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
}