//package controllers
//
//import controllers.helpers.UserAction
//import play.api.mvc._
//
//import javax.inject._
//
///**
// * This controller creates an `Action` to handle HTTP requests to the
// * application's home page.
// */
//@Singleton
//class HomeController @Inject()(val controllerComponents: ControllerComponents,
//                               val userAction: UserAction) extends BaseController {
//    def index(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
//        Ok(views.html.index())
//    }
//
//    // Solution 1
//    //    def priv(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
//    //        withUser(user => Ok(views.html.priv(user)))
//    //    }
//    //
//    //    private def withUser[T](block: User => Result)(implicit request: Request[AnyContent]): Result = {
//    //        val user = extractUser(request)
//    //
//    //        user
//    //            .map(block)
//    //            .getOrElse(Unauthorized(views.html.defaultpages.unauthorized())) // 401, but 404 could be better from a security point of view
//    //    }
//
//    // Solution 2
//    //    def priv(): EssentialAction = withPlayUser { user =>
//    //        Ok(views.html.priv(user))
//    //    }
//    //
//    //    private def withPlayUser(block: User => Result): EssentialAction = {
//    //        Security.WithAuthentication(extractUser)(user => Action(block(user)))
//    //    }
//
//    //    private def extractUser(req: RequestHeader): Option[User] = {
//    //
//    //        val sessionTokenOpt = req.session.get("sessionToken")
//    //
//    //        sessionTokenOpt
//    //            .flatMap(SessionDao.getSession)
//    //            .filter(session => session.expiration.isAfter(LocalDateTime.now()))
//    //            .map(session => session.username)
//    //            .flatMap(UserDAO.getUser)
//    //    }
//    //
//    //    // Solution 3
//    //    def priv(): Action[AnyContent] = userAction { implicit userReq: UserRequest[AnyContent] =>
//    //        userReq.user.map(user => Ok(views.html.priv(user)))
//    //            .getOrElse(Unauthorized(views.html.defaultpages.unauthorized()))
//    //    }
//    //
//    //    // login
//    //    def login(username: String, pass: String): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
//    //        if (isValidLogin(username, pass)) {
//    //            val token = SessionDao.generateToken(username)
//    //
//    //            Redirect(routes.HomeController.index()).withSession(request.session + ("sessionToken" -> token))
//    //        } else {
//    //            // we should redirect to login page
//    //            Unauthorized(views.html.defaultpages.unauthorized()).withNewSession
//    //        }
//    //    }
//    //
//    //    private def isValidLogin(username: String, password: String): Boolean = {
//    //        UserDAO.getUser(username).exists(_.password == password)
//    //    }
//}
