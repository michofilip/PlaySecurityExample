package security

import security.SessionService.Session

import java.time.{LocalDateTime, ZoneOffset}
import java.util.UUID
import javax.inject.Singleton
import scala.collection.mutable


@Singleton
class SessionService {
    private val sessions = mutable.Map.empty[String, Session]

    def getActiveSession(token: String): Option[Session] = {
        sessions.get(token)
            .filter(_.isActive)
    }

    def removeSession(token: String): Unit = {
        sessions.remove(token)
    }

    def generateToken(username: String): String = {
        val token = s"$username-token-${UUID.randomUUID().toString}"
        val session = Session(token, username, LocalDateTime.now(ZoneOffset.UTC).plusHours(6))

        sessions.put(token, session)

        token
    }
}

object SessionService {

    case class Session(token: String, username: String, expiration: LocalDateTime) {
        def isActive: Boolean = expiration.isAfter(LocalDateTime.now(ZoneOffset.UTC))
    }

}
