package wh.models

import scala.concurrent.stm.Ref

case class Ratings(ratings: List[Rating]) {

  /**
    * @return An updated `Ratings` containing the given rating if `user` has not yet rated `location`, otherwise `None`
    */
  def addRating(user: User, location: Location, rate: Int): Option[Ratings] =
    if (ratings.exists(rating => rating.location == location && rating.user == user)) None
    else Some(copy(ratings = Rating(user, location, rate) :: ratings))

}

object Ratings {

  val current = Ref(Ratings(Nil))

  def getRate(locationName: String): Int = {
    println(current.single.get.ratings)
    val rates = current.single.get.ratings
      .filter(_.location.name.toLowerCase == locationName.toLowerCase().replaceAll("&", " "))
      .map(_.rate)
    println(rates)
    if(rates.nonEmpty) rates.sum / rates.length else 0
  }

}