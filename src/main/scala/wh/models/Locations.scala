package wh.models

case class Locations(locations: List[Location]) {

}

object Locations {

  val current =
    Locations(
      List(
        Location("Canal Saint-Martin"),
        Location("Le Mal Barré"),
        Location("Tour Eiffel")
      )
    )

}