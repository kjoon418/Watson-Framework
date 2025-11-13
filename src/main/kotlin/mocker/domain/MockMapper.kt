package mocker.domain

import framework.component.Component
import mocker.dto.response.MockResponseDto
import mocker.entity.Mock

@Component
class MockMapper {
    fun map(mock: Mock): MockResponseDto {
        return MockResponseDto(
            key = mock.key,
            method = mock.method,
            endPoint = mock.endPoint,
            data = mock.data
        )
    }
}
