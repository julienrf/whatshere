package wh.controllers

import play.api.data.Form
import play.api.data.Forms.{single, nonEmptyText}
import play.api.mvc.Security.AuthenticatedBuilder
import play.api.mvc.{Results, Action, Controller}

class Authentication extends Controller {

  def login(redirectTo: String) = Action {
    Ok(wh.html.login(redirectTo))
  }

  val authenticationForm = Form(single("username" -> nonEmptyText))

  def authenticate(redirectTo: String) = Action { implicit request =>
    authenticationForm.bindFromRequest().fold(
      error => BadRequest(wh.html.login(redirectTo)),
      username => Redirect(redirectTo).addingToSession(Authentication.authKey -> username)
    )
  }

  val logout = Action { implicit request =>
    Redirect(routes.WhatsHere.list()).removingFromSession(Authentication.authKey)
  }

}

object Authentication {

  val authKey = "user_id"

  type AuthenticatedRequest[Payload] = play.api.mvc.Security.AuthenticatedRequest[Payload, String]

  object Authenticated extends AuthenticatedBuilder[String](
    userinfo = _.session.get(authKey),
    onUnauthorized = request => Results.Redirect(routes.Authentication.login(request.uri))
  )

}