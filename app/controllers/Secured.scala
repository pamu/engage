package controllers

import constants.Constants
import models.DAO
import play.api.mvc._

import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import models.Models._

/**
 * Created by pnagarjuna on 27/05/15.
 */
trait Secured {
  def userId(requestHeader: RequestHeader) = requestHeader.session.get(Constants.userId)
  def onUnauthorized(requestHeader: RequestHeader) = Results.Redirect(routes.Auth.login)
  def withAuth[A](parser: BodyParser[A])(f: => String => Request[A] => Future[Result]) =
    Security.Authenticated(userId, onUnauthorized) { userId =>
      Action.async(parser)(request => f(userId)(request))
    }
  def withUser[A](parser: BodyParser[A])(f: User => Request[A] => Future[Result]) = withAuth(parser) { userId => implicit request =>
    DAO.getUser(userId).flatMap(user => f(user)(request)).recover{case throwable: Throwable => Results.BadRequest("Bad Request")}
  }
}
