package scalaudio.amp.midi

import javax.sound.midi.{MidiMessage, Receiver}

import scala.collection.immutable.TreeMap
import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scalaudio.units.control.{AdsrEnvelope, EnvelopeSegment, LinearEnvelope}
import scalaudio.units.synth.MonosynthState
import scalaudio.core.midi._
import scalaudio.core.types.AudioDuration
import scalaudio.core.{AudioContext, CoreSyntax}

/**
  * Created by johnmcgill on 6/2/16.
  */
case class MonosynthMidiReceiver(adsrTemplate: AdsrEnvelope, glissDuration: AudioDuration)(implicit audioContext: AudioContext) extends Receiver with CoreSyntax {
  val unmatchedNoteOns: mutable.LinkedHashMap[String, NoteOn] = mutable.LinkedHashMap.empty[String, NoteOn]
  val unprocessedCommands: ListBuffer[MidiCommand] = ListBuffer[MidiCommand]()

  def close() {
    System.out.print("Closed.")
  }

  def send(message: MidiMessage, timeStamp: Long) {
    val bytes: Array[Byte] = message.getMessage
    MidiParser.parse(bytes) match {
      case c@NoteOn(channel, noteNumber, velocity) => unprocessedCommands += c
      case c@NoteOff(channel, noteNumber, velocity) => unprocessedCommands += c
      case CtrlChange(channel, index, value) => println("monosynth recvd ctrl change")
      case PitchBend(channel, bend) => println("monosynth recvd pitch bend")
    }
  }

  def processMidiCommandsIntoState(s: MonosynthState): MonosynthState =
    if (unprocessedCommands.isEmpty) s
    else {
      println("unprocessedCommands available")
      val newState = unprocessedCommands.foldLeft(s)(addCommandToState) // Some fold lefty-thing that matches on commands and uses unmatched noteOn info
      unprocessedCommands.clear()
      newState
    }

  def addCommandToState(s: MonosynthState, c: MidiCommand): MonosynthState = {
    c match {
      case n@NoteOn(channel, noteNumber, velocity) =>
        println("adding noteon")
        val newState = s.copy(
          adsrEnvState = if (unmatchedNoteOns.isEmpty) {
            s.adsrEnvState.copy(
              remainingEvents = adsrTemplate.asLinearEnvelopes(audioContext.currentTime, s.adsrEnvState.value).take(2)
            )
          } else s.adsrEnvState,
          pitchEnvState = if (unmatchedNoteOns.isEmpty) s.pitchEnvState.copy(
            value = noteNumber.MidiPitch.toHz,
            remainingEvents = TreeMap.empty[AudioDuration, EnvelopeSegment]
          )
          else {
            s.pitchEnvState.copy(remainingEvents = TreeMap(
              audioContext.currentTime -> LinearEnvelope(s.pitchEnvState.value, noteNumber.MidiPitch.toHz, glissDuration)
            ))
          }
        )
        println(noteNumber.MidiPitch.toHz)
        unmatchedNoteOns.update(s"$channel-$noteNumber", n)
        newState
      case NoteOff(channel, noteNumber, velocity) =>
        println("adding noteoff")
        val repitch = (unmatchedNoteOns.size > 1) && (unmatchedNoteOns.last._1 == s"$channel-$noteNumber")

        unmatchedNoteOns -= s"$channel-$noteNumber"

        s.copy(
          adsrEnvState = if (unmatchedNoteOns.isEmpty) s.adsrEnvState.copy(
            remainingEvents = TreeMap(audioContext.currentTime -> LinearEnvelope(s.adsrEnvState.value, 0, adsrTemplate.releaseDuration))
          )
          else s.adsrEnvState,
          pitchEnvState = if (repitch) s.pitchEnvState.copy(
            remainingEvents = TreeMap(audioContext.currentTime -> LinearEnvelope(s.pitchEnvState.value, unmatchedNoteOns.last._2.noteNumber.MidiPitch.toHz, glissDuration))
          )
          else s.pitchEnvState
        )
      case _ => s
    }
  }
}
