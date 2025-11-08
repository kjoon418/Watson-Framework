package framework.server

import framework.dispatcher.Dispatcher
import framework.response.HttpResponseWriter
import framework.request.RequestMapper
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

class Servlet(
    private val requestMapper: RequestMapper<HttpServletRequest>,
    private val responseWriter: HttpResponseWriter,
    private val dispatcher: Dispatcher
) : HttpServlet() {
    override fun service(request: HttpServletRequest?, response: HttpServletResponse?) {
        val requestData = requestMapper.map(request!!)
        val responseData = dispatcher.dispatch(requestData)

        responseWriter.write(response!!, responseData)
    }
}
