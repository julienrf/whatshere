package wh.models

case class User(name: String)

case class Location(name: String)

case class Rating(user: User, location: Location, rate: Int) {
  require (rate >= 1 && rate <= 5)
}