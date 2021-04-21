package db.model

import db.model.RoleTable.RoleEntity
import slick.jdbc.PostgresProfile.api._
import slick.lifted.TableQuery

class RoleTable(t: Tag) extends Table[RoleEntity](t, "role") {

    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def role = column[String]("role")

    def * = (role, id).mapTo[RoleEntity]

}

object RoleTable {
    val all = TableQuery[RoleTable]

    case class RoleEntity(role: String, id: Int = 0)

}

