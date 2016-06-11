package com.scalaudio.rpc

import com.scalaudio.rpc.thrift.generated.ScalaudioService
import org.apache.thrift.TProcessor
import org.apache.thrift.protocol.TBinaryProtocol
import org.apache.thrift.server.TThreadPoolServer
import org.apache.thrift.transport.{TFramedTransport, TServerSocket}

/**
  * Created by johnmcgill on 6/8/16.
  */
case class RpcServer() {
  def serve() = {
    val processor = new ScalaudioService.Processor[ScalaudioService.Iface](new ServiceHandler)
    launchServer(9090, processor)
  }

  def launchServer(port: Int, processor: TProcessor) = {

    val serverTransport = new TServerSocket(port)
    val serverArgs = new TThreadPoolServer.Args(serverTransport)

    serverArgs.maxWorkerThreads(10)
    serverArgs.transportFactory(new TFramedTransport.Factory())
    serverArgs.protocolFactory(new TBinaryProtocol.Factory())
    serverArgs.processor(processor)

    val server = new TThreadPoolServer(serverArgs)
    new Thread(new TServerRunnable(server)).start()
  }
}