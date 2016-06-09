package com.scalaudio.rpc

import com.scalaudio.rpc.thrift.generated.ScalaudioService

/**
  * Created by johnmcgill on 6/8/16.
  */
class Handler extends ScalaudioService.Iface {
  override def transmitDouble(handle: String, value: Double): Unit =
    MessageQueue.updateDoublesMap(handle, value)
}