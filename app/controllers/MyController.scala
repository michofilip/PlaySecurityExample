package controllers

import play.api.mvc._
import security.Authenticated
import security.Authenticated.UserRequest

import javax.inject._
import scala.concurrent.ExecutionContext


@Singleton
class MyController @Inject()(val controllerComponents: ControllerComponents,
                             authenticated: Authenticated)
                            (implicit ec: ExecutionContext) extends BaseController {

    def index: Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
        Redirect(routes.MyController.public()).withNewSession
    }

    def public(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
        Ok(views.html.public())
    }

    def priv(): Action[AnyContent] = authenticated { implicit userReq: UserRequest[AnyContent] =>
        Ok(views.html.priv2(userReq.user))
    }

    def admin(): Action[AnyContent] = authenticated.withRole("ADMIN") { implicit userReq: UserRequest[AnyContent] =>
        Ok(views.html.admin(userReq.user))
    }
}
