package weka

import java.io.File
import java.util

import org.scalatest.{FlatSpec, Matchers}
import weka.core.{DenseInstance, Attribute, Instance, Instances}
import weka.core.converters.ArffSaver
import weka.core.converters.ConverterUtils.DataSource
import scala.collection.JavaConversions._
import scala.collection.JavaConverters._
/**
  * Created by johnmcgill on 1/22/17.
  *
  */
class ArffFileWriter extends FlatSpec with Matchers{

    "weka lib" should "generate a neato ARFF file" in {

      val source = new DataSource("/some/where/data.arff")
      val data = source.getDataSet()

      // setting class attribute if the data format does not provide this information
      // For example, the XRFF format saves the class attribute information as well
      if (data.classIndex() == -1)
        data.setClassIndex(data.numAttributes() - 1)

      println(data)
    }

  "dense instance" should "print all that jive in the test method" in {
    DenseInstance.main(Array.empty)
  }

  "arff saver" should "save a set of instances with proper headers" in {
    // Create numeric attributes "length" and "weight"
    val length: Attribute = new Attribute("length")
    val weight: Attribute = new Attribute("weight")

    // Create vector to hold nominal values "first", "second", "third"
    val my_nominal_values = List("first", "second", "third")

    // Create nominal attribute "position"
    val position: Attribute = new Attribute("position", my_nominal_values.asJava)

    // Create vector of the above attributes
    val attributes: List[Attribute] = List(length, weight, position)

    // Create the empty dataset "race" with above attributes
    val race: Instances = new Instances("race", new util.ArrayList(attributes), 0)

    // Make position the class attribute
    race.setClassIndex(position.index)

    // Create empty instance with three attribute values
    val inst: Instance = new DenseInstance(3)

    // Set instance's values for the attributes "length", "weight", and
    // "position"
    inst.setValue(length, 5.3)
    inst.setValue(weight, 300)
    inst.setValue(position, "first")

    // Set instance's dataset to be the dataset "race"
    inst.setDataset(race)

    // Get a ARFF goin. grrrrr!
    val arffSaver = new ArffSaver()

    arffSaver.setFile(new File("/Users/johnmcgill/Documents/weka-test.arff"))

    race.add(inst)
    arffSaver.setInstances(race)

    arffSaver.writeBatch()

//    arffSaver.writeIncremental(inst)

  }
}
