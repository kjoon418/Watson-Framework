package framework.action

data class ActionResponse<out R>(
    val statusCode: Int,
    val body: R?
)
