package com.scalaudio.amp.midi

import javax.sound.midi.{MidiMessage, Receiver}

import com.scalaudio.amp.immutable.envelope.AdsrEnvelope
import com.scalaudio.amp.immutable.synth.MonosynthState
import com.scalaudio.core.AudioContext
import com.scalaudio.core.midi._
import com.scalaudio.core.syntax.ScalaudioSyntaxHelpers

import scala.collection._
import scala.collection.immutable.TreeMap
import scala.collection.mutable.ListBuffer

/**
  * Created by johnmcgill on 6/2/16.
  */
case class MonosynthMidiReceiver(adsrTemplate: AdsrEnvelope)(implicit audioContext: AudioContext) extends Receiver with ScalaudioSyntaxHelpers {
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
          adsrEnvState = s.adsrEnvState.copy(
            remainingEvents = s.adsrEnvState.remainingEvents ++ adsrTemplate.asLinearEnvelopes(audioContext.currentTime).take(2)
          ),
          pitchEnvState = s.pitchEnvState.copy(value = noteNumber.MidiPitch.toHz)
        )
        println(noteNumber.MidiPitch.toHz)
        unmatchedNoteOns.update(s"$channel-$noteNumber", n)
        newState
      case NoteOff(channel, noteNumber, velocity) =>
        println("adding noteoff")
        unmatchedNoteOns -= s"$channel-$noteNumber"
        if (unmatchedNoteOns.isEmpty)
          s.copy(
            adsrEnvState = s.adsrEnvState.copy(
              remainingEvents = s.adsrEnvState.remainingEvents ++ TreeMap(adsrTemplate.asLinearEnvelopes(audioContext.currentTime).last)
            )
          )
        else s
      case _ => s
    }
  }
}
