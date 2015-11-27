package wh

import _root_.controllers.Assets
import play.api.i18n.I18nComponents
import play.api.{BuiltInComponentsFromContext, ApplicationLoader}
import play.api.ApplicationLoader.Context
import router.Routes
import wh.controllers.{WhatsHere, Authentication}

class Main extends ApplicationLoader {
  def load(context: Context) =
    new BuiltInComponentsFromContext(context) with I18nComponents {
      def router = new Routes(httpErrorHandler, new Authentication, new WhatsHere(messagesApi), new Assets(httpErrorHandler))
    }.application
}
