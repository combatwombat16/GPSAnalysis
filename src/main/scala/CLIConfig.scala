/**
 * Created with IntelliJ IDEA.
 * User: babrams
 * Date: 5/13/13
 * Time: 1:26 PM
 * To change this template use File | Settings | File Templates.
 */
case class CLIConfig (
  bikeStyle: String = CLIConfigDefaults.bikeStyle,
  rideStyle: String = CLIConfigDefaults.rideStyle,
  xmlFile: String = CLIConfigDefaults.xmlFile,
  tomlFile: String = CLIConfigDefaults.tomlFile
)

object CLIConfigDefaults {
  val bikeStyle = "road"
  val rideStyle = "extended"
  val xmlFile = "/home/babrams/Dropbox/CyclingRoutesGPX/Leucadian20_20130504.gpx"
  val tomlFile = "/home/babrams/repo/GPSAnalysis/src/resources/RiderData.toml"
}
