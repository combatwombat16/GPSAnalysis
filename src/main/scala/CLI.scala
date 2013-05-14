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
          "\nDefault: %s".format(CLIConfigDefaults.xmlFile)){
          (file:String, c:CLIConfig) =>
            c.copy(xmlFile = file)
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
      val gpsData = new ProcGPXFile(config.xmlFile)

      println(gpsData.gpsRoute.routeName,
               gpsData.gpsRoute.startTime,
               gpsData.gpsDerived.totalDist,
               gpsData.gpsDerived.totalTime,
               gpsData.gpsDerived.totalPosEle,
               gpsData.gpsDerived.totalNegEle)
      (1 until 100).foreach{i => println(gpsData.gpsRoute.points(i),gpsData.gpsDerived.sinceLast(i))}

    }
  }
}
