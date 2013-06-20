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

class ProcGPXFile (config: RideConfig) {
  val dateTimePattern = "YYYY-MM-dd'T'HH:mm:ssZ"
  val dateFormat = DateTimeFormat.forPattern(dateTimePattern)

  val inFile = XML.loadFile(config.gpsFile)

  object gpsRoute {
    val routeName = (inFile \ "trk" \ "name").text
    val startTime = (inFile \ "trk" \ "time").text

    private var counter = 0.0
    val points = (inFile \ "trk" \ "trkseg" \ "trkpt").map{ node =>
      counter += 1.0
      (counter, Map(
          "lat" -> (node \ "@lat").text,
          "lon" -> (node \ "@lon").text,
          "ele" -> (node \ "ele").text,
          "time" -> (node \ "time").text
        )
      )
    }.toMap
  }
  //TODO: Add corrections for stops > 30 seconds.  Possibly figure distance and speed of surrounding points, average and interpolate.
  // Set a simple check if time > 30, set to 30
  object gpsDerived {
    val sinceLast: Map[Double,Map[String, Double]] = gpsRoute.points.keys.toList.sorted.map{case point =>
      point match {
        case 1.0 => (point, Map("ele" -> "%.2f".format(0.0).toDouble,
                                 "dist" -> "%.2f".format(0.0).toDouble,
                                 "bearing" -> "%.2f".format(0.0).toDouble,
                                 "time" -> "%.2f".format(0.0).toDouble))
        case _ => (point, Map("ele" -> "%.2f".format(gpsRoute.points(point)("ele").toDouble - gpsRoute.points(point - 1)("ele").toDouble).toDouble,
                               "dist" -> "%.2f".format(calcDistance(gpsRoute.points(point - 1)("lat").toDouble, gpsRoute.points(point - 1)("lon").toDouble,
                                                                     gpsRoute.points(point)("lat").toDouble, gpsRoute.points(point)("lon").toDouble)).toDouble,
                               "bearing" -> "%.2f".format(calcBearing(gpsRoute.points(point - 1)("lat").toDouble, gpsRoute.points(point - 1)("lon").toDouble,
                                                                       gpsRoute.points(point)("lat").toDouble, gpsRoute.points(point)("lon").toDouble)).toDouble,
                               "time" -> {(new Duration(dateFormat.parseDateTime(gpsRoute.points(point - 1)("time")), dateFormat.parseDateTime(gpsRoute.points(point)("time")))).getStandardSeconds.toDouble match {
                                              case x if x < 60.0 => x
                                              case _ => 30.0
                                            }
                                         }
                              )
                  )
      }
    }.toMap

    val totalDist = sinceLast.map{case (point,data) => data("dist")}.toList.sum
    val totalTime = sinceLast.map{case (point,data) => data("time")}.toList.sum
    val totalPosEle = sinceLast.map{case (point,data) => data("ele")}.toList.filter(x=>x>0).sum
    val totalNegEle = sinceLast.map{case (point,data) => data("ele")}.toList.filter(x=>x<0).sum
  }

  object gpsCalculated {
    val aveSpeed = (gpsDerived.totalDist / gpsDerived.totalTime) / 5280 * 60 * 60
  }

  def calcDistance (lat1: Double, lon1: Double, lat2:Double, lon2:Double): Double = {
    val R = 6371 // km
    val dLat = (lat2 - lat1).toRadians
    val dLon = (lon2 - lon1).toRadians
    val lat1rad = lat1.toRadians
    val lat2rad = lat2.toRadians

    val a = Math.sin(dLat/2) * Math.sin(dLat/2) +
      Math.sin(dLon/2) * Math.sin(dLon/2) * Math.cos(lat1rad) * Math.cos(lat2rad)
    val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a))
    if(R * c * 3280.84 > 10000) 1.0 else R * c * 3280.84
  }

  def calcBearing (lat1: Double, lon1: Double, lat2:Double, lon2:Double): Double = {
    val R = 6371 // km
    val dLat = (lat2 - lat1).toRadians
    val dLon = (lon2 - lon1).toRadians
    val lat1rad = lat1.toRadians
    val lat2rad = lat2.toRadians

    val y = Math.sin(dLon) * Math.cos(lat2rad)
    val x = Math.cos(lat1rad)*Math.sin(lat2rad) -
      Math.sin(lat1rad)*Math.cos(lat2rad)*Math.cos(dLon)
    Math.atan2(y, x).toDegrees
  }
}

object ProcGPXFile {
  def apply(config: RideConfig) =
    new ProcGPXFile(config)
}
