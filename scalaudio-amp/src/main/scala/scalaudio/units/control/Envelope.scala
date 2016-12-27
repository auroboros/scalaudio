package scalaudio.units.control

import signalz.{ReflexiveMutatingState, SequentialState}

import scala.collection.immutable.SortedMap
import scala.collection.mutable
import scalaudio.core.AudioContext
import scalaudio.core.types._

/**
  * Created by johnmcgill on 5/29/16.
  */
object Envelope {
  def apply(envelopeEvents: List[TimedEnvelopeSegment],
            startValue: Double = 0)
           (implicit audioContext: AudioContext) = MutableEnvelope(envelopeEvents, startValue)

  val immutable = new ImmutableEnvelope {}
}

// Mutable

case class MutableEnvelope(
                            envelopeEvents: List[TimedEnvelopeSegment],
                            startValue: Double = 0
                          )
                          (implicit val audioContext: AudioContext)
  extends ReflexiveMutatingState[MutableEnvelope, Unit, Sample] {

  val sortedEventQueue = mutable.Queue(envelopeEvents.sortBy(_.startTime): _*)

  var latestValue = startValue

  var maybeCurrentEvent: Option[TimedEnvelopeSegment] = None

  // Definition
  override def process(i: Unit, s: MutableEnvelope): (Sample, MutableEnvelope) = {

    val currentTime = audioContext.currentTime // TODO: Unnecessary? (performance?)

    // If there was an event in progress but it has ended, clear slot
    maybeCurrentEvent.foreach{ inProgressEvent =>
      if (currentTime > inProgressEvent.endTime)
        maybeCurrentEvent = None
    }

    // If there is no event in progress, check for the next one (but ignore overlapping ones that would have also ended)
    if (maybeCurrentEvent.isEmpty) {
      maybeCurrentEvent = sortedEventQueue.dequeueAll(_.startTime <= currentTime)
        .find(_.endTime >= currentTime)
    }

    // If event in progress, get value. Otherwise, use last (maybe can optimize by only updating this on end time but... requires comparison anyway:)
    latestValue = maybeCurrentEvent.map(_.valueAtTime(currentTime)).getOrElse(latestValue)

    (latestValue, this)
  }
}

// Immutable

case class EnvelopeState(value: Double,
                         remainingEvents: SortedMap[AudioDuration, EnvelopeSegment])

trait ImmutableEnvelope extends SequentialState[EnvelopeState, AudioContext] {
  def nextState(s: EnvelopeState)(implicit audioContext: AudioContext): EnvelopeState = {
    val firstStartTimeOption = s.remainingEvents.headOption.map(_._1)
    val currentTime = AudioDuration(audioContext.State.currentSample)

    s.copy(
      value = firstStartTimeOption.map { firstStartTime =>
        if (firstStartTime <= currentTime)
          s.remainingEvents.head._2.valueAtRelativeTime(currentTime - firstStartTime)
        else s.value
      }.getOrElse(s.value),
      remainingEvents = s.remainingEvents.dropWhile(re => re._2.endTime(re._1) < currentTime.nextSample)
    )
  }
}
