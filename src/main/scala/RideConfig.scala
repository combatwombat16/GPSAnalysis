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
  private val parser = new TomlParser()
  private val tomlConfig = try {
    parser.parse(new FileReader(config.tomlFile)).right.get
  } catch {
    case e:NoSuchElementException =>
      throw new Exception("Error parsing config file. Either does not exist or is not properly formatted.")
  }

  object bikeData {
    val bike = try {
      tomlConfig.getAs[String]("bike."+config.bikeStyle+".year").get.replaceAll("\"","") + " " + tomlConfig.getAs[String]("bike."+config.bikeStyle+".brand").get.replaceAll("\"","") + " " + tomlConfig.getAs[String]("bike."+config.bikeStyle+".model").get.replaceAll("\"","")
    } catch {
      case e: Exception => throw new Exception("Could not extract bike name components")
    }
    val weight = try {
      tomlConfig.getAs[String]("bike."+config.bikeStyle+".weight").get.replaceAll("\"","").toLong + tomlConfig.getAs[String]("accessories."+config.rideStyle+".weight").get.replaceAll("\"","").toLong
    } catch {
      case e: Exception => throw new Exception("Could not extract bike."+config.bikeStyle+".weight")
    }
    val wheel = try {
      tomlConfig.getAs[String]("bike."+config.bikeStyle+".wheel").get.replaceAll("\"","").toLong
    } catch {
      case e: Exception => throw new Exception("Could not extract bike."+config.bikeStyle+".wheel")
    }
    val tire = try {
      tomlConfig.getAs[String]("bike."+config.bikeStyle+".tire").get.replaceAll("\"","").toLong
    } catch {
      case e: Exception => throw new Exception("Could not extract bike."+config.bikeStyle+".tire")
    }
    val crankLength = try {
      tomlConfig.getAs[String]("bike."+config.bikeStyle+".crank").get.replaceAll("\"","").toLong
    } catch {
      case e: Exception => throw new Exception("Could not extract bike."+config.bikeStyle+".crank")
    }
    val crankset = try {
      tomlConfig.getAs[List[String]]("bike."+config.bikeStyle+".crankset").get.map(_.replaceAll("\"","").toLong)
    } catch {
      case e: Exception => throw new Exception("Could not extract bike."+config.bikeStyle+".crankset")
    }
    val cassette = try {
      tomlConfig.getAs[List[String]]("bike."+config.bikeStyle+".cassette").get.map(_.replaceAll("\"","").toLong)
    } catch {
      case e: Exception => throw new Exception("Could not extract bike."+config.bikeStyle+".cassette")
    }
    val accessories = try {
      tomlConfig.getAs[List[String]]("accessories."+config.rideStyle+".items").get.map(_.replaceAll("\"",""))
    } catch {
      case e: Exception => throw new Exception("Could not extract accessories."+config.rideStyle+".items")
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
