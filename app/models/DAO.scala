package models

import java.security.MessageDigest
import java.sql.Timestamp
import java.util.Date

import utils.Utils

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
  def getUserWithEmail(email: String): Future[User] = {
    val q = for(user <- Tables.users.filter(_.email === email)) yield user
    DB.db.run(q.result).map(_ head)
  }
  def auth(email: String, password: String): Future[Boolean]  = {
    val q = for(user <- Tables.users.filter(_.email === email).filter(_.password === password)) yield user
    DB.db.run(q.exists.result)
  }
  def getAuthUser(email: String, password: String): Future[User] = {
    val q = for(user <- Tables.users.filter(_.email === email).filter(_.password === password)) yield user
    DB.db.run(q.result).map(_ head)
  }
  def exists(email: String): Future[Boolean] = {
    val q = for(user <- Tables.users.filter(_.email === email)) yield user
    DB.db.run(q.exists.result)
  }
  def createUser(email: String, password: String): Future[Unit] = {
    val timestamp = new Timestamp(new Date().getTime())
    val q = DBIO.seq(Tables.users += User(s"${Utils.randomStr}${timestamp.getTime}", email, password, timestamp))
    DB.db.run(q.transactionally)
  }
}
