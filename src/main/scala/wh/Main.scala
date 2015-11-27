package wh

import play.api.{BuiltInComponentsFromContext, ApplicationLoader}
import play.api.ApplicationLoader.Context
import router.Routes
import wh.controllers.{WhatsHere, Authentication}

class Main extends ApplicationLoader {
  def load(context: Context) =
    new BuiltInComponentsFromContext(context) {
      def router = new Routes(httpErrorHandler, new Authentication, new WhatsHere)
    }.application
}
