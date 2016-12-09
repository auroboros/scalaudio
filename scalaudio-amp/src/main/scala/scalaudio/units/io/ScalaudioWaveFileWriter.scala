package scalaudio.units.io

import java.io._
import java.lang.{Short => JShort}
/**
  * Created by johnmcgill on 12/8/16.
  *
  * TODO: PeriodicDriftReview
  * Adapted from JSyn WaveFileWriter
  */
case class ScalaudioWaveFileWriter(outputFile: File,
                                   frameRate: Int = 44100,
                                   samplesPerFrame: Int = 1,  // 2 for stereo
                                   bitsPerSample: Int) {

  if ((bitsPerSample != 16) && (bitsPerSample != 24)) throw new IllegalArgumentException("Only 16 or 24 bits per sample allowed. Not " + bitsPerSample)

  val fileOut: FileOutputStream = new FileOutputStream(outputFile)
  val outputStream = new BufferedOutputStream(fileOut)

  private val WAVE_FORMAT_PCM: Short = 1
  private var riffSizePosition: Long = 0
  private var dataSizePosition: Long = 0
  private var bytesWritten: Int = 0
  private var headerWritten: Boolean = false
  private val PCM24_MIN: Int = -(1 << 23)
  private val PCM24_MAX: Int = (1 << 23) - 1

  @throws[IOException]
  def close() {
    outputStream.close()
    fixSizes()
  }

  /** Write entire buffer of audio samples to the WAV file. */
  @throws[IOException]
  def write(buffer: Array[Double]) {
    write(buffer, 0, buffer.length)
  }

  /** Write audio to the WAV file. */
  @throws[IOException]
  def write(buffer: Array[Float]) {
    write(buffer, 0, buffer.length)
  }

  /** Write single audio data value to the WAV file. */
  @throws[IOException]
  def write(value: Double) {
    if (!headerWritten) writeHeader()
    if (bitsPerSample == 24) writePCM24(value)
    else writePCM16(value)
  }

  @throws[IOException]
  private def writePCM24(value: Double) {
    // Offset before casting so that we can avoid using floor().
    // Also round by adding 0.5 so that very small signals go to zero.
    val temp: Double = (PCM24_MAX * value) + 0.5 - PCM24_MIN
    var sample: Int = temp.toInt + PCM24_MIN
    // clip to 24-bit range
    if (sample > PCM24_MAX) sample = PCM24_MAX
    else if (sample < PCM24_MIN) sample = PCM24_MIN
    // encode as little-endian
    writeByte(sample) // little end
    writeByte(sample >> 8) // middle
    writeByte(sample >> 16) // big end
  }

  @throws[IOException]
  private def writePCM16(value: Double) {
    // Offset before casting so that we can avoid using floor().
    // Also round by adding 0.5 so that very small signals go to zero.
    val temp: Double = (JShort.MAX_VALUE * value) + 0.5 - JShort.MIN_VALUE
    var sample: Int = temp.toInt + JShort.MIN_VALUE
    if (sample > JShort.MAX_VALUE) sample = JShort.MAX_VALUE
    else if (sample < JShort.MIN_VALUE) sample = JShort.MIN_VALUE
    writeByte(sample)
    writeByte(sample >> 8)
  }

  /** Write audio to the WAV file. */
  @throws[IOException]
  def write(buffer: Array[Double], start: Int, count: Int) {
    var i: Int = 0
    while (i < count) {
        write(buffer(start + i))
        i += 1
    }
  }

  /** Write audio to the WAV file. */
  @throws[IOException]
  def write(buffer: Array[Float], start: Int, count: Int) {
    var i: Int = 0
    while (i < count) {
        write(buffer(start + i))
        i += 1
    }
  }

  // Write lower 8 bits. Upper bits ignored.
  @throws[IOException]
  private def writeByte(b: Int) {
    outputStream.write(b)
    bytesWritten += 1
  }

  /**
    * Write a 32 bit integer to the stream in Little Endian format.
    */
  @throws[IOException]
  def writeIntLittle(n: Int) {
    writeByte(n)
    writeByte(n >> 8)
    writeByte(n >> 16)
    writeByte(n >> 24)
  }

  /**
    * Write a 16 bit integer to the stream in Little Endian format.
    */
  @throws[IOException]
  def writeShortLittle(n: Short) {
    writeByte(n)
    writeByte(n >> 8)
  }

  /**
    * Write a simple WAV header for PCM data.
    */
  @throws[IOException]
  private def writeHeader() {
    writeRiffHeader()
    writeFormatChunk()
    writeDataChunkHeader()
    outputStream.flush()
    headerWritten = true
  }

  /**
    * Write a 'RIFF' file header and a 'WAVE' ID to the WAV file.
    */
  @throws[IOException]
  private def writeRiffHeader() {
    writeByte('R')
    writeByte('I')
    writeByte('F')
    writeByte('F')
    riffSizePosition = bytesWritten
    writeIntLittle(Integer.MAX_VALUE)
    writeByte('W')
    writeByte('A')
    writeByte('V')
    writeByte('E')
  }

  /**
    * Write an 'fmt ' chunk to the WAV file containing the given information.
    */
  @throws[IOException]
  def writeFormatChunk() {
    val bytesPerSample: Int = (bitsPerSample + 7) / 8
    writeByte('f')
    writeByte('m')
    writeByte('t')
    writeByte(' ')
    writeIntLittle(16) // chunk size
    writeShortLittle(WAVE_FORMAT_PCM)
    writeShortLittle(samplesPerFrame.toShort)
    writeIntLittle(frameRate)
    // bytes/second
    writeIntLittle(frameRate * samplesPerFrame * bytesPerSample)
    // block align
    writeShortLittle((samplesPerFrame * bytesPerSample).toShort)
    writeShortLittle(bitsPerSample.toShort)
  }

  /**
    * Write a 'data' chunk header to the WAV file. This should be followed by call to
    * writeShortLittle() to write the data to the chunk.
    */
  @throws[IOException]
  def writeDataChunkHeader() {
    writeByte('d')
    writeByte('a')
    writeByte('t')
    writeByte('a')
    dataSizePosition = bytesWritten
    writeIntLittle(Integer.MAX_VALUE) // size
  }

  /**
    * Fix RIFF and data chunk sizes based on final size. Assume data chunk is the last chunk.
    */
  @throws[IOException]
  private def fixSizes() {
    val randomFile: RandomAccessFile = new RandomAccessFile(outputFile, "rw")
    try {
      // adjust RIFF size
      val end: Long = bytesWritten
      val riffSize: Int = (end - riffSizePosition).toInt - 4
      randomFile.seek(riffSizePosition)
      writeRandomIntLittle(randomFile, riffSize)
      // adjust data size
      val dataSize: Int = (end - dataSizePosition).toInt - 4
      randomFile.seek(dataSizePosition)
      writeRandomIntLittle(randomFile, dataSize)
    } finally randomFile.close()
  }

  @throws[IOException]
  private def writeRandomIntLittle(randomFile: RandomAccessFile, n: Int) {
    val buffer: Array[Byte] = new Array[Byte](4)
    buffer(0) = n.toByte
    buffer(1) = (n >> 8).toByte
    buffer(2) = (n >> 16).toByte
    buffer(3) = (n >> 24).toByte
    randomFile.write(buffer)
  }
}
