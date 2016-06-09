namespace java com.scalaudio.rpc.thrift.generated

service ScalaudioService {
  void transmit_double(1: string handler, 2: double value);
}