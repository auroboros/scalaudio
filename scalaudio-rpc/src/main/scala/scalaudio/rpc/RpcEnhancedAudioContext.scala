package scalaudio.rpc

import scalaudio.core.{AudioContext, ScalaudioConfig}

/**
  * Created by johnmcgill on 12/4/16.
  */
class RpcEnhancedAudioContext(override val config: ScalaudioConfig = ScalaudioConfig()) extends AudioContext {
  override def preStart(): Unit = if (config.rpcEnabled) JsonServer.serve()
}

object RpcEnhancedAudioContext {
  def apply(config: ScalaudioConfig = ScalaudioConfig()) =
    new RpcEnhancedAudioContext(config)
}