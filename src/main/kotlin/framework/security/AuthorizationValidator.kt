package framework.security

import framework.action.Action

class AuthorizationValidator {
    fun validate(action: Action<*, *>, token: String?) {
        val annotation = action::class.java.getAnnotation(RequireAuthorize::class.java)

        if (annotation.isRequireAuthorize) {
            validateToken(token, annotation.role)
        }
    }

    private val RequireAuthorize?.isRequireAuthorize: Boolean
        get() {
            return this != null
        }
}
