package wh.models

case class Users(users: List[User]) {

  def findByName(name: String): Option[User] = users.find(_.name == name)

}

object Users {

  val current = Users(List(User("Julien"), User("Thomas")))

}