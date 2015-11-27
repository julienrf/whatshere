package wh.models

case class Locations(locations: List[Location]) {
  def findByName(name: String): Option[Location] =
    locations.find(name == _.name)
}

object Locations {

  val current =
    Locations(
      List(
        Location("Canal Saint-Martin", description = "Un endroit bien sympathique pour prendre une bière au bord de l'eau."),
        Location("Le Mal Barré", description = "Burgers originaux, potatoes et cupcakes faits maison proposés dans un cadre chaleureux avec terrasse sur rue."),
        Location("Tour Eiffel", description = "Impossible de visiter Paris sans passer devant.")
      )
    )

}