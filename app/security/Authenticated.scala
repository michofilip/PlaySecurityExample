package security

import dto.User
import play.api.mvc.Results.NotFound
import play.api.mvc.{ActionBuilder, ActionFilter, ActionRefiner, AnyContent, BodyParsers, Request, Result, WrappedRequest}
import security.Authenticated.{UserRequest, authorised}
import service.UserService

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class Authenticated @Inject()(val parser: BodyParsers.Default,
                              sessionService: SessionService,
                              userService: UserService)
                             (implicit val executionContext: ExecutionContext)
    extends ActionBuilder[UserRequest, AnyContent]
        with ActionRefiner[Request, UserRequest] {

    override protected def refine[A](request: Request[A]): Future[Either[Result, UserRequest[A]]] = {
        request.session.get("sessionToken")
            .flatMap(sessionService.getActiveSession)
            .map(_.username)
            .map(userService.findByUsername)
            .getOrElse(Future.successful(None))
            .map {
                case Some(user) => Right(new UserRequest(user, request))
                case None => Left(NotFound)
            }
    }

    def withRole(role: String): ActionBuilder[UserRequest, AnyContent] =
        this.andThen(authorised(Set(role)))

    def withAnyRoles(role: String, roles: String*): ActionBuilder[UserRequest, AnyContent] =
        this.andThen(authorised((role +: roles).toSet))

    def withAllRoles(role: String, roles: String*): ActionBuilder[UserRequest, AnyContent] =
        this.andThen(authorised((role +: roles).toSet, allRequired = true))
}

object Authenticated {

    class UserRequest[A](val user: User, request: Request[A]) extends WrappedRequest[A](request)

    private def authorised(requiredRoles: Set[String], allRequired: Boolean = false)(implicit ec: ExecutionContext): ActionFilter[UserRequest] = new ActionFilter[UserRequest] {
        override protected def filter[A](request: UserRequest[A]): Future[Option[Result]] = Future.successful {
            val userRoles = request.user.roles.toSet

            val isAuthorised = if (allRequired) {
                requiredRoles.forall(userRoles.contains)
            } else {
                requiredRoles.exists(userRoles.contains)
            }

            if (!isAuthorised) {
                Some(NotFound)
            } else {
                None
            }
        }

        override protected def executionContext: ExecutionContext = ec
    }
}
