package wh.models

object predictions {

  val minRate = 0.0
  val maxRate = 5.0
  val neutralRate = (maxRate - minRate) / 2 + minRate

  /**
    * @return Predicted ratings for a given user, locations and existing ratings.
    */
  def predict(user: User, locations: List[Location], _ratings: Ratings): Map[Location, Double] = {
    val ratings =
      _ratings.copy(locations.map(l => Rating(User("$factice"), l, neutralRate)) ++ _ratings.ratings)

    // Ratings of all users, only for locations already rated rated by user
    val knownLocationsRatings: Map[User, List[(Location, Double)]] = {
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

        val distances: Map[User, Double] =
          normalize(othersRatings.mapValues(rs => distance(userRatings.map(_._2), rs.map(_._2))))

        val weightedLocations: Map[Location, Double] =
          normalize(
            othersRatings.to[Seq]
              .flatMap { case (other, rs) =>
                rs.map { case (location, rate) => (location, rate / (0.5 + distances.getOrElse(other, 0.5))) }
              }.groupBy(_._1)
              .mapValues(rs => rs.map(_._2).sum / rs.size),
            min = 1,
            max = 5
          )

        locations.map { location =>
          userRatings
            .find { case (l, r) => l == location }
            .map { case (l, r) => (l, r.toDouble) }
            .getOrElse {
              location -> weightedLocations.getOrElse(location, neutralRate)
            }
        }.toMap
      case None =>
        // No information from current user: return an average note for all locations
        locations.map((_, neutralRate)).toMap
    }
  }

  def distance[A](as1: Seq[Double], as2: Seq[Double]): Double = {
    require(as1.size == as2.size)
    math.sqrt(
      as1.zip(as2)
        .map { case (a1, a2) => (a2 - a1) * (a2 - a1) }
        .sum
    )
  }

  def normalize[A](map: Map[A, Double], min: Double = 0, max: Double = 1): Map[A, Double] = {
    def scale(n: Double) = n * (max - min) + min
    if (map.values.to[Seq].distinct.size <= 1) map.mapValues(_ => scale(0.5)) else {
      val minValue = map.values.min
      val maxValue = map.values.max
      map.mapValues(x => scale((x - minValue) / (maxValue - minValue)))
    }
  }

}
