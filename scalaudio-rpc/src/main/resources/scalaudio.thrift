namespace java com.scalaudio.rpc.thrift.generated

service ScalaudioService {
  void transmit_double(1: string handle, 2: double value);
}