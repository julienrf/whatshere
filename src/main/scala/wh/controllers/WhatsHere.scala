package wh.controllers

import play.api.data.Form
import play.api.data.Forms.{single, number}
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.Controller
import wh.controllers.Authentication.Authenticated
import wh.models.{Location, Locations, Ratings}


class WhatsHere(val messagesApi: MessagesApi) extends Controller with I18nSupport {

  /** (temporary) Show all registered locations */
  val list = Authenticated {
    Ok(wh.html.list())
  }

  /** Show the list of recommended locations for the current user */
  val recommendations = Authenticated {
    val list = Seq("Chez moi", "Chez ma maman", "Dans un bar")
    Ok(wh.html.recommandation(list))
  }

  /** Show the details of one location, so that the user can like it */
  def show(locationName: String) = Authenticated {
    Locations.current.findByName(locationName) match {
      case None => NotFound
      case Some(location) => Ok(wh.html.show(locationName, rateForm))
    }
  }

  val rateForm = Form(single("rate" -> number(min = 1, max = 5)))

  /** Add the fact that the user rated the given location */
  def rate(locationName: String) = Authenticated { implicit request =>
    Locations.current.findByName(locationName) match {
      case None => NotFound
      case Some(location) =>
        rateForm.bindFromRequest().fold(
          errors => BadRequest(wh.html.show(locationName, rateForm)),
          rate => {
            Ratings.current.single.transformIfDefined(Function.unlift((ratings: Ratings) => ratings.addRating(request.user, location, rate)))
            Redirect(routes.WhatsHere.show(locationName))
          }
        )
    }
  }

}
