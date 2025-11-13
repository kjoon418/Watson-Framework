package mocker.service

import framework.component.Component
import framework.component.ComponentProvider
import framework.constants.HttpMethod
import mocker.domain.MockMapper
import mocker.dto.request.MockCreateRequestDto
import mocker.dto.request.MockUpdateRequestDto
import mocker.dto.response.MockResponseDto
import mocker.entity.Mock
import mocker.entity.User
import mocker.repository.mockRepository
import mocker.repository.userRepository

private val mockMapper = ComponentProvider.get(MockMapper::class.java)

@Component
class MockServiceImpl : MockService {
    override fun create(requestDto: MockCreateRequestDto, userKey: Long) {
        val user = findUserBy(userKey)
        validateDuplicate(user, requestDto.endPoint, requestDto.method)

        val mock = Mock(
            key = requestDto.mockKey,
            method = requestDto.method,
            endPoint = requestDto.endPoint.removePrefix(LEADING_SLASH),
            data = requestDto.mockData
        )
        user.addMock(mock)

        mockRepository.save(mock)
    }

    override fun read(userKey: Long): List<MockResponseDto> {
        val user = findUserBy(userKey)

        return user.mocks
            .map { mockMapper.map(it) }
    }

    override fun call(userId: String, endPoint: String, method: HttpMethod): String {
        val user = findUserBy(userId)

        val mock = user.mocks
            .find { it.endPoint == endPoint && it.method == method }
            ?: throw IllegalArgumentException(MOCK_NOT_FOUND)

        return mock.data
    }

    override fun update(requestDto: MockUpdateRequestDto, userKey: Long, mockKey: Long) {
        val user = findUserBy(userKey)
        val mock = user.findMockBy(mockKey)
            ?: throw IllegalArgumentException(MOCK_NOT_FOUND)

        mock.data = requestDto.mockData
    }

    override fun delete(userKey: Long, mockKey: Long) {
        val user = findUserBy(userKey)
        val mock = user.findMockBy(mockKey)
            ?: throw IllegalArgumentException(MOCK_NOT_FOUND)

        user.removeMock(mock)
        mockRepository.delete(mock)
    }

    private fun findUserBy(userKey: Long): User {
        return userRepository.findByKey(userKey)
            ?: throw RuntimeException(USER_NOT_FOUND)
    }

    private fun findUserBy(userId: String): User {
        return userRepository.findAll()
            .find { it.id == userId }
            ?: throw RuntimeException(USER_NOT_FOUND)
    }

    private fun validateDuplicate(user: User, endPoint: String, method: HttpMethod) {
        val notDuplicated = user.mocks.none { it.endPoint == endPoint && it.method == method }

        require(notDuplicated) { MOCK_DUPLICATE }
    }

    companion object {
        private const val USER_NOT_FOUND = "회원을 조회할 수 없습니다."
        private const val MOCK_NOT_FOUND = "Mock을 조회할 수 없습니다."
        private const val MOCK_DUPLICATE = "Mock의 경로와 메서드가 중복됩니다."

        private const val LEADING_SLASH = "/"
    }
}
