package com.scalaudio.rpc

import com.scalaudio.rpc.thrift.generated.ScalaudioService
import org.apache.thrift.protocol.TJSONProtocol
import org.apache.thrift.server.TServlet
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.{ServletHandler, ServletHolder}

object JsonServer extends App {

  val processor = new ScalaudioService.Processor[ScalaudioService.Iface](new ServiceHandler)
  val server: Server = new Server(9091)
  val handler: ServletHandler = new ServletHandler
  server.setHandler(handler)

  val holder = new ServletHolder(new TServlet(processor, new TJSONProtocol.Factory()))
  handler.addServletWithMapping(holder, "/*")

  server.start()
}