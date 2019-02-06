package pandora.core

import java.time.format.DateTimeFormatter
import java.time.{Instant, ZoneId}

package object time {

  private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ").withZone(ZoneId.systemDefault())

  def formatDateFromInstant(t: Long): String = {
    val instant = Instant.ofEpochMilli(t)
    formatter.format(instant)
  }

}
