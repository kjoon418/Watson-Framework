package mocker.repository

import framework.repository.Repository
import framework.repository.RepositoryProvider
import mocker.entity.User

interface UserRepository : Repository<User, Long>
val userRepository = RepositoryProvider.get(User::class.java, Long::class.javaObjectType)
