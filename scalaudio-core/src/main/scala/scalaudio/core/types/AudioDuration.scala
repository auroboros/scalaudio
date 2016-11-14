package scalaudio.core.types

import scala.concurrent.duration.{FiniteDuration, _}
import scalaudio.core.AudioContext
/**
  * Created by johnmcgill on 1/18/16.
  */
// TODO: allow this to accept some form of samples as input instead since this is really the smallest indivisible unit?
case class AudioDuration(nSamples : Long)(implicit audioContext: AudioContext) extends Ordered[AudioDuration] {
  import DurationConverter._

  def toSamples : Long = nSamples

  def nextSample : AudioDuration = AudioDuration(nSamples + 1)

  def toBuffers : Long = samples2Buffers(nSamples) // TODO: Should be Double?? Buffer is not smallest unit in system

  def toBeats : Double = samples2Beats(nSamples)

  // TODO: Should include conversion back to finite dur, for convenience

  def +(other : AudioDuration) : AudioDuration = AudioDuration(nSamples + other.nSamples)

  def -(other : AudioDuration) : AudioDuration = AudioDuration(nSamples - other.nSamples)

  override def compare(that: AudioDuration) : Int = Math.signum(nSamples - that.nSamples).toInt

  def /(other : AudioDuration) : Double = nSamples.toDouble / other.nSamples
}

object AudioDuration {
  import scalaudio.core.math._

  def linearDurationInterpolation(partialDur: AudioDuration, fullDur: AudioDuration, startVal: Double, endVal: Double) : Double =
    linearInterpolate(startVal, endVal, partialDur / fullDur)
}

object DurationConverter {
  def finiteDuration2Samples(duration : FiniteDuration)(implicit audioContext: AudioContext) : Long =
    (duration.toNanos * audioContext.config.samplingRate / Math.pow(10,9)).toLong

  def samples2Buffers(nSamples : Long)(implicit audioContext: AudioContext) : Long = (nSamples.toDouble / audioContext.config.framesPerBuffer.toDouble).ceil.toLong

  def buffers2Samples(nBuffers : Long)(implicit audioContext: AudioContext) : Long = nBuffers * audioContext.config.framesPerBuffer

  def beats2Samples(nBeats : Long)(implicit audioContext: AudioContext) : Long = (nBeats * finiteDuration2Samples(1 minute) / audioContext.config.beatsPerMinute).toLong

  def measures2Samples(nMeasures : Long)(implicit audioContext: AudioContext) : Long = (beats2Samples(nMeasures) * audioContext.config.beatsPerMeasure).toLong

  def samples2Beats(nSamples : Long)(implicit audioContext: AudioContext) : Double = ???

  def buffers2FiniteDuration(length: Int)(implicit audioContext: AudioContext) : FiniteDuration =
    samples2FiniteDuration(length * audioContext.config.framesPerBuffer)

  def samples2FiniteDuration(nSamples : Int)(implicit audioContext: AudioContext) =
    (nSamples.toDouble / audioContext.config.samplingRate) seconds
}

case class AudioDurationRichInt(n : Int)(implicit audioContext: AudioContext) {
  import DurationConverter._

  def samples = AudioDuration(n)

  def buffers = AudioDuration(buffers2Samples(n))

  def beats = AudioDuration(beats2Samples(n))

  def measures = AudioDuration(measures2Samples(n))

//  def measure&beat = AudioDuration(beats2Samples(n)) // Should be an implicit on a tuple?
} //TODO: should be able to give these as Doubles as well