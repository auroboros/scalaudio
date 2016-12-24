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
  def apply(envelopeEvents: mutable.Queue[(AudioDuration, EnvelopeSegment)],
            startValue: Double = 0)
           (implicit audioContext: AudioContext) = MutableEnvelope(envelopeEvents, startValue)

  val immutable = new ImmutableEnvelope {}
}

// Mutable

case class MutableEnvelope(
                            envelopeEvents: mutable.Queue[(AudioDuration, EnvelopeSegment)],
                            startValue: Double = 0
                          )
                          (implicit val audioContext: AudioContext)
  extends ReflexiveMutatingState[MutableEnvelope, Unit, Sample] {

  // TODO: Use "peek" instead of all this nonsense
  val (firstStartTime, firstSegment) = envelopeEvents.dequeue()

  var lastValue = startValue

  var maybeCurrentStartTime: Option[AudioDuration] = Some(firstStartTime)
  var currentSegment = firstSegment

  // Definition
  override def process(i: Unit, s: MutableEnvelope): (Sample, MutableEnvelope) = {
    while (maybeCurrentStartTime.isDefined && currentSegment.endTime(maybeCurrentStartTime.get) < audioContext.currentTime) {

      if (envelopeEvents.nonEmpty) {
        val (newStartTime, newSegment) = envelopeEvents.dequeue()

        maybeCurrentStartTime = Some(newStartTime)
        currentSegment = newSegment
      } else {
        maybeCurrentStartTime = None
      }
    }

    Tuple2(
      maybeCurrentStartTime.map(time => currentSegment.valueAtRelativeTime(audioContext.currentTime - time)) // TODO: OO this, shouldnt be re-providing current time
        .getOrElse(lastValue),
      this
    )
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
