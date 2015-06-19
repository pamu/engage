package models

import slick.driver.MySQLDriver.api._
import models.Models._
import java.sql.Timestamp

/**
 * Created by pnagarjuna on 27/05/15.
 */
object Tables {
  val usersTable = "users"
  class Users(tag: Tag) extends Table[User](tag, usersTable) {
    def userId = column[String]("userId")
    def email = column[String]("email")
    def password = column[String]("password")
    def timestamp = column[Timestamp]("timestamp")
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def * = (userId, email, password, timestamp, id.?) <> (User.tupled, User.unapply)
  }

  /**
  val entityTable = "entities"
  class Entities(tag: Tag) extends Table[Entity](tag, entityTable) {
    def key = column[String]("key")
    def value = column[String]("value")
    def timestamp = column[Timestamp]("timestamp")
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def * = (key, value, timestamp, id.?) <> (Entity.tupled, Entity.unapply)
  } **/

  val discussionTable = "Discussions"
  class Discussions(tag: Tag) extends Table[Discussion](tag, discussionTable) {
    def heading = column[String]("heading")
    def desc = column[String]("description")
    def authorId = column[Long]("authorId")
    def timestamp = column[Timestamp]("timestamp")
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def * = (heading, desc.?, authorId, timestamp, id.?) <> (Discussion.tupled, Discussion.unapply)

    def authorIdFk = foreignKey("authorIdUserFK", authorId, TableQuery[Users])(_.id, ForeignKeyAction.Cascade, ForeignKeyAction.Cascade)
  }
  
  val users = TableQuery[Users]
  //val entities = TableQuery[Entities]
}
