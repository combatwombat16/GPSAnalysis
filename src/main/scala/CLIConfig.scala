/**
 * Created with IntelliJ IDEA.
 * User: babrams
 * Date: 5/13/13
 * Time: 1:26 PM
 * To change this template use File | Settings | File Templates.
 */
case class CLIConfig (
  xmlFile: String = CLIConfigDefaults.xmlFile
)

object CLIConfigDefaults {
  val xmlFile = "/home/babrams/Dropbox/CyclingRoutesGPX/Leucadian20_20130504.gpx"
}
