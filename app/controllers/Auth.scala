package controllers

import constants.Constants
import models.DAO
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.Messages
import play.api.mvc.{Action, Controller}

import scala.concurrent.Future

import play.api.libs.concurrent.Execution.Implicits.defaultContext

/**
 * Created by pnagarjuna on 27/05/15.
 */
object Auth extends Controller with Secured {
  val loginForm = Form(
    tuple(
      "email" -> email,
      "password" -> nonEmptyText(minLength = 6, maxLength = 25)
    )
  )

  def login = Action { implicit request =>
    request.session.get(Constants.userId).map(email => Redirect(routes.Application.home)).getOrElse(Ok(views.html.login(loginForm)(request.flash)))
  }

  def loginSubmit = Action.async { implicit request =>
    loginForm.bindFromRequest().fold(
      hasErrors => Future(BadRequest(views.html.login(hasErrors)(request.flash))),
      success => {
        DAO.getAuthUser(success._1, success._2).map(result => {
          Redirect(routes.Application.home()).withSession(Constants.userId -> result.userId)
        }).recover {case t => Redirect(routes.Auth.login).withNewSession.flashing("failure" -> "Login Failed, Please Signup.")}
      }
    )
  }

  val signupForm = Form(
    tuple(
      "email" -> email,
      "passwords" -> tuple(
        "first_time" -> nonEmptyText(minLength = 6, maxLength = 25),
        "second_time" -> nonEmptyText
      )
    )
  )

  def signup = Action { implicit request =>
    request.session.get(Constants.userId).map(email => Redirect(routes.Application.home)).getOrElse(Ok(views.html.signup(signupForm)(request.flash)))
  }

  //save database calls. Avoid database calls if necessary
  def signupSubmit = Action.async { implicit request =>
    signupForm.bindFromRequest().fold(
      hasErrors => Future(BadRequest(views.html.signup(hasErrors)(request.flash))),
      success => {
        val email = success._1
        val password = success._2._1
        val password_2 = success._2._2
        if (password == password_2) {
          DAO.exists(email).flatMap { exists =>
            if (exists) Future(Redirect(routes.Auth.signup()).withNewSession.flashing("failure" -> "Email is taken."))
            else {
              DAO.createUser(email, password)
                .map(_ => Redirect(routes.Auth.login).flashing("success" -> "Signup successful."))
                .recover {case t => Redirect(routes.Auth.signup()).withNewSession.flashing("failure" -> "Signup Failed. User cannot be created.")}
            }
          }
        }else {
          Future(Redirect(routes.Auth.signup()).withNewSession.flashing("success" -> "Passwords do not match."))
        }
        }
    )
  }

  def logout() = Action { implicit request =>
    Redirect(routes.Auth.login).withNewSession.flashing("success" -> "logged out.")
  }
}
