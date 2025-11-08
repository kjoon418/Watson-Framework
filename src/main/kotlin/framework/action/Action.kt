package framework.action

import framework.dto.Request

interface Action<in P, out R> {
    fun act(
        request: Request,
        pathVariables: Map<String, String> = mapOf(),
        body: P
    ): ActionResponse<R>
}
