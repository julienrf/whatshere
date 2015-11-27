package wh.models

case class Locations(locations: List[Location]) {
  def findByName(name: String): Option[Location] =
    locations.find(name == _.name)
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