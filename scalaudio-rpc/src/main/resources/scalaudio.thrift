namespace java scalaudio.rpc.thrift.generated

service ScalaudioService {
  string hello();
  oneway void transmit_double(1: string handle, 2: double value);
}