package scalaudio.rpc

import com.scalaudio.rpc.thrift.generated.ScalaudioService
import org.apache.thrift.protocol.TJSONProtocol
import org.apache.thrift.server.TServlet
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.{FilterHolder, ServletHandler, ServletHolder}
import org.eclipse.jetty.servlets.CrossOriginFilter

object JsonServer {

  def serve() = {
    val processor = new ScalaudioService.Processor[ScalaudioService.Iface](new ServiceHandler)
    val server: Server = new Server(9091)
    val handler: ServletHandler = new ServletHandler
    server.setHandler(handler)

    // Add the filter, and then use the provided FilterHolder to configure it
    val cors = new FilterHolder(classOf[CrossOriginFilter]) // ,"/*",util.EnumSet.of(DispatcherType.REQUEST)
    cors.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*")
    cors.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*")
    cors.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,POST,HEAD")
    cors.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, "X-Requested-With,Content-Type,Accept,Origin")
    handler.addFilterWithMapping(cors, "/*", 0)

    val holder = new ServletHolder(new TServlet(processor, new TJSONProtocol.Factory()))
    handler.addServletWithMapping(holder, "/*")

    server.start()
  }
}