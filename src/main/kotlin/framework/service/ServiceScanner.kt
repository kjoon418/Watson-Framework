package framework.service

import org.reflections.Reflections
import java.lang.reflect.Modifier.isAbstract

class ServiceScanner {
    fun scan(basePackages: List<String>): ServiceInstances {
        val serviceClasses = findServiceClasses(basePackages)

        val instances = ServiceInstances()
        for (serviceClass in serviceClasses) {
            if (serviceClass.isNotImplementation) {
                continue
            }

            val instance = serviceClass.createInstance()

            instances.register(
                classKey = serviceClass,
                instanceValue = instance
            )
            instances.registerSuperInterfaces(
                serviceClass = serviceClass,
                serviceInstance = instance
            )
        }

        return instances
    }

    private fun findServiceClasses(basePackages: List<String>): Set<Class<*>> {
        val reflections = Reflections(basePackages)

        return reflections.getTypesAnnotatedWith(Service::class.java)
    }

    private val Class<*>.isNotImplementation: Boolean
        get() = this.isInterface || isAbstract(this.modifiers)

    private fun Class<*>.createInstance(): Any {
        return try {
            getDeclaredConstructor().newInstance()
        } catch (e: NoSuchMethodException) {
            throw IllegalStateException("${REQUIRE_NO_ARGS_CONSTRUCTOR}: ${this.name}")
        }
    }

    companion object {
        private const val REQUIRE_NO_ARGS_CONSTRUCTOR = "기본 생성자가 필요합니다"
    }
}
