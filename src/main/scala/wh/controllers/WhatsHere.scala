package wh.controllers

import play.api.mvc.Controller
import wh.controllers.Authentication.Authenticated

class WhatsHere extends Controller {

  /** (temporary) Show all registered locations */
  val list = Authenticated {
    val list = Seq("Chez moi", "Chez ma maman", "Dans un bar")
    Ok(wh.html.list(list))
  }

  /** Show the list of recommended locations for the current user */
  val recommendations = Authenticated {
    val list = Seq("Chez moi", "Chez ma maman", "Dans un bar")
    Ok(wh.html.recommandation(list))
  }

  /** Show the details of one location, so that the user can like it */
  def show(locationName: String) = Authenticated {

    Ok(wh.html.show(locationName))
  }

  /** Add the fact that the user rated the given location */
  def rate(locationName: String) = Authenticated {
    NotImplemented
  }

}
