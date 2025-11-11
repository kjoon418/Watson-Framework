package framework.config

import framework.action.HttpActionScanner
import framework.dispatcher.Dispatcher
import framework.request.HttpRequestMapper
import framework.request.JsonRequestBodyMapper
import framework.response.JsonResponseMapper
import framework.exception.ExceptionResponseHandler
import framework.repository.MemoryRepository
import framework.repository.RepositoryBuilders
import framework.repository.RepositoryProvider
import framework.repository.RepositoryScanner
import framework.response.HttpResponseWriter
import framework.router.DefaultRouter
import framework.router.PathVariableExtractor
import framework.router.Router
import framework.server.HttpServer
import framework.server.Server
import framework.server.Servlet
import framework.service.ServiceProvider
import framework.service.ServiceScanner

class FrameworkConfig {
    // Mapper
    private val requestMapper = HttpRequestMapper()
    private val responseMapper = JsonResponseMapper()
    private val bodyMapper = JsonRequestBodyMapper()

    // Router
    private val pathVariableExtractor = PathVariableExtractor()
    private val actionScanner = HttpActionScanner()
    private val router: Router

    // Exception
    private val exceptionResponseHandler = ExceptionResponseHandler()

    // Repository
    private val repositoryScanner = RepositoryScanner(MemoryRepository::class)
    private val repositoryProvider = RepositoryProvider

    // Service
    private val serviceScanner = ServiceScanner()
    private val serviceProvider = ServiceProvider

    // Dispatcher
    private val dispatcher: Dispatcher

    // Server
    private val httpResponseWriter = HttpResponseWriter()
    private val servlet: Servlet
    val server: Server

    init {
        val repositoryBuilders = repositoryScanner.scan(BASE_PACKAGES)
        repositoryProvider.init(repositoryBuilders)

        val services = serviceScanner.scan(BASE_PACKAGES)
        serviceProvider.init(services)

        val actions = actionScanner.scan(BASE_PACKAGES)
        router = DefaultRouter(actions)

        dispatcher = Dispatcher(
            responseMapper = responseMapper,
            bodyMapper = bodyMapper,
            router = router,
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
