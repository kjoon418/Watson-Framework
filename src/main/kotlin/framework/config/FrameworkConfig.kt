package framework.config

import framework.action.HttpActionScanner
import framework.dispatcher.Dispatcher
import framework.request.HttpRequestMapper
import framework.request.JsonRequestBodyMapper
import framework.response.JsonResponseMapper
import framework.exception.ExceptionResponseHandler
import framework.response.HttpResponseWriter
import framework.router.DefaultRouter
import framework.router.PathVariableExtractor
import framework.server.HttpServer
import framework.server.Servlet

class FrameworkConfig {
    // Mapper
    private val requestMapper = HttpRequestMapper()
    private val responseMapper = JsonResponseMapper()
    private val bodyMapper = JsonRequestBodyMapper()

    // Router
    private val actionScanner = HttpActionScanner()
    private val actions = actionScanner.scan(BASE_PACKAGES)
    private val router = DefaultRouter(actions)
    private val pathVariableExtractor = PathVariableExtractor()

    // Exception
    private val exceptionResponseHandler = ExceptionResponseHandler()

    // Dispatcher
    private val dispatcher = Dispatcher(
        responseMapper = responseMapper,
        bodyMapper = bodyMapper,
        router = router,
        pathVariableExtractor = pathVariableExtractor,
        exceptionResponseHandler = exceptionResponseHandler
    )

    // Server
    private val httpResponseWriter = HttpResponseWriter()
    private val servlet = Servlet(
        requestMapper = requestMapper,
        responseWriter = httpResponseWriter,
        dispatcher = dispatcher
    )
    val server = HttpServer(servlet)
}
