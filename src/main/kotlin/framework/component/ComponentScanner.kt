package framework.component

import org.reflections.Reflections
import java.lang.reflect.Modifier.isAbstract

class ComponentScanner {
    fun scan(basePackages: List<String>): ComponentInstances {
        val componentClasses = findServiceClasses(basePackages)

        val instances = ComponentInstances()
        for (componentClass in componentClasses) {
            if (componentClass.isNotImplementation) {
                continue
            }

            val instance = componentClass.createInstance()

            instances.register(
                classKey = componentClass,
                instanceValue = instance
            )
            instances.registerSuperInterfaces(
                componentClass = componentClass,
                componentInstance = instance
            )
        }

        return instances
    }

    private fun findServiceClasses(basePackages: List<String>): Set<Class<*>> {
        val reflections = Reflections(basePackages)

        return reflections.getTypesAnnotatedWith(Component::class.java)
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
