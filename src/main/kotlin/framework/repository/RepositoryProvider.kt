package framework.repository

import java.util.concurrent.ConcurrentHashMap

object RepositoryProvider {
    private lateinit var repositoryBuilders: RepositoryBuilders
    private val repositories = ConcurrentHashMap<Pair<Class<*>, Class<*>>, Repository<*, *>>()

    fun init(repositoryBuilders: RepositoryBuilders) {
        this.repositoryBuilders = repositoryBuilders
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Entity<K>, K> get(
        entityClass: Class<T>,
        keyClass: Class<K>
    ): Repository<T, K> {
        val key = Pair(entityClass, keyClass)

        return repositories.computeIfAbsent(key) {
            repositoryBuilders.build(entityClass, keyClass)
        } as Repository<T, K>
    }
}
