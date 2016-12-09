package scalaudio.core.util

import java.io.{File, IOException, InputStream}
import java.lang.{Float => JFloat}
import java.net.URL
import javax.sound.sampled.{AudioFormat, AudioInputStream, AudioSystem, UnsupportedAudioFileException}

import scalaudio.core.math.Interleaver

/**
  * Created by johnmcgill on 1/5/16.
  *
  * Adapted/modified from "JavaSoundSampleLoader" of JSyn
 *
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
          decodeBigI16ToF32(audioBytes, 0, numBytesRead, data, totalSamplesRead)
        }
        else {
          decodeLittleI16ToF32(audioBytes, 0, numBytesRead, data, totalSamplesRead)
        }
      }
      else if (audioInputStream.getFormat.getSampleSizeInBits == 24) {
        if (format.isBigEndian) {
          decodeBigI24ToF32(audioBytes, 0, numBytesRead, data, totalSamplesRead)
        }
        else {
          decodeLittleI24ToF32(audioBytes, 0, numBytesRead, data, totalSamplesRead)
        }
      }
      else if (audioInputStream.getFormat.getSampleSizeInBits == 32) {
        if (format.isBigEndian) {
          decodeBigI32ToF32(audioBytes, 0, numBytesRead, data, totalSamplesRead)
        }
        else {
          decodeLittleI32ToF32(audioBytes, 0, numBytesRead, data, totalSamplesRead)
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

  /**
    * Decode 24 bit samples from a BigEndian byte array into a float array. The samples will be
    * normalized into the range -1.0 to +1.0.
    *
    * @param audioBytes   raw data from an audio file
    * @param offset       first element of byte array
    * @param numBytes     number of bytes to process
    * @param data         array to be filled with floats
    * @param outputOffset first element of float array to be filled
    */
  def decodeBigI24ToF32(audioBytes: Array[Byte], offset: Int, numBytes: Int, data: Array[Float], outputOffset: Int) {
    val lastByte: Int = offset + numBytes
    var byteCursor: Int = offset
    var floatCursor: Int = outputOffset
    while (byteCursor < lastByte) {
      val hi: Int = audioBytes({
        byteCursor += 1
        byteCursor - 1
      }) & 0x00FF
      val mid: Int = audioBytes({
        byteCursor += 1
        byteCursor - 1
      }) & 0x00FF
      val lo: Int = audioBytes({
        byteCursor += 1
        byteCursor - 1
      }) & 0x00FF
      val value: Int = (hi << 24) | (mid << 16) | (lo << 8)
      data({
        floatCursor += 1; floatCursor - 1
      }) = value * (1.0f / Integer.MAX_VALUE)
    }
  }

  def decodeBigI16ToF32(audioBytes: Array[Byte], offset: Int, numBytes: Int, data: Array[Float], outputOffset: Int) {
    val lastByte: Int = offset + numBytes
    var byteCursor: Int = offset
    var floatCursor: Int = outputOffset
    while (byteCursor < lastByte) {
      val hi: Int = audioBytes({
        byteCursor += 1
        byteCursor - 1
      }) & 0x00FF
      val lo: Int = audioBytes({
        byteCursor += 1
        byteCursor - 1
      }) & 0x00FF
      val value: Short = ((hi << 8) | lo).toShort
      data({
        floatCursor += 1; floatCursor - 1
      }) = value * (1.0f / 32768)
    }
  }

  def decodeBigF32ToF32(audioBytes: Array[Byte], offset: Int, numBytes: Int, data: Array[Float], outputOffset: Int) {
    val lastByte: Int = offset + numBytes
    var byteCursor: Int = offset
    var floatCursor: Int = outputOffset
    while (byteCursor < lastByte) {
      var bits: Int = audioBytes({
        byteCursor += 1; byteCursor - 1
      })
      bits = (bits << 8) | (audioBytes({
        byteCursor += 1
        byteCursor - 1
      }) & 0x00FF)
      bits = (bits << 8) | (audioBytes({
        byteCursor += 1
        byteCursor - 1
      }) & 0x00FF)
      bits = (bits << 8) | (audioBytes({
        byteCursor += 1
        byteCursor - 1
      }) & 0x00FF)
      data({
        floatCursor += 1; floatCursor - 1
      }) = JFloat.intBitsToFloat(bits)
    }
  }

  def decodeBigI32ToF32(audioBytes: Array[Byte], offset: Int, numBytes: Int, data: Array[Float], outputOffset: Int) {
    val lastByte: Int = offset + numBytes
    var byteCursor: Int = offset
    var floatCursor: Int = outputOffset
    while (byteCursor < lastByte) {
      var value: Int = audioBytes({
        byteCursor += 1; byteCursor - 1
      }) // MSB
      value = (value << 8) | (audioBytes({
        byteCursor += 1
        byteCursor - 1
      }) & 0x00FF)
      value = (value << 8) | (audioBytes({
        byteCursor += 1
        byteCursor - 1
      }) & 0x00FF)
      value = (value << 8) | (audioBytes({
        byteCursor += 1
        byteCursor - 1
      }) & 0x00FF)
      data({
        floatCursor += 1; floatCursor - 1
      }) = value * (1.0f / Integer.MAX_VALUE)
    }
  }

  def decodeLittleF32ToF32(audioBytes: Array[Byte], offset: Int, numBytes: Int, data: Array[Float], outputOffset: Int) {
    val lastByte: Int = offset + numBytes
    var byteCursor: Int = offset
    var floatCursor: Int = outputOffset
    while (byteCursor < lastByte) {
      var bits: Int = audioBytes({
        byteCursor += 1
        byteCursor - 1
      }) & 0x00FF // LSB
      bits += (audioBytes({
        byteCursor += 1
        byteCursor - 1
      }) & 0x00FF) << 8
      bits += (audioBytes({
        byteCursor += 1
        byteCursor - 1
      }) & 0x00FF) << 16
      bits += audioBytes({
        byteCursor += 1
        byteCursor - 1
      }) << 24
      data({
        floatCursor += 1; floatCursor - 1
      }) = JFloat.intBitsToFloat(bits)
    }
  }

  def decodeLittleI32ToF32(audioBytes: Array[Byte], offset: Int, numBytes: Int, data: Array[Float], outputOffset: Int) {
    val lastByte: Int = offset + numBytes
    var byteCursor: Int = offset
    var floatCursor: Int = outputOffset
    while (byteCursor < lastByte) {
      var value: Int = audioBytes({
        byteCursor += 1
        byteCursor - 1
      }) & 0x00FF
      value += (audioBytes({
        byteCursor += 1
        byteCursor - 1
      }) & 0x00FF) << 8
      value += (audioBytes({
        byteCursor += 1
        byteCursor - 1
      }) & 0x00FF) << 16
      value += audioBytes({
        byteCursor += 1
        byteCursor - 1
      }) << 24
      data({
        floatCursor += 1; floatCursor - 1
      }) = value * (1.0f / Integer.MAX_VALUE)
    }
  }

  def decodeLittleI24ToF32(audioBytes: Array[Byte], offset: Int, numBytes: Int, data: Array[Float], outputOffset: Int) {
    val lastByte: Int = offset + numBytes
    var byteCursor: Int = offset
    var floatCursor: Int = outputOffset
    while (byteCursor < lastByte) {
      val lo: Int = audioBytes({
        byteCursor += 1
        byteCursor - 1
      }) & 0x00FF
      val mid: Int = audioBytes({
        byteCursor += 1
        byteCursor - 1
      }) & 0x00FF
      val hi: Int = audioBytes({
        byteCursor += 1
        byteCursor - 1
      }) & 0x00FF
      val value: Int = (hi << 24) | (mid << 16) | (lo << 8)
      data({
        floatCursor += 1; floatCursor - 1
      }) = value * (1.0f / Integer.MAX_VALUE)
    }
  }

  def decodeLittleI16ToF32(audioBytes: Array[Byte], offset: Int, numBytes: Int, data: Array[Float], outputOffset: Int) {
    val lastByte: Int = offset + numBytes
    var byteCursor: Int = offset
    var floatCursor: Int = outputOffset
    while (byteCursor < lastByte) {
      {
        val lo: Int = audioBytes({
          byteCursor += 1; byteCursor - 1
        }) & 0x00FF
        val hi: Int = audioBytes({
          byteCursor += 1; byteCursor - 1
        }) & 0x00FF
        val value: Short = ((hi << 8) | lo).toShort
        val sample: Float = value * (1.0f / 32768)
        data({
          floatCursor += 1; floatCursor - 1
        }) = sample
      }
    }
  }
}
