package framework.action

import framework.constants.HttpStatus

data class ActionResponse<out R>(
    val status: HttpStatus = HttpStatus.OK,
    val body: R? = null
) {
    companion object {
        fun noContent(): ActionResponse<Unit> {
            return ActionResponse(
                status = HttpStatus.NO_CONTENT,
                body = Unit
            )
        }
    }
}
