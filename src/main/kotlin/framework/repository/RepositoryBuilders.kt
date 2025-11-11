package framework.repository

import java.util.concurrent.ConcurrentHashMap

class RepositoryBuilders {
    private val repositoryBuilders = ConcurrentHashMap<Pair<Class<*>, Class<*>>, () -> Repository<*, *>>()

    fun register(
        entityClass: Class<*>,
        keyClass: Class<*>,
        builder: () -> Repository<*, *>
    ) {
        val key = Pair(entityClass, keyClass)
        repositoryBuilders[key] = builder
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Entity<K>, K> build(
        entityClass: Class<T>,
        keyClass: Class<K>
    ): Repository<T, K> {
        val key = Pair(entityClass, keyClass)

        return repositoryBuilders[key]?.invoke() as Repository<T, K>?
            ?: throw IllegalStateException("${IMPLEMENTATION_STRATEGY_NOT_FOUND}: ${key.first}, ${key.second}")
    }

    companion object {
        private const val IMPLEMENTATION_STRATEGY_NOT_FOUND = "Repository 구현체 정책이 등록되지 않았습니다"
    }
}
