package scalaudio.rpc

import org.apache.thrift.protocol.TBinaryProtocol
import org.apache.thrift.transport.{TFramedTransport, TSocket}
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}

import scalaudio.rpc.thrift.generated.ScalaudioService

/**
  * Created by johnmcgill on 6/9/16.
  */
class ScalaudioServiceTest extends FlatSpec with Matchers with BeforeAndAfterAll {
    val host = "localhost"

    var socket: TSocket = null
    var client: ScalaudioService.Client = null

    override def beforeAll: Unit = {
      socket = new TSocket(host, 9092)
      socket.open()
      socket.setTimeout(10000)
      val framedTransport: TFramedTransport = new TFramedTransport(socket, Math.pow(10, 10).toInt)
      val protocol: TBinaryProtocol = new TBinaryProtocol(framedTransport)

      client = new ScalaudioService.Client(protocol)
    }

    override def afterAll: Unit = {
      socket.close()
    }
}
