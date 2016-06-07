package com.scalaudio.amp.engine

import com.scalaudio.amp.immutable.analysis.{EnergyAnalyzerState, EnergyAnalyzerStateGen}
import com.scalaudio.amp.immutable.ugen.{OscState, SineStateGen}
import com.scalaudio.core.{AudioContext, ScalaudioConfig, ScalaudioCoreTestHarness}

import scala.concurrent.duration._

/**
  * Created by johnmcgill on 6/7/16.
  */
class AnalysisFuncFileWriterDemo extends ScalaudioCoreTestHarness {
  "Analysis file writer" should "capture some RMS values" in {
    implicit val audioContext = AudioContext(ScalaudioConfig(nOutChannels = 1))

    var sineState : OscState = OscState(0, 440.Hz, 0)
    var rmsState: EnergyAnalyzerState = EnergyAnalyzerState(0, None)

    val analysisFunc = () => {
      sineState = SineStateGen.nextState(sineState)
      rmsState = EnergyAnalyzerStateGen.nextState(rmsState.copy(sampleIn = sineState.sample))
      rmsState.energyLevel.map(Array(_))
    }

    val writer = AnalysisFuncFileWriter(analysisFunc, "rms")
    writer.write("test-analysis", 5 seconds)
  }
}
