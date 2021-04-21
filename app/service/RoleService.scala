package service

import db.repository.RoleRepository

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class RoleService @Inject()(roleRepository: RoleRepository)
                           (implicit ec: ExecutionContext) {

    def getUserIdToRoles(userIds: Seq[Int]): Future[Map[Int, Seq[String]]] = {
        roleRepository.findAllByUserIdInGroupByUserId(userIds).map { userIdRoleEntities =>
            userIdRoleEntities.groupMap { case (userId, _) => userId } { case (_, roleEntity) => roleEntity.role }
        }
    }

}
