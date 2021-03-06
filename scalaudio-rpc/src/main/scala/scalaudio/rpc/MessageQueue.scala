package scalaudio.rpc

import org.slf4j.LoggerFactory

import scala.collection.mutable

/**
  * Created by johnmcgill on 6/9/16.
  */
object MessageQueue {
  val logger = LoggerFactory.getLogger(this.getClass)

  private val doublesMap = mutable.Map.empty[String, Double]

  def updateDoublesMap(key: String, value: Double) = {
    doublesMap.update(key, value)
    logger.debug(s"Updated doubles map: $doublesMap")
  }

  def getDoubleValue(key: String): Option[Double] = doublesMap.get(key)

  //  doublesMap.-=()
  // what is parallel.mutable?
}
