var transport = new Thrift.Transport("/thrift/service/tutorial/");
var protocol  = new Thrift.Protocol(transport);
var client    = new ScalaudioServiceClient(protocol);