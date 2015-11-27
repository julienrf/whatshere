package wh

import play.api.{BuiltInComponentsFromContext, ApplicationLoader}
import play.api.ApplicationLoader.Context
import router.Routes

class Main extends ApplicationLoader {
  def load(context: Context) =
    new BuiltInComponentsFromContext(context) {
      def router = new Routes(httpErrorHandler)
    }.application
}
