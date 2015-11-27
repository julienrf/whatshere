package wh.models

case class Ratings(ratings: List[Rating]) {

}

object Ratings {

  val current = Ratings(Nil)

}