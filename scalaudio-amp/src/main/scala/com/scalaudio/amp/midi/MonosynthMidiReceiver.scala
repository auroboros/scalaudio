package com.scalaudio.amp.midi

import javax.sound.midi.{MidiMessage, Receiver}

import com.scalaudio.amp.immutable.envelope.AdsrEnvelope
import com.scalaudio.amp.immutable.synth.{MonosynthState, MonosynthStateGen}
import com.scalaudio.core.AudioContext
import com.scalaudio.core.midi._

import scala.collection.mutable.ListBuffer
import scala.collection._

/**
  * Created by johnmcgill on 6/2/16.
  */
class MonosynthMidiReceiver(adsrTemplate: AdsrEnvelope)(implicit audioContext: AudioContext) extends Receiver {
  val unmatchedNoteOns: mutable.Map[String, NoteOn] = mutable.Map.empty[String, NoteOn]
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
      val newState = unprocessedCommands.foldLeft(s)(addCommandToState) // Some fold lefty-thing that matches on commands and uses unmatched noteOn info
      unprocessedCommands.clear()
      newState
    }

  def addCommandToState(s: MonosynthState, c: MidiCommand): MonosynthState =
    c match {
      case NoteOn(channel, noteNumber, velocity) =>
        if (unmatchedNoteOns.isEmpty) {

        }
        s.copy(pitchEnvState = s.pitchEnvState.copy(value = noteNumber))
      case NoteOff(channel, noteNumber, velocity) =>
        s.copy(
          adsrEnvState = s.adsrEnvState.copy(
            remainingEvents = s.adsrEnvState.remainingEvents ++ adsrTemplate.asLinearEnvelopes(audioContext)
          )
        )
    }
}
