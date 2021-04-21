package controllers

import play.api.libs.json.Json
import play.api.mvc._
import service.UserService

import javax.inject._
import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

@Singleton
class UserController @Inject()(val controllerComponents: ControllerComponents,
                               userService: UserService)
                              (implicit ec: ExecutionContext) extends BaseController {

    def findAll(): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
        userService.findAll().map { users =>
            Ok(Json.toJson(users))
        }
    }

    def findById(userId: Int): Action[AnyContent] = Action.async { implicit request =>
        userService.findById(userId).map {
            case Success(user) => Ok(Json.toJson(user))
            case Failure(e) => NotFound(e.getMessage)
        }
    }

}
