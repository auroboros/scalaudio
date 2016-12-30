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
            startValue: Double = 0,
            mode: EnvelopeFallbackMode = Previous)
           (implicit audioContext: AudioContext) = MutableEnvelope(envelopeEvents, startValue, mode)

  val immutable = new ImmutableEnvelope {}
}

sealed trait EnvelopeFallbackMode
case object Previous extends EnvelopeFallbackMode
case object Default extends EnvelopeFallbackMode

// Mutable

case class MutableEnvelope(
                            envelopeEvents: List[TimedEnvelopeSegment],
                            startValue: Double = 0,
                            mode: EnvelopeFallbackMode = Previous
                          )
                          (implicit val audioContext: AudioContext)
  extends ReflexiveMutatingState[MutableEnvelope, Unit, Sample] {

  val sortedEventQueue = mutable.Queue(envelopeEvents: _*)

  var latestValue = startValue

  var maybeCurrentEvent: Option[TimedEnvelopeSegment] = None

  // TODO: Although pattern matching is only done once, do these anon function invocations create overhead? Could just create a class for each mode
  // Especially since in "Default" mode latestValue var is probably irrelevant altogether.
  // Another possibility is to output Option and then trail with sample & hold or defaulter as separate sig processing units
  val fallbackValue = mode match {
    case Previous => () => latestValue
    case Default => () => startValue
  }

  // Add segment & Default to Now (real-time)
  def addSegment(segment: EnvelopeSegment, time: AudioDuration = audioContext.currentTime) = sortedEventQueue.enqueue(TimedEnvelopeSegment(time, segment))

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
      maybeCurrentEvent = sortedEventQueue.dequeueAll(_.startTime <= currentTime) // If sorting is enforced, maybe won't have to dequeueAll?
        .find(_.endTime >= currentTime)
    }

    // If event in progress, get value. Otherwise, use last (maybe can optimize by only updating this on end time but... requires comparison anyway?)
    latestValue = maybeCurrentEvent.map(_.valueAtTime(currentTime)).getOrElse(fallbackValue())

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
