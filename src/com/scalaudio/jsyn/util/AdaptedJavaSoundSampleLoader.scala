package com.scalaudio.jsyn.util

import java.io.{InputStream, File, IOException}
import java.net.URL
import javax.sound.sampled.{AudioInputStream, AudioFormat, UnsupportedAudioFileException, AudioSystem}

import com.jsyn.util.SampleLoader
import com.scalaudio.engine.Interleaver

/**
  * Created by johnmcgill on 1/5/16.
  *
  * Adapted/modified from "JavaSoundSampleLoader" of JSyn
  * @author Phil Burk (C) 2011 Mobileer Inc
  */
object AdaptedJavaSoundSampleLoader {

  def loadDoubleSample(fileIn: File): DoubleSample =
    loadDoubleSample(AudioSystem.getAudioInputStream(fileIn))

  def loadDoubleSample(inputStream: InputStream): DoubleSample =
    loadDoubleSample(AudioSystem.getAudioInputStream(inputStream))

  def loadDoubleSample(url: URL): DoubleSample =
    loadDoubleSample(AudioSystem.getAudioInputStream(url))

  private def loadDoubleSample(audioInputStream: AudioInputStream): DoubleSample = {
    var floatData: Array[Float] = null
    var bytesPerFrame: Int = audioInputStream.getFormat.getFrameSize
    if (bytesPerFrame == AudioSystem.NOT_SPECIFIED) {
      bytesPerFrame = 1
    }
    val format: AudioFormat = audioInputStream.getFormat
    if (format.getEncoding eq AudioFormat.Encoding.PCM_SIGNED) {
      floatData = loadSignedPCM(audioInputStream)
    } else {
      throw new UnsupportedAudioFileException("Only signed PCM is supported.")
    }

    DoubleSample(Interleaver.deinterleave(floatData.map(_.toDouble), format.getChannels), format.getFrameRate)
  }

  private def loadSignedPCM(audioInputStream: AudioInputStream): Array[Float] = {
    var totalSamplesRead: Int = 0
    val format: AudioFormat = audioInputStream.getFormat
    val numFrames: Int = audioInputStream.getFrameLength.toInt
    val numSamples: Int = format.getChannels * numFrames
    val data: Array[Float] = new Array[Float](numSamples)
    val bytesPerFrame: Int = format.getFrameSize
    val numBytes: Int = 1024 * bytesPerFrame
    val audioBytes: Array[Byte] = new Array[Byte](numBytes)
    var numBytesRead: Int = 0
    var numFramesRead: Int = 0
    while ({
      numBytesRead = audioInputStream.read(audioBytes); numBytesRead
    } != -1) {
      val bytesRemainder: Int = numBytesRead % bytesPerFrame
      if (bytesRemainder != 0) {
        throw new IOException("Read partial block of sample data!")
      }
      if (audioInputStream.getFormat.getSampleSizeInBits == 16) {
        if (format.isBigEndian) {
          SampleLoader.decodeBigI16ToF32(audioBytes, 0, numBytesRead, data, totalSamplesRead)
        }
        else {
          SampleLoader.decodeLittleI16ToF32(audioBytes, 0, numBytesRead, data, totalSamplesRead)
        }
      }
      else if (audioInputStream.getFormat.getSampleSizeInBits == 24) {
        if (format.isBigEndian) {
          SampleLoader.decodeBigI24ToF32(audioBytes, 0, numBytesRead, data, totalSamplesRead)
        }
        else {
          SampleLoader.decodeLittleI24ToF32(audioBytes, 0, numBytesRead, data, totalSamplesRead)
        }
      }
      else if (audioInputStream.getFormat.getSampleSizeInBits == 32) {
        if (format.isBigEndian) {
          SampleLoader.decodeBigI32ToF32(audioBytes, 0, numBytesRead, data, totalSamplesRead)
        }
        else {
          SampleLoader.decodeLittleI32ToF32(audioBytes, 0, numBytesRead, data, totalSamplesRead)
        }
      }
      else {
        throw new UnsupportedAudioFileException("Only 16, 24 or 32 bit PCM samples supported.")
      }
      numFramesRead = numBytesRead / bytesPerFrame
      totalSamplesRead += numFramesRead * format.getChannels
    }
    data
  }
}
