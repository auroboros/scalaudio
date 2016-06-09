#!/usr/bin/env bash

rm -r src/main/java/com/scalaudio/rpc/thrift/generated/*

thrift -out src/main/java --gen java:fullcamel src/main/resources/scalaudio.thrift