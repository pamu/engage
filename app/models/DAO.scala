package models

import scala.concurrent.Future
import models.Models._
import slick.driver.MySQLDriver.api._

import play.api.libs.concurrent.Execution.Implicits.defaultContext

/**
 * Created by pnagarjuna on 27/05/15.
 */
object DAO {
  def init() = {
    import Tables._
    val init = DBIO.seq((users.schema ++ entities.schema).create)
    DB.db.run(init)
  }
  def getUser(userId: String): Future[User] = {
    val q = for(user <- Tables.users.filter(_.userId === userId)) yield user
    DB.db.run(q.result).map(_ head)
  }
}
