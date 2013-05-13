/**
 * Created with IntelliJ IDEA.
 * User: babrams
 * Date: 5/13/13
 * Time: 10:40 AM
 * To change this template use File | Settings | File Templates.
 */

import scala.xml._
import scala.math.{toRadians, toDegrees}
import org.joda.time.Duration
import org.joda.time.format.DateTimeFormat

class ProcGPXFile (inData: String) {
  val dateTimePattern = "YYYY-MM-dd'T'HH:mm:ssZ"
  val dateFormat = DateTimeFormat.forPattern(dateTimePattern)

  val inFile = XML.loadFile(inData)

  object gpsRoute {
    val startTime = (inFile \ "metadata" \ "time").text
    val routeName = (inFile \ "trk" \ "name").text

    private var counter = 0
    val points = (inFile \ "trk" \ "trkseg" \ "trkpt").map{ node =>
      counter += 1
      (counter, Map(
          "lat" -> (node \ "@lat").text,
          "lon" -> (node \ "@lon").text,
          "ele" -> (node \ "ele").text,
          "time" -> (node \ "time").text
        )
      )
    }.toMap

    val sinceLast = points.keys.map{case point =>
      point match {
        case 1 => (point, Map("ele" -> "%.2f".format(0.0).toDouble,
                      "dist" -> "%.2f".format(0.0).toDouble,
                      "bearing" -> "%.2f".format(0.0).toDouble,
                      "time" -> "%.2f".format(0.0).toDouble))
        case _ => (point, Map("ele" -> "%.2f".format(points(point)("ele").toDouble - points(point - 1)("ele").toDouble).toDouble,
                      "dist" -> "%.2f".format(calcDistance(points(point - 1)("lat").toDouble, points(point - 1)("lon").toDouble,
                                                       points(point)("lat").toDouble, points(point)("lon").toDouble)).toDouble,
                      "bearing" -> "%.2f".format(calcBearing(points(point - 1)("lat").toDouble, points(point - 1)("lon").toDouble,
                                                       points(point)("lat").toDouble, points(point)("lon").toDouble)).toDouble,
                      "time" -> (new Duration(dateFormat.parseDateTime(points(point - 1)("time")), dateFormat.parseDateTime(points(point)("time")))).getStandardSeconds.toDouble))
      }
    }.toMap

    val totalDist = sinceLast.map{case (point,data) => data("dist")}.toList.sum
    val totalTime = sinceLast.map{case (point,data) => data("time")}.toList.sum
    val totalPosEle = sinceLast.map{case (point,data) => data("ele")}.toList.filter(x=>x>0).sum
    val totalNegEle = sinceLast.map{case (point,data) => data("ele")}.toList.filter(x=>x<0).sum
  }

  def calcDistance (lat1: Double, lon1: Double, lat2:Double, lon2:Double): Double = {
    val R = 6371; // km
    val dLat = (lat2 - lat1).toRadians
    val dLon = (lon2 - lon1).toRadians
    val lat1rad = lat1.toRadians
    val lat2rad = lat2.toRadians

    val a = Math.sin(dLat/2) * Math.sin(dLat/2) +
      Math.sin(dLon/2) * Math.sin(dLon/2) * Math.cos(lat1rad) * Math.cos(lat2rad)
    val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a))
    return R * c * 3280.84
  }

  def calcBearing (lat1: Double, lon1: Double, lat2:Double, lon2:Double): Double = {
    val R = 6371; // km
    val dLat = (lat2 - lat1).toRadians
    val dLon = (lon2 - lon1).toRadians
    val lat1rad = lat1.toRadians
    val lat2rad = lat2.toRadians

    val y = Math.sin(dLon) * Math.cos(lat2rad)
    val x = Math.cos(lat1rad)*Math.sin(lat2rad) -
      Math.sin(lat1rad)*Math.cos(lat2rad)*Math.cos(dLon)
    return Math.atan2(y, x).toDegrees
  }
}

object ProcGPXFile {
  def apply(inData: String) =
    new ProcGPXFile(inData)
}
