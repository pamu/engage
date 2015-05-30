package controllers

import play.api.mvc.Controller

import scala.concurrent.Future

import play.api.libs.concurrent.Execution.Implicits.defaultContext

object Application extends Controller with Secured {
  def index = withUser(parse.anyContent) { user => implicit request =>
    Future {
      Ok(views.html.index("Engage"))
    }
  }
}