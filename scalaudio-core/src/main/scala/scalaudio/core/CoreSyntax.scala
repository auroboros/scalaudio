package scalaudio.core

import scala.concurrent.duration.FiniteDuration
import scalaudio.core.engine.{AudioFunctionGraph, AudioSignalProcessingGraph, AudioStreamGraph}
import scalaudio.core.types.{AudioDuration, PitchRichDouble, PitchRichInt, _}

/**
  * This is a bag-of-junk file to magically make all syntax work. Maybe there is a better pattern to split this up by function?
  * Could include implicits with relevant classes but then can only be really assured they're available if some flagship class
  * is imported? Considering case-by-case for now.
  *
  * Created by johnmcgill on 12/22/15.
  */
trait CoreSyntax {
  // "Durations" syntax
  implicit def finiteDuration2AudioDuration(duration: FiniteDuration)(implicit audioContext: AudioContext): AudioDuration = AudioDuration(DurationConverter.finiteDuration2Samples(duration))

  implicit def int2AudioDurationRichInt(n: Int)(implicit audioContext: AudioContext): AudioDurationRichInt = AudioDurationRichInt(n)

  // "Pitch" syntax
  implicit def int2PitchRichInt(i : Int) : PitchRichInt = PitchRichInt(i)
  implicit def double2PitchRichDouble(d : Double) : PitchRichDouble = PitchRichDouble(d)


  // Unit func vs. func0
  implicit def unitFuncToFunction0[T](unitFunc: Unit => T): () => T = () => unitFunc()
  implicit def function0ToUnitFunc[T](func0: () => T): Unit => T = (u: Unit) => func0()

  // Functions & streams to their "completed Graph" counterparts
  implicit def emptyParensFunc2AudioGraph(func: () => _)
                                         (implicit audioContext: AudioContext): AudioFunctionGraph = AudioFunctionGraph(func)

  implicit def unitFunc2AudioGraph(func: Unit => _)
                                  (implicit audioContext: AudioContext): AudioFunctionGraph = AudioFunctionGraph(func)

  implicit def stream2AudioGraph(stream: => Stream[_])
                                (implicit audioContext: AudioContext): AudioStreamGraph = AudioStreamGraph(stream)

}