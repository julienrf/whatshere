package wh.models

import scala.concurrent.stm.Ref

case class Ratings(ratings: List[Rating]) {

  /**
    * @return An updated `Ratings` containing the given rating if `user` has not yet rated `location`, otherwise `None`
    */
  def addRating(user: User, location: Location, rate: Int): Option[Ratings] =
    if (ratings.exists(rating => rating.location == location && rating.user == user)) None
    else Some(copy(ratings = Rating(user, location, rate.toDouble) :: ratings))

}

object Ratings {

  val current = Ref(
    Ratings(
      List(
        Locations.current.findByName("Tour Eiffel").map(Rating(User("Julien"), _, 1)),
        Locations.current.findByName("Les Champs Élysées").map(Rating(User("Julien"), _, 0)),
        Locations.current.findByName("Le Mal Barré").map(Rating(User("Julien"), _, 4))/*,
        Locations.current.findByName("Le Mal Barré").map(Rating(User("Thomas"), _, 5))*/
      ).flatten
    )
  )

  def getRate(locationName: String): Double = {
    val rates = current.single.get.ratings
      .filter(_.location.name == locationName)
      .map(_.rate)
    if(rates.nonEmpty) rates.sum / rates.length else 0
  }

}