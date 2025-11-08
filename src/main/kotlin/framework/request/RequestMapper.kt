package framework.request

import framework.dto.Request

interface RequestMapper<T> {
    fun map(request: T): Request
}
