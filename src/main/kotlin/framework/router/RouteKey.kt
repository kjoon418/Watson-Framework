package framework.router

import framework.constants.HttpMethod

data class RouteKey(
    val method: HttpMethod,
    val pathTemplate: PathTemplate
)
