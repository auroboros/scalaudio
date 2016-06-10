#!/usr/bin/env bash

rm -r ../scalaudio-rpc-frontend/public/javascripts/thrift/*

thrift -out ../scalaudio-rpc-frontend/public/javascripts/thrift --gen js:jquery src/main/resources/scalaudio.thrift