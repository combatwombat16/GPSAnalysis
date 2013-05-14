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
  gpsFile: String = CLIConfigDefaults.gpsFile,
  tomlFile: String = CLIConfigDefaults.tomlFile
)

object CLIConfigDefaults {
  val bikeStyle = "road"
  val rideStyle = "extended"
  val gpsFile = "/home/babrams/Dropbox/CyclingRoutesGPX/Leucadian20_20130504.gpx"
  val tomlFile = "/home/babrams/repo/GPSAnalysis/src/resources/RiderData.toml"
}
