package controllers

import db.repository.UserRepository
import form.LoginData
import play.api.mvc._
import security.{Authenticated, PasswordUtils, SessionService}

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}


@Singleton
class LoginController @Inject()(val cc: MessagesControllerComponents,
                                authenticated: Authenticated,
                                sessionService: SessionService,
                                userRepository: UserRepository)
                               (implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

    def loginPage: Action[AnyContent] = Action { implicit request: MessagesRequest[AnyContent] =>
        Ok(views.html.login(LoginData.loginForm))
    }

    def login: Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
        LoginData.loginForm.bindFromRequest().fold(formWithErrors => {
            Future.successful(BadRequest)
        }, { case LoginData(username, password) =>
            isValidLogin(username, password).map {
                case true =>
                    val token = sessionService.generateToken(username)
                    Redirect(routes.MyController.priv()).withSession(request.session + ("sessionToken" -> token))

                case false =>
                    Redirect(routes.LoginController.loginPage()).withNewSession
            }
        })
    }

    def logout: Action[AnyContent] = authenticated { implicit request: Request[AnyContent] =>
        request.session.get("sessionToken").foreach(sessionService.removeSession)

        Redirect(routes.LoginController.loginPage()).withNewSession
    }

    private def isValidLogin(username: String, password: String): Future[Boolean] = {
        userRepository.findByUsername(username).map {
            case Some(user) => PasswordUtils.checkPassword(password, user.password)
            case None => false
        }
    }

}
