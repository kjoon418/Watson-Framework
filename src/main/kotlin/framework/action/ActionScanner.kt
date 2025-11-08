package framework.action

import framework.router.RouteKey

interface ActionScanner {
    fun scan(basePackages: List<String>): Map<RouteKey, Action<*, *>>
}
