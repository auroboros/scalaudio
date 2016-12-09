package scalaudio.core.engine

/**
  * Created by johnmcgill on 12/8/16.
  */
case class VirtualBidiDevice(frameRate: Int,
                        samplesPerFrame: Int,
                        inputDeviceId: Option[Int] = Some(ScalaudioDeviceManager.defaultInputDeviceID),
                        outputDeviceId: Option[Int] = Some(ScalaudioDeviceManager.defaultOutputDeviceID)) {

  private val maybeInput = inputDeviceId.map(VirtualInputDevice(frameRate, samplesPerFrame, _))
  private val maybeOutput = outputDeviceId.map(VirtualOutputDevice(frameRate, samplesPerFrame, _))

  // for simpler access + fast fail on read/write if uninitialized
  private val input = maybeInput.orNull
  private val output = maybeOutput.orNull

  def startInput() = maybeInput match {
    case None => throw new Exception("No input to start")
    case Some(i) => i.startInput()
  }

  def startOutput() = maybeOutput match {
    case None => throw new Exception("No output to start")
    case Some(o) => o.startOutput()
  }

  def startIO() = {
    startInput()
    startOutput()
  }

  def stopInput() = maybeInput.foreach(_.stopInput())
  def stopOutput() = maybeOutput.foreach(_.stop())

  def stopIO() = {
    stopInput()
    stopOutput()
  }

  def read(buffer: Array[Double]) = {
    input.read(buffer)
  }

  def write(buffer: Array[Double]) = {
    output.write(buffer)
  }
}