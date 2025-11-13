package framework.dispatcher

import framework.action.Action
import framework.action.ActionResponse
import framework.dto.Request
import framework.dto.Response
import framework.exception.ExceptionResponseHandler
import framework.request.HttpBodyMapper
import framework.response.ResponseMapper
import framework.router.PathVariableExtractor
import framework.router.Router
import framework.security.AuthorizationValidator
import java.lang.reflect.ParameterizedType

class Dispatcher(
    private val responseMapper: ResponseMapper,
    private val bodyMapper: HttpBodyMapper,
    private val router: Router,
    private val authorizationValidator: AuthorizationValidator,
    private val pathVariableExtractor: PathVariableExtractor,
    private val exceptionResponseHandler: ExceptionResponseHandler
) {
    fun dispatch(request: Request): Response {
        try {
            val (routeKey, action) = router.route(request)
            authorizationValidator.validate(action, request.bearerToken)

            val type = findTypeOf(action)
            val body = bodyMapper.map(request.requestBody.body, type)
            val pathVariables = pathVariableExtractor.extract(routeKey, request.requestContext.path)

            val actionResponse = runAction(
                action = action,
                request = request,
                pathVariables = pathVariables,
                body = body
            )

            return responseMapper.map(actionResponse)
        } catch (exception: Exception) {
            return exceptionResponseHandler.createErrorResponse(exception)
        }
    }

    private fun findTypeOf(action: Action<*, *>): Class<*> {
        val superclass = action.javaClass.genericInterfaces
            .firstOrNull { it is ParameterizedType } as? ParameterizedType

        return superclass?.actualTypeArguments?.first() as Class<*>
    }

    @Suppress("UNCHECKED_CAST")
    private fun runAction(
        action: Action<*, *>,
        request: Request,
        pathVariables: Map<String, String>,
        body: Any
    ): ActionResponse<*> {
        return (action as Action<Any?, *>).act(request, pathVariables, body)
    }
}
