package framework.security

import org.mindrot.jbcrypt.BCrypt

object PasswordEncoder {
    private const val LOG_ROUNDS = 10

    fun encode(password: String): String {
        return BCrypt.hashpw(password, BCrypt.gensalt(LOG_ROUNDS))
    }

    fun isMatch(rawPassword: String, encodedPassword: String): Boolean {
        return BCrypt.checkpw(rawPassword, encodedPassword)
    }
}
