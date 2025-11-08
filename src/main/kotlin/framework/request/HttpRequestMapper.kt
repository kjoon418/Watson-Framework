package framework.request

import framework.constants.HttpMethod
import framework.dto.Request
import framework.dto.RequestBody
import framework.dto.RequestContext
import jakarta.servlet.http.HttpServletRequest

class HttpRequestMapper : RequestMapper<HttpServletRequest> {
    override fun map(request: HttpServletRequest): Request {
        val context = RequestContext(
            method = request.httpMethod,
            path = request.requestURI,
            queryParams = request.queryParameters,
            headers = request.headers
        )
        val requestBody = RequestBody(
            body = request.bodyText,
            contentType = request.contentType ?: "text/plain"
        )

        return Request(
            requestContext = context,
            requestBody = requestBody
        )
    }

    private val HttpServletRequest.httpMethod: HttpMethod
        get() = HttpMethod.from(method)

    private val HttpServletRequest.queryParameters: Map<String, List<String>>
        get() {
            val params = mutableMapOf<String, List<String>>()

            for (name in parameterMap.keys) {
                params[name] = getParameterValues(name)?.toList() ?: emptyList()
            }

            return params
        }

    private val HttpServletRequest.headers: Map<String, List<String>>
        get() {
            val headers = mutableMapOf<String, List<String>>()

            while (headerNames.hasMoreElements()) {
                val name = headerNames.nextElement()
                headers[name] = getHeaders(name).toList()
            }

            return headers
        }

    private val HttpServletRequest.bodyText: String
        get() {
            val builder = StringBuilder()
            reader.useLines { lines ->
                lines.forEach { builder.append(it).append('\n') }
            }

            return builder.toString().trimEnd()
        }
}
