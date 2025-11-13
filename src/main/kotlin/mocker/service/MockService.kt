package mocker.service

import framework.constants.HttpMethod
import mocker.dto.request.MockCreateRequestDto
import mocker.dto.request.MockUpdateRequestDto
import mocker.dto.response.MockResponseDto

interface MockService {
    fun create(requestDto: MockCreateRequestDto, userKey: Long)

    fun read(userKey: Long): List<MockResponseDto>

    fun call(userId: String, endPoint: String, method: HttpMethod): String

    fun update(requestDto: MockUpdateRequestDto, userKey: Long, mockKey: Long)

    fun delete(userKey: Long, mockKey: Long)
}
