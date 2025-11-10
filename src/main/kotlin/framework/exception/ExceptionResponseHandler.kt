package framework.exception

import framework.constants.HttpStatus
import framework.dto.Response
import org.slf4j.LoggerFactory

class ExceptionResponseHandler {
    private val log = LoggerFactory.getLogger(ExceptionResponseHandler::class.java)

    fun createErrorResponse(exception: Exception): Response {
        log.warn(exception.stackTraceToString())

        return Response(
            status = exception.statusCode,
            contentType = "text/plain",
            body = exception.message ?: ""
        )
    }

    private val Exception.statusCode: HttpStatus
        get() {
            return when (this) {
                is ResponsibleException -> status
                else -> HttpStatus.INTERNAL_SERVER_ERROR
            }
        }
}
