/**
 * Created with IntelliJ IDEA.
 * User: babrams
 * Date: 5/13/13
 * Time: 1:24 PM
 * To change this template use File | Settings | File Templates.
 */

import scopt.immutable.OptionParser

object CLI {
  def main(args: Array[String]) {
    val parser = new OptionParser[CLIConfig]("CLI", "0.1") {
      def options = Seq(
        arg("xmlFile", "File for processing." +
          "\nDefault: %s".format(CLIConfigDefaults.gpsFile)){
          (file:String, c:CLIConfig) =>
            c.copy(gpsFile = file)
        },
        opt("b", "bikeStyle", "Style of bike" +
          "\nDefault: %s".format(CLIConfigDefaults.bikeStyle)){
          (bike:String, c:CLIConfig) =>
            c.copy(rideStyle = bike)
        },
        opt("r", "rideStyle", "Style of ride" +
          "\nDefault: %s".format(CLIConfigDefaults.rideStyle)){
          (ride:String, c:CLIConfig) =>
            c.copy(rideStyle = ride)
        },
        opt("c", "config", "TOML file for configuration" +
          "\nDefault: %s".format(CLIConfigDefaults.tomlFile)){
          (file:String, c:CLIConfig) =>
            c.copy(tomlFile = file)
        },
        help("h", "help", "This help message.")
      )
    }

    parser.parse(args, CLIConfig()) map { config =>
      val rideData = new RideConfig(config)
      val gpsData = new ProcGPXFile(rideData)
      println(rideData.riderData.name, rideData.riderData.age, rideData.riderData.weight)
      println(rideData.bikeData.bike, rideData.bikeData.weight)
      println(gpsData.gpsRoute.routeName,
               gpsData.gpsRoute.startTime,
               gpsData.gpsDerived.totalDist,
               gpsData.gpsDerived.totalTime,
               gpsData.gpsDerived.totalPosEle,
               gpsData.gpsDerived.totalNegEle,
               gpsData.gpsCalculated.aveSpeed)
      gpsData.gpsDerived.sinceLast
        .filter{case (k,v) => v("time")>30.0}
        .foreach{case (k,v) => {println(v);println(gpsData.gpsRoute.points(k))}}

    }
  }
}
