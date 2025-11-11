package framework.repository

import org.reflections.Reflections
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import kotlin.reflect.KClass

class RepositoryScanner(
    private val implementationStrategy: KClass<out Repository<*, *>>
) {
    fun scan(basePackages: List<String>): RepositoryBuilders {
        val repositories = findRepositories(basePackages)

        val repositoryBuilders = RepositoryBuilders()
        for (repository in repositories) {
            registerRepositoryBuilder(repositoryBuilders, repository)
        }

        return repositoryBuilders
    }

    private fun findRepositories(basePackages: List<String>): Set<Class<*>> {
        val reflections = Reflections(basePackages)
        val allSubTypes = reflections.getSubTypesOf(Repository::class.java)

        return allSubTypes.filter { it.isInterface }.toSet()
    }

    private fun registerRepositoryBuilder(
        repositoryBuilders: RepositoryBuilders,
        repositoryClass: Class<*>
    ) {
        val (entityClass, keyClass) = findEntityAndKeyTypes(repositoryClass)
            ?: return

        @Suppress("UNCHECKED_CAST")
        repositoryBuilders.register(
            entityClass = entityClass as Class<Nothing>,
            keyClass = keyClass,
            builder = builder
        )
    }

    private fun findEntityAndKeyTypes(repoInterface: Class<*>): Pair<Class<*>, Class<*>>? {
        val superInterfaces = repoInterface.genericInterfaces

        for (type in superInterfaces) {
            if (type.isRepository()) {
                return extractValidTypeArguments(type as ParameterizedType)
            }
        }

        return null
    }

    private fun extractValidTypeArguments(type: ParameterizedType): Pair<Class<*>, Class<*>>? {
        val entityType = type.entityType
        val keyType = type.keyType

        if (entityType is Class<*> && keyType is Class<*>) {
            return Pair(entityType, keyType)
        }

        return null
    }

    private fun Type.isRepository(): Boolean {
        return this is ParameterizedType && rawType == Repository::class.java
    }

    private val ParameterizedType.entityType: Type
        get() = actualTypeArguments[INDEX_OF_ENTITY_TYPE]

    private val ParameterizedType.keyType: Type
        get() = actualTypeArguments[INDEX_OF_KEY_TYPE]

    private val builder: () -> Repository<*, *>
        get() = {
            implementationStrategy.java.getDeclaredConstructor().newInstance() as Repository<*, *>
        }

    companion object {
        const val INDEX_OF_ENTITY_TYPE = 0
        const val INDEX_OF_KEY_TYPE = 1
    }
}
