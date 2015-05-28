package controllers

import play.api.mvc.{Action, Controller}

/**
 * Created by pnagarjuna on 27/05/15.
 */
object Auth extends Controller with Secured {
  def login = Action {
    Ok("")
  }
}
