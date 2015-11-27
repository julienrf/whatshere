package wh.models

case class User(name: String)

case class Location(name: String)

case class Rating(user: User, location: Location, rate: Double) {
  require (rate >= 0 && rate <= 5)
}