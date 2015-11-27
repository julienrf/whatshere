package wh.controllers

import play.api.data.Form
import play.api.data.Forms.{single, nonEmptyText}
import play.api.mvc.Security.AuthenticatedBuilder
import play.api.mvc.{Results, Action, Controller}
import wh.models.{User, Users}

class Authentication extends Controller {

  def login(redirectTo: String) = Action {
    Ok(wh.html.login(redirectTo))
  }

  val authenticationForm = Form(single("username" -> nonEmptyText))

  def authenticate(redirectTo: String) = Action { implicit request =>
    authenticationForm.bindFromRequest().fold(
      error => BadRequest(wh.html.login(redirectTo)),
      username =>
        Users.current.findByName(username) match {
          case Some(user) => Redirect(redirectTo).addingToSession(Authentication.authKey -> username)
          case None       => BadRequest(wh.html.login(redirectTo))
        }
    )
  }

  val logout = Action { implicit request =>
    Redirect(routes.WhatsHere.list()).removingFromSession(Authentication.authKey)
  }

}

object Authentication {

  val authKey = "user_id"

  type AuthenticatedRequest[Payload] = play.api.mvc.Security.AuthenticatedRequest[Payload, User]

  object Authenticated extends AuthenticatedBuilder[User](
    userinfo = _.session.get(authKey).flatMap(Users.current.findByName),
    onUnauthorized = request => Results.Redirect(routes.Authentication.login(request.uri))
  )

}