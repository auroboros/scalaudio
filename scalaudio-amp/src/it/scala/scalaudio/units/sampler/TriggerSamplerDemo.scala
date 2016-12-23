package scalaudio.units.sampler

import org.scalatest.{FlatSpec, Matchers}

import scala.collection.immutable.{SortedMap, TreeMap}
import scala.collection.parallel
import scala.collection.parallel.immutable
import scala.concurrent.duration._
import scalaudio.core.AudioContext
import scalaudio.core.types.AudioDuration
import scalaudio.units.AmpSyntax
import scalaudio.units.control.{Trigger, TriggerState}
import scalaz.Scalaz._
import scalaz._

/**
  * Created by johnmcgill on 12/23/16.
  */
class TriggerSamplerDemo extends FlatSpec with Matchers with AmpSyntax {
  "TriggerSampler" should "play a single sample if initialized with a single 0 position list entry" in {
    implicit val audioContext = AudioContext()

    val ff = TriggerSampler.immutable(FileSample("/Users/johnmcgill/nocode/samples/Yamaha-TG100-Whistle-C5.wav"))
      .asFunction(TriggerSamplerState(playbackPositions = List(0)))
      .map(_.frame)

    playback(ff, 3.seconds)
  }

  "TriggerSampler downstream from trigger" should "play at given times" in {
    implicit val audioContext = AudioContext()

    val triggerTimes = List(1.seconds, 5.seconds) ++ 2.0.to(3.0, 0.1).map(_.seconds)

    val triggerMap: SortedMap[AudioDuration, List[Double]] = TreeMap(
      triggerTimes.map { time =>
        (finiteDuration2AudioDuration(time), List(0.0))
      }.toArray: _*
    )

    val triggerFunc: () => TriggerState[Double] = Trigger.immutable.asFunction(TriggerState(Nil, triggerMap))

    val samplerFunc: (List[Double]) => TriggerSamplerState = TriggerSampler.immutable(FileSample("/Users/johnmcgill/nocode/samples/Yamaha-TG100-Whistle-C5.wav"))
      .asFunction.withModifier(
      TriggerSamplerState(playbackRate = .75),
      (state: TriggerSamplerState, newPositions: List[Double]) => state.copy(playbackPositions = state.playbackPositions ++ newPositions)
    )

    val ff = triggerFunc map (_.releasedTriggers) map samplerFunc map (_.frame)

    playback(ff, 8.seconds)
  }
}
