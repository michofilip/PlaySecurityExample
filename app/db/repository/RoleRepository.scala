package db.repository

import db.model.RoleTable.RoleEntity
import db.model.UserTable
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile
import slick.jdbc.PostgresProfile.api._

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class RoleRepository @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {

    def findAllByUserId(userId: Int): Future[Seq[RoleEntity]] = db.run {
        UserTable.all
            .filter(user => user.id === userId)
            .flatMap { user =>
                user.roles
            }
            .result
    }

    def findAllByUserIdInGroupByUserId(userIds: Seq[Int]): Future[Seq[(Int, RoleEntity)]] = db.run {
        UserTable.all
            .filter(user => user.id inSet userIds)
            .flatMap { user =>
                user.roles.map(role => user.id -> role)
            }
            .result
    }

}
