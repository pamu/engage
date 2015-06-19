package controllers

import play.api.libs.json.{JsError, Json}
import play.api.mvc.{AnyContent, Controller}

import scala.concurrent.Future

import play.api.libs.concurrent.Execution.Implicits.defaultContext

object Application extends Controller with Secured {
  def index = withUser[AnyContent](parse.anyContent) { user => implicit request =>
    Future {
      //Ok(views.html.index("Engage")(request.flash))
      Redirect(routes.Application.home)
    }
  }
  def home = withUser[AnyContent](parse.anyContent) { user => implicit request =>
    Future {
      Ok(views.html.home()(request.flash))
    }
  }

  def newDiscussion() = withUser(parse.anyContent) { user => implicit request =>
    Future {
      Ok(views.html.newDiscussion()(request.flash))
    }
  }

  case class Discussion(userId: String, heading: String, description: String)
  def newPostDiscussion() = withUser(parse.json) { user => implicit request =>
    implicit val discussionFormat = Json.format[Discussion]
    Future {
      request.body.validate[Discussion].fold(
        invalid => BadRequest(JsError.toFlatJson(invalid)),
        valid => {
          //store
          Ok(Json.obj("success" -> "Discussion is posted successfully."))
        }
      )
    }
  }
}