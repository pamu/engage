package controllers

import play.api.mvc.{AnyContent, Controller}

import scala.concurrent.Future

import play.api.libs.concurrent.Execution.Implicits.defaultContext

object Application extends Controller with Secured {
  def index = withUser[AnyContent](parse.anyContent) { user => implicit request =>
    Future {
      Ok(views.html.index("Engage")(request.flash))
    }
  }
  def home = withUser[AnyContent](parse.anyContent) { user => implicit request =>
    Future {
      Ok("home")
    }
  }
}