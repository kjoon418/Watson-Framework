package mocker.dto.request

import framework.constants.HttpMethod

data class MockCreateRequestDto(
    val mockKey: Long,
    val method: HttpMethod,
    val endPoint: String,
    val mockData: String
)
