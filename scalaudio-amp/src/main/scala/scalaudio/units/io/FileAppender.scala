package scalaudio.units.io

import java.io.FileWriter

import signalz.SequentialState

import scalaudio.core.AudioContext

/**
  * Created by johnmcgill on 6/6/16.
  */
case class FileAppender[A](filename: String,
                           a2String: A => String,
                           analysisTypeExtension: String = "txt")
  extends SequentialState[Option[A], AudioContext] {
  // Why make this option.. can allow that to be determined by user?

  val fw = new FileWriter(s"$filename.$analysisTypeExtension.txt", true)

  //  fw.close() // TODO: Close callback?

  override def nextState(maybeData: Option[A])(implicit context: AudioContext): Option[A] = {
    maybeData.foreach(data => fw.write(a2String(data) + "\n"))
    maybeData
  }

  //vals.mkString(",")
}