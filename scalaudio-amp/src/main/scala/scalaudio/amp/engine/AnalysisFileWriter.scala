package scalaudio.amp.engine

import java.io.FileWriter

import scalaudio.core.AudioContext
import scalaudio.core.types.AudioDuration

/**
  * Created by johnmcgill on 6/6/16.
  */
trait AnalysisFileWriter {

  val analysisTypeExtension: String = "unk" // unknown

  def dataFrameOut: Option[Array[Double]]

  def write(filename: String, duration: AudioDuration)(implicit audioContext: AudioContext) = {
    val fw = new FileWriter(s"$filename.$analysisTypeExtension.txt", true)
    1 to duration.toSamples.toInt foreach { absoluteSample =>
      audioContext.State.currentSample = absoluteSample
      optionallyWriteDataFrame(fw, dataFrameOut)
    }
    fw.close()
  }

  private def optionallyWriteDataFrame(fw: FileWriter, valuesOpt: Option[Array[Double]]) =
    valuesOpt.foreach(vals => fw.write(vals.mkString(",") + "\n"))
}