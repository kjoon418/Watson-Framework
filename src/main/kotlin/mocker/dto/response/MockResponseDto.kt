package mocker.dto.response

import framework.constants.HttpMethod

data class MockResponseDto(
    val key: Long,
    val method: HttpMethod,
    val endPoint: String,
    val data: String
)
