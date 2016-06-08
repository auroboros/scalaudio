package com.scalaudio.amp.engine

/**
  * Created by johnmcgill on 6/7/16.
  */
case class AnalysisFuncFileWriter(analysisFunc: () => Option[Array[Double]],
                                  override val analysisTypeExtension: String) extends AnalysisFileWriter {

  override def dataFrameOut: Option[Array[Double]] = analysisFunc()
}