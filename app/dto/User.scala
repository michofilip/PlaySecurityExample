package dto

import play.api.libs.json.{Json, OWrites}

case class User(id: Int, username: String, roles: Seq[String])

object User {
    implicit val writes: OWrites[User] = Json.writes[User]
}
