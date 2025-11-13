package framework.action

import framework.constants.HttpMethod

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class HttpAction(
    val method: Array<HttpMethod> = [],
    val path: String
)
