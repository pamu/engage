package controllers

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
    request.session.get("email").map(email => Redirect(routes.Application.home)).getOrElse(Ok(views.html.login(loginForm)(request.flash)))
  }

  def loginSubmit = Action.async { implicit request =>
    loginForm.bindFromRequest().fold(
      hasErrors => Future(BadRequest(views.html.login(hasErrors)(request.flash))),
      success => {
        DAO.auth(success._1, success._2).map(result => {
          if (result) {
            Redirect(routes.Application.home()).withSession("email" -> success._1)
          } else {
            Redirect(routes.Auth.login).flashing("failure" -> Messages("error.login_failed"))
          }
        }).recover {case t => Redirect(routes.Auth.login).flashing("failure" -> Messages("error.login_failed"))}
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
    request.session.get("email").map(email => Redirect(routes.Application.home)).getOrElse(Ok(views.html.signup(signupForm)(request.flash)))
  }

  def signupSubmit = Action.async { implicit request =>
    signupForm.bindFromRequest().fold(
      hasErrors => Future(BadRequest(views.html.signup(hasErrors)(request.flash))),
      success => {
        val email = success._1
        val password = success._2._1
        val password_2 = success._2._2
        if (password == password_2) {
          DAO.createUser(email, password)
            .map(_ => Redirect(routes.Auth.login).flashing("success" -> "Signup successful."))
            .recover {case t => Redirect(routes.Auth.signup()).flashing("failure" -> "Signup Failed")}
        } else {
          Future(Redirect(routes.Auth.signup()).flashing("success" -> "Signup Failed."))
        }
      }
    )
  }

  def logout() = Action { implicit request =>
    Redirect(routes.Auth.login).withNewSession.flashing("success" -> "logged out.")
  }
}
