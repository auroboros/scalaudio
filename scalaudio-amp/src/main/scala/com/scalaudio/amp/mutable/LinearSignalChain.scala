package com.scalaudio.amp.mutable

import com.scalaudio.amp.immutable.AudioUnitState
import com.scalaudio.core.types.Sample

/**
  * Created by johnmcgill on 7/18/16.
  */
//case class LinearSignalChain(nodes: List[MutableStateWrapper[_ <: AudioUnitState]]) {
//
//  def nextSampleOut: Sample =
//    nodes.foldLeft(nodes.head.nextState().sample)((prevNodeSample, currentNode) =>
//      currentNode.nextState().sample
//    )
//
//}
