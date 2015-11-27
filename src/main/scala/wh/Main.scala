package wh

import javax.inject.Inject

import play.api.i18n.{I18nComponents, MessagesApi}
import play.api.{BuiltInComponentsFromContext, ApplicationLoader}
import play.api.ApplicationLoader.Context
import router.Routes
import wh.controllers.{WhatsHere, Authentication}

class Main extends ApplicationLoader {
  def load(context: Context) =
    new BuiltInComponentsFromContext(context) with I18nComponents {
      def router = new Routes(httpErrorHandler, new Authentication, new WhatsHere(messagesApi))
    }.application
}
