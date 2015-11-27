package wh.models

object predictions {

  val minRate = 1.0
  val maxRate = 1.0
  val averageRate = (maxRate + minRate) / 2

  /**
    * @return Predicted ratings for a given user, locations and existing ratings.
    */
  def predict(user: User, locations: List[Location], ratings: Ratings): Map[Location, Double] = {
    // Ratings of all users, only for locations already rated rated by user
    val knownLocationsRatings: Map[User, List[(Location, Int)]] = {
      val knownLocations: Seq[Location] =
        ratings.ratings
          .filter(_.user == user)
          .map(_.location)

      val ratingsByUsers: Map[User, List[Rating]] =
        ratings.ratings.groupBy(_.user)

      ratingsByUsers.mapValues( rs =>
        rs.filter(r => knownLocations.contains(r.location))
          .map(r => (r.location, r.rate))
          .sortBy(_._1.name) // Sort locations so that they are in the same order for all users and thus can be treated like vectors
      )
    }

    knownLocationsRatings.get(user) match {
      case Some(userRatings) =>
        // Distance between user and others
        val othersRatings = knownLocationsRatings - user

        val distances: Map[User, Double] = {
          val rawDistances: Map[User, Double] =
            othersRatings.mapValues(rs => distance(userRatings.map(_._2), rs.map(_._2)))
          val minDistance = rawDistances.values.min
          val maxDistance = rawDistances.values.max
          rawDistances.mapValues(d => (d - minDistance) / (maxDistance - minDistance)) // Normalization so that each distance is a number between 0 and 1
        }

        val weightedLocations: Map[Location, Double] =
          othersRatings.to[Seq]
            .flatMap { case (other, rs) =>
              rs.map { case (location, rate) => (location, rate / (0.5 + distances.getOrElse(other, 0.5))) }
            }.groupBy(_._1)
            .mapValues(rs => rs.map(_._2).sum / rs.size)

        locations.map { location =>
          userRatings
            .find { case (l, r) => l == location }
            .map { case (l, r) => (l, r.toDouble) }
            .getOrElse {
              location -> weightedLocations.getOrElse(location, 2.5)
            }
        }.toMap
      case None =>
        // No information from current user: return an average note for all locations
        locations.map((_, averageRate)).toMap
    }
  }

  def distance[A](as1: Seq[A], as2: Seq[A])(implicit num: Numeric[A]): Double = {
    require(as1.size == as2.size)
    math.sqrt(
      as1.zip(as2)
        .map { case (a1, a2) => (num.toDouble(a1), num.toDouble(a2)) }
        .map { case (a1, a2) => (a2 - a1) * (a2 - a1) }
        .sum
    )
  }

}