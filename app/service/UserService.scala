package service

import db.model.UserTable.UserEntity
import db.repository.UserRepository
import dto.User

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

@Singleton
class UserService @Inject()(userRepository: UserRepository,
                            roleService: RoleService)
                           (implicit ec: ExecutionContext) {

    def findAll(): Future[Seq[User]] = {
        userRepository.findAll()
            .flatMap(convertToUsers)
    }

    def findById(userId: Int): Future[Try[User]] = {
        userRepository.findById(userId).flatMap {
            case None =>
                Future.successful {
                    Failure {
                        new NoSuchElementException(s"User id $userId not found!")
                    }
                }

            case Some(userEntity) =>
                convertToUser(userEntity).map { user =>
                    Success {
                        user
                    }
                }
        }
    }

    def findByUsername(username: String): Future[Option[User]] = {
        userRepository.findByUsername(username).flatMap {
            case None =>
                Future.successful {
                    None
                }

            case Some(userEntity) =>
                convertToUser(userEntity).map { user =>
                    Option(user)
                }
        }
    }

    private def convertToUsers(userEntities: Seq[UserEntity]): Future[Seq[User]] = {
        val userIds = userEntities.map(userEntity => userEntity.id)

        for {
            roles <- roleService.getUserIdToRoles(userIds)
        } yield {
            userEntities.map { userEntity =>
                User(
                    id = userEntity.id,
                    username = userEntity.username,
                    roles = roles.getOrElse(userEntity.id, Seq.empty)
                )
            }
        }
    }

    private def convertToUser(userEntity: UserEntity): Future[User] = {
        convertToUsers(Seq(userEntity)).map(users => users.head)
    }
}
