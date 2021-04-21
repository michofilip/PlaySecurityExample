package security

import java.security.SecureRandom
import java.util.Base64
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

object PasswordUtils {
    private val iterations = 10000
    private val random = new SecureRandom()
    private val delimiter = ":"

    def hashPassword(password: String): String = {
        val salt = new Array[Byte](16)
        random.nextBytes(salt)
        val hash = pbkdf2(password, salt, iterations)
        val salt64 = Base64.getEncoder.encodeToString(salt)
        val hash64 = Base64.getEncoder.encodeToString(hash)

        s"$iterations:$hash64:$salt64"
    }

    def checkPassword(password: String, hashedPassword: String): Boolean = {
        hashedPassword.split(delimiter) match {
            case Array(iterations, hash64, salt64) if iterations.forall(_.isDigit) =>
                val hash = Base64.getDecoder.decode(hash64)
                val salt = Base64.getDecoder.decode(salt64)
                val calculatedHash = pbkdf2(password, salt, iterations.toInt)

                calculatedHash.sameElements(hash)

            case _ => false
        }
    }

    private def pbkdf2(password: String, salt: Array[Byte], iterations: Int): Array[Byte] = {
        val keySpec = new PBEKeySpec(password.toCharArray, salt, iterations, 256)
        val keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        keyFactory.generateSecret(keySpec).getEncoded
    }
}
