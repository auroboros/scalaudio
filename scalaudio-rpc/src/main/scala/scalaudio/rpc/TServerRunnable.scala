package scalaudio.rpc

import org.apache.thrift.server.TServer

/**
  * Created by johnmcgill on 4/8/16.
  */
class TServerRunnable extends Runnable {
  private var server: TServer = null

  def this(server: TServer) {
    this()
    this.server = server
  }

  def run() {
    this.server.serve()
  }
}
