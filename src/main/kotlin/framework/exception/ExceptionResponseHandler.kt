package framework.exception

import framework.dto.Response
import org.slf4j.LoggerFactory
import kotlin.Exception

class ExceptionResponseHandler {
    private val log = LoggerFactory.getLogger(ExceptionResponseHandler::class.java)

    fun createErrorResponse(exception: Exception): Response {
        log.warn(exception.stackTraceToString())

        return Response(
            code = exception.statusCode,
            contentType = "text/plain",
            body = exception.stackTraceToString()
        )
    }

    private val Exception.statusCode: Int
        get() {
            return when (this) {
                is ResponsibleException -> code
                else -> 500
            }
        }
}
