package framework.action

import framework.router.PathTemplate
import framework.router.RouteKey
import org.reflections.Reflections
import org.reflections.scanners.Scanners
import org.reflections.util.ClasspathHelper
import org.reflections.util.ConfigurationBuilder

class HttpActionScanner : ActionScanner {
    override fun scan(basePackages: List<String>): Map<RouteKey, Action<*, *>> {
        val reflections = buildReflections(basePackages)
        val types = reflections.findTypesOfHttpAction()

        return buildActionRegistry(types)
    }

    private fun buildReflections(basePackages: List<String>): Reflections {
        val contextClassLoader = Thread.currentThread().contextClassLoader

        val reflectionsConfig = ConfigurationBuilder()
            .setClassLoaders(arrayOf(contextClassLoader))
            .setScanners(Scanners.TypesAnnotated, Scanners.SubTypes)
            .apply {
                basePackages.forEach { addUrls(ClasspathHelper.forPackage(it)) }
            }

        return Reflections(reflectionsConfig)
    }

    private fun Reflections.findTypesOfHttpAction(): List<Class<*>> {
        return get(Scanners.TypesAnnotated.with(HttpAction::class.java).asClass<Any>())
            .filter { Action::class.java.isAssignableFrom(it) }
    }

    private fun buildActionRegistry(actionTypes: List<Class<*>>): Map<RouteKey, Action<*, *>> {
        return actionTypes.associate { clazz ->
            val annotation = clazz.httpActionAnnotation
            val actionInstance = instantiateAction(clazz)
            val key = annotation.toRouteKey()

            key to actionInstance
        }
    }

    private val Class<*>.httpActionAnnotation: HttpAction
        get() {
            return getAnnotation(HttpAction::class.java)
                ?: error("HttpAction 애노태이션이 누락되었습니다: $this")
        }

    private fun instantiateAction(clazz: Class<*>): Action<*, *> {
        return clazz.getDeclaredConstructor()
            .apply { isAccessible = true }
            .newInstance() as Action<*, *>
    }

    private fun HttpAction.toRouteKey(): RouteKey {
        return RouteKey(
            method = method,
            pathTemplate = PathTemplate(path)
        )
    }
}
