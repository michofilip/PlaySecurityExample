package db.model

import db.model.UserTable.UserEntity
import slick.jdbc.PostgresProfile.api._
import slick.lifted.TableQuery

class UserTable(t: Tag) extends Table[UserEntity](t, "user") {

    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def username = column[String]("username")

    def password = column[String]("password")

    def * = (username, password, id).mapTo[UserEntity]

    def roles = UserRoleTable.all.filter(_.userId === id).flatMap(_.role)

}

object UserTable {
    val all = TableQuery[UserTable]

    case class UserEntity(username: String, password: String, id: Int = 0)

}
