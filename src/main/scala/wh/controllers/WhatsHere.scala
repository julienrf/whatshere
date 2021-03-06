package wh.controllers

import play.api.data.Form
import play.api.data.Forms.{single, number}
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.Controller
import wh.controllers.Authentication.Authenticated
import wh.models.{Locations, Ratings, predictions}


class WhatsHere(val messagesApi: MessagesApi) extends Controller with I18nSupport {

  /** (temporary) Show all registered locations */
  val list = Authenticated { request =>
    Ok(wh.html.list(Some(request.user)))
  }

  /** Show the list of recommended locations for the current user */
  val recommendations = Authenticated { request =>
    val predictionsMap = predictions.predict(request.user, Locations.current.locations, Ratings.current.single.apply())
    val list = predictionsMap.toList.sortBy(_._2).reverse
    Ok(wh.html.recommandation(list, Some(request.user)))
  }

  /** Show the details of one location, so that the user can like it */
  def show(locationName: String) = Authenticated { request =>
    Locations.current.findByName(locationName) match {
      case None => NotFound
      case Some(location) => Ok(wh.html.show(locationName, Some(request.user)))
    }
  }

  val rateForm = Form(single("rating" -> number(min = 0, max = 5)))

  /** Add the fact that the user rated the given location */
  def rate(locationName: String) = Authenticated { implicit request =>
    Locations.current.findByName(locationName) match {
      case None => NotFound
      case Some(location) =>
        rateForm.bindFromRequest().fold(
          errors => BadRequest(wh.html.show(locationName, Some(request.user))),
          rate => {
            Ratings.current.single.transformIfDefined(Function.unlift((ratings: Ratings) => ratings.addRating(request.user, location, rate)))
            Redirect(routes.WhatsHere.show(locationName))
          }
        )
    }
  }

}
