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
        help("h", "help", "This help message.")
      )
    }

    parser.parse(args, CLIConfig()) map { config =>
      val gpsData = new ProcGPXFile(config.xmlFile)

      println(gpsData.gpsRoute.routeName,
               gpsData.gpsRoute.startTime,
               gpsData.gpsRoute.totalDist,
               gpsData.gpsRoute.totalTime,
               gpsData.gpsRoute.totalPosEle,
               gpsData.gpsRoute.totalNegEle)
      (1 until 100).foreach{i => println(gpsData.gpsRoute.points(i),gpsData.gpsRoute.sinceLast(i))}

    }
  }
}
