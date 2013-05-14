/**
 * Created with IntelliJ IDEA.
 * User: babrams
 * Date: 5/14/13
 * Time: 11:27 AM
 * To change this template use File | Settings | File Templates.
 */

import com.axelarge.tomelette._
import java.io.FileReader

class RideConfig (config: CLIConfig) {
  val gpsFile = config.gpsFile
  val rideStyle = config.rideStyle
  val bikeStyle = config.bikeStyle

  private val tomlFile = config.tomlFile
  private val parser = new TomlParser()
  private val tomlConfig = try {
    parser.parse(new FileReader(tomlFile)).right.get
  } catch {
    case e:NoSuchElementException =>
      throw new Exception("Error parsing config file. Either does not exist or is not properly formatted.")
  }

  object bikeData {
    val bike = try {
      tomlConfig.getAs[String]("bike."+bikeStyle+".year").get.replaceAll("\"","") + " " + tomlConfig.getAs[String]("bike."+bikeStyle+".brand").get.replaceAll("\"","") + " " + tomlConfig.getAs[String]("bike."+bikeStyle+".model").get.replaceAll("\"","")
    } catch {
      case e: Exception => throw new Exception("Could not extract bike name components")
    }
    val weight = try {
      tomlConfig.getAs[String]("bike."+bikeStyle+".weight").get.replaceAll("\"","").toLong + tomlConfig.getAs[String]("accessories."+rideStyle+".weight").get.replaceAll("\"","").toLong
    } catch {
      case e: Exception => throw new Exception("Could not extract bike."+bikeStyle+".weight")
    }
    val wheel = try {
      tomlConfig.getAs[String]("bike."+bikeStyle+".wheel").get.replaceAll("\"","").toLong
    } catch {
      case e: Exception => throw new Exception("Could not extract bike."+bikeStyle+".wheel")
    }
    val tire = try {
      tomlConfig.getAs[String]("bike."+bikeStyle+".tire").get.replaceAll("\"","").toLong
    } catch {
      case e: Exception => throw new Exception("Could not extract bike."+bikeStyle+".tire")
    }
    val crankLength = try {
      tomlConfig.getAs[String]("bike."+bikeStyle+".crank").get.replaceAll("\"","").toLong
    } catch {
      case e: Exception => throw new Exception("Could not extract bike."+bikeStyle+".crank")
    }
    val crankset = try {
      tomlConfig.getAs[List[String]]("bike."+bikeStyle+".crankset").get.map(_.replaceAll("\"","").toLong)
    } catch {
      case e: Exception => throw new Exception("Could not extract bike."+bikeStyle+".crankset")
    }
    val cassette = try {
      tomlConfig.getAs[List[String]]("bike."+bikeStyle+".cassette").get.map(_.replaceAll("\"","").toLong)
    } catch {
      case e: Exception => throw new Exception("Could not extract bike."+bikeStyle+".cassette")
    }
    val accessories = try {
      tomlConfig.getAs[List[String]]("accessories."+rideStyle+".items").get.map(_.replaceAll("\"",""))
    } catch {
      case e: Exception => throw new Exception("Could not extract accessories."+rideStyle+".items")
    }
  }

  object riderData {
    val name = try {
      tomlConfig.getAs[String]("rider.name").get.replaceAll("\"","")
    } catch {
      case e: Exception => throw new Exception("Could not extract rider.name")
    }
    val age = try {
      tomlConfig.getAs[String]("rider.age").get.replaceAll("\"","").toLong
    } catch {
      case e: Exception => throw new Exception("Could not extract rider.age")
    }
    val weight = try {
      tomlConfig.getAs[String]("rider.weight").get.replaceAll("\"","").toLong
    } catch {
      case e: Exception => throw new Exception("Could not extract rider.weight")
    }
    val height = try {
      tomlConfig.getAs[String]("rider.height").get.replaceAll("\"","").toLong
    } catch {
      case e: Exception => throw new Exception("Could not extract rider.height")
    }
  }
}

object RideConfig {
  def apply(config: CLIConfig) =
    new RideConfig(config)
}
