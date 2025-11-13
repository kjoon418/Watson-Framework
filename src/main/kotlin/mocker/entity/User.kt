package mocker.entity

import framework.repository.Entity

class User(
    key: Long,
    val id: String,
    val password: String,

    val mocks: MutableList<Mock> = mutableListOf()
) : Entity<Long>(key) {
    fun addMock(mock: Mock) {
        mocks.add(mock)
    }

    fun removeMock(mock: Mock) {
        mocks.remove(mock)
    }

    fun findMockBy(mockKey: Long): Mock? {
        return mocks.firstOrNull { it.key == mockKey }
    }
}
