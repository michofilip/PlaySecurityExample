//package controllers.helpers
//
//import controllers.helpers.UserAction.UserRequest
//import play.api.mvc.{ActionBuilder, ActionTransformer, WrappedRequest, _}
//import security.UserDAO.User
//import security.{SessionDao, UserDAO}
//
//import java.time.{LocalDateTime, ZoneOffset}
//import javax.inject.Inject
//import scala.concurrent.{ExecutionContext, Future}
//
//class UserAction @Inject()(val parser: BodyParsers.Default)(implicit val executionContext: ExecutionContext)
//    extends ActionBuilder[UserRequest, AnyContent]
//        with ActionTransformer[Request, UserRequest] {
//
//    def transform[A](request: Request[A]): Future[UserRequest[A]] = Future.successful {
//
//        val sessionTokenOpt = request.session.get("sessionToken")
//
//        val user = sessionTokenOpt
//            .flatMap(token => SessionDao.getSession(token))
//            .filter(_.expiration.isAfter(LocalDateTime.now(ZoneOffset.UTC)))
//            .map(_.username)
//            .flatMap(UserDAO.getUser)
//
//        new UserRequest(user, request)
//    }
//}
//
//object UserAction {
//    class UserRequest[A](val user: Option[User], request: Request[A]) extends WrappedRequest[A](request)
//}
