package mocker.action

import framework.action.Action
import framework.action.ActionResponse
import framework.action.HttpAction
import framework.component.ComponentProvider
import framework.dto.Request
import mocker.service.MockService

private val mockService = ComponentProvider.get(MockService::class.java)

@HttpAction(
    path = "/api/{userId}/{endPoint}"
)
class CallApi : Action<Unit, String> {
    override fun act(request: Request, pathVariables: Map<String, String>, body: Unit): ActionResponse<String> {
        val userId = pathVariables["userId"]!!
        val endPoint = pathVariables["endPoint"]!!
        val method = request.requestContext.method

        val response = mockService.call(userId, endPoint, method)

        return ActionResponse(
            body = response
        )
    }
}
