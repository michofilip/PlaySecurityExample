package db.repository

import db.model.UserTable
import db.model.UserTable.UserEntity
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile
import slick.jdbc.PostgresProfile.api._

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class UserRepository @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {

    def findAll(): Future[Seq[UserEntity]] = db.run {
        UserTable.all.result
    }

    def findById(id: Int): Future[Option[UserEntity]] = db.run {
        UserTable.all
            .filter(user => user.id === id)
            .result.headOption
    }

    def findByUsername(username: String): Future[Option[UserEntity]] = db.run {
        UserTable.all
            .filter(user => user.username === username)
            .result.headOption
    }

}
