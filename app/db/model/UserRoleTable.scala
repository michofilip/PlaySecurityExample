package db.model

import slick.jdbc.PostgresProfile.api._
import slick.lifted.TableQuery

class UserRoleTable(t: Tag) extends Table[(Int, Int)](t, "user_role") {

    def userId = column[Int]("user_id")

    def roleId = column[Int]("role_id")

    def * = (userId, roleId)

    def userFk = foreignKey("user_fk", userId, UserTable.all)(_.id)

    def user = userFk.filter(_.id === userId)

    def roleFk = foreignKey("role_fk", roleId, RoleTable.all)(_.id)

    def role = roleFk.filter(_.id === roleId)

}

object UserRoleTable {
    val all = TableQuery[UserRoleTable]
}
