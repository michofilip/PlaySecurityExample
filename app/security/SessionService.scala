package security

import akka.actor.ActorSystem
import security.SessionService.Session

import java.time.{LocalDateTime, ZoneOffset}
import java.util.UUID
import javax.inject.{Inject, Singleton}
import scala.collection.mutable
import scala.concurrent.ExecutionContext
import scala.concurrent.duration.DurationInt


@Singleton
class SessionService @Inject()(actorSystem: ActorSystem)(implicit executionContext: ExecutionContext) {

    private val sessions = mutable.Map.empty[String, Session]

    actorSystem.scheduler.scheduleAtFixedRate(initialDelay = 1.minute, interval = 1.minute) { () =>
        removeExpiredSessions()
    }

    def generateToken(username: String): String = {
        val token = s"$username-token-${UUID.randomUUID().toString}"
        val session = Session(token, username, LocalDateTime.now(ZoneOffset.UTC).plusHours(6))

        sessions.put(token, session)

        token
    }

    def getActiveSession(token: String): Option[Session] = {
        sessions.get(token)
            .filter(_.isActive)
    }

    def removeSession(token: String): Unit = {
        sessions.remove(token)
    }

    private def removeExpiredSessions(): Unit = {
        sessions.values
            .filter(_.isExpired)
            .map(_.token)
            .foreach(removeSession)
    }
}

object SessionService {

    case class Session(token: String, username: String, expiration: LocalDateTime) {
        def isActive: Boolean = expiration.isAfter(LocalDateTime.now(ZoneOffset.UTC))

        def isExpired: Boolean = !isActive
    }

}
