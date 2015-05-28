package models

import java.sql.Timestamp

/**
 * Created by pnagarjuna on 27/05/15.
 */
object Models {
  case class User(userId: String, email: String, password: String, timestamp: Timestamp, id: Option[Long] = None)
  case class Entity(key: String, value: String, timestamp: Timestamp, id: Option[Long] = None)
}
