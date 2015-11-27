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

  val current =
    Ref(
      Ratings(
        List(
          Rating(User("Julien"), Location("Tour Eiffel"), 1),
          Rating(User("Julien"), Location("Le Mal Barré"), 4),
          Rating(User("Thomas"), Location("Le Mal Barré"), 5)
       )
     )
    )

  def getRate(locationName: String): Double = {
    val rates = current.single.get.ratings
      .filter(_.location.name == locationName)
      .map(_.rate)
    if(rates.nonEmpty) rates.sum / rates.length else 0
  }

}