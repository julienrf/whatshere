package wh.controllers

import play.api.mvc.Security.AuthenticatedBuilder
import play.api.mvc.{Action, Controller}

class Authentication extends Controller {

  val login = Action {
    Ok(wh.html.login())
  }

  val authenticate = Action {
    NotImplemented
  }

  val logout = Action { implicit request =>
    NotImplemented.removingFromSession(Authentication.authKey)
  }

}

object Authentication {

  val authKey = "user_id"

  type AuthenticatedRequest[Payload] = play.api.mvc.Security.AuthenticatedRequest[Payload, String]

  object Authenticated extends AuthenticatedBuilder[String](_.session.get(authKey))

}