package models

import slick.driver.MySQLDriver.api._

/**
 * Created by pnagarjuna on 27/05/15.
 */
object DB {
  lazy val db = Database.forURL(
      url  = "jdbc:mysql://localhost:3306/engage", 
      driver = "com.mysql.jdbc.Driver",
      user = "root",
      password = "root")
}
