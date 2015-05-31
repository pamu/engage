package global

import models.DAO
import play.api.{Logger, Application, GlobalSettings}

import scala.util.{Failure, Success}
import play.api.libs.concurrent.Execution.Implicits.defaultContext

/**
 * Created by pnagarjuna on 30/05/15.
 */
object Global extends GlobalSettings {
  override def onStop(app: Application): Unit = {
    super.onStop(app)
    println(DAO.init().value)
    /*
    onComplete {
      case Success(x) => Logger.info("created schema")
      case Failure(y) => Logger.info("schema creation failed")
    }*/
    Logger.info("App Started")
  }

  override def onStart(app: Application): Unit = {
    super.onStart(app)
    Logger.info("App Stopped")
  }
}
