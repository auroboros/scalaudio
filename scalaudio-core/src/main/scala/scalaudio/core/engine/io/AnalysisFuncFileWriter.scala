package scalaudio.core.engine.io

/**
  * Created by johnmcgill on 6/7/16.
  */
// TODO: Move this to units.io
case class AnalysisFuncFileWriter(analysisFunc: () => Option[Array[Double]],
                                  override val analysisTypeExtension: String) extends AnalysisFileWriter {

  override def dataFrameOut: Option[Array[Double]] = analysisFunc()
}
