package form

import play.api.data.Forms._
import play.api.data._

case class LoginData(username: String, password: String)

object LoginData {
    val loginForm: Form[LoginData] = Form(
        mapping(
            "username" -> text,
            "password" -> text
        )(LoginData.apply)(LoginData.unapply)
    )
}
