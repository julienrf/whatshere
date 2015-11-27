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

}