package scalaudio.rpc.receiver

import scalaudio.rpc.MessageQueue

/**
  * Created by johnmcgill on 6/12/16.
  */
case class RpcDoubleReceiver(handle: String,
                             defaultVal: Double) {

  def currentValue : Double = MessageQueue.getDoubleValue(handle).getOrElse(defaultVal)
}

