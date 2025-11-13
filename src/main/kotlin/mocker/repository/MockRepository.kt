package mocker.repository

import framework.repository.Repository
import framework.repository.RepositoryProvider
import mocker.entity.Mock

interface MockRepository : Repository<Mock, Long>
val mockRepository = RepositoryProvider.get(Mock::class.java, Long::class.javaObjectType)
