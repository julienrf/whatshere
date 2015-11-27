package wh.models

case class Locations(locations: List[Location]) {
  def findByName(name: String): Option[Location] =
    locations.find(name == _.name)
}

object Locations {

  val current =
    Locations(
      List(
        Location("Canal Saint-Martin",
          description = "Un endroit bien sympathique pour prendre une bière au bord de l'eau.",
          "http://www.de-nicher.com/wp-content/uploads/sites/5/old/6a01156fa90739970c01a511a54d5b970c-pi.jpg"
        ),
        Location("Le Mal Barré",
          description = "Burgers originaux, potatoes et cupcakes faits maison proposés dans un cadre chaleureux avec terrasse sur rue.",
          "http://www.frankizbird.com/wp-content/uploads/2014/03/DSCF20671.jpg"
        ),
        Location("Tour Eiffel", description = "Impossible de visiter Paris sans passer devant.", "http://www.kairn.com/files/news/42475312222826-tour-eiffel-paris-jpg"),
        Location("Les Champs Élysées", description = "La plus belle avenue de Paris. Si si.", "http://www.holiday-velvet.com/guide/wp-content/uploads/2013/01/champs-elysees.jpg")
      )
    )

}