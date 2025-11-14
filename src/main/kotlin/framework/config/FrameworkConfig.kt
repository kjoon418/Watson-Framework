package framework.config

import framework.action.HttpActionScanner
import framework.component.ComponentProvider
import framework.component.ComponentScanner
import framework.config.ConfigProperty.BasePackage
import framework.dispatcher.Dispatcher
import framework.exception.ExceptionResponseHandler
import framework.repository.MemoryRepository
import framework.repository.RepositoryProvider
import framework.repository.RepositoryScanner
import framework.request.HttpRequestMapper
import framework.request.JsonRequestBodyMapper
import framework.response.HttpResponseWriter
import framework.response.JsonResponseMapper
import framework.router.DefaultRouter
import framework.router.PathVariableExtractor
import framework.router.Router
import framework.security.AuthorizationValidator
import framework.server.HttpServer
import framework.server.Server
import framework.server.Servlet

object FrameworkConfig {
    // Mapper
    private val requestMapper = HttpRequestMapper()
    private val responseMapper = JsonResponseMapper()
    private val bodyMapper = JsonRequestBodyMapper()

    // Router
    private val pathVariableExtractor = PathVariableExtractor()
    private val actionScanner = HttpActionScanner
    private val router: Router

    // Exception
    private val exceptionResponseHandler = ExceptionResponseHandler()

    // Repository
    private val repositoryScanner = RepositoryScanner(MemoryRepository::class)
    private val repositoryProvider = RepositoryProvider

    // Component
    private val componentScanner = ComponentScanner()
    private val componentProvider = ComponentProvider

    // Security
    private val authorizationValidator = AuthorizationValidator()

    // Dispatcher
    private val dispatcher: Dispatcher

    // Server
    private val httpResponseWriter = HttpResponseWriter()
    private val servlet: Servlet
    val server: Server

    init {
        val repositoryBuilders = repositoryScanner.scan(BasePackage.value)
        repositoryProvider.init(repositoryBuilders)

        val components = componentScanner.scan(BasePackage.value)
        componentProvider.init(components)

        val actions = actionScanner.scan(BasePackage.value)
        router = DefaultRouter(actions)

        dispatcher = Dispatcher(
            responseMapper = responseMapper,
            bodyMapper = bodyMapper,
            router = router,
            authorizationValidator = authorizationValidator,
            pathVariableExtractor = pathVariableExtractor,
            exceptionResponseHandler = exceptionResponseHandler
        )
        servlet = Servlet(
            requestMapper = requestMapper,
            responseWriter = httpResponseWriter,
            dispatcher = dispatcher
        )
        server = HttpServer(servlet)
    }
}
