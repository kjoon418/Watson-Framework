package framework.component

import java.util.concurrent.ConcurrentHashMap

object ComponentProvider {
    private val components = ConcurrentHashMap<Class<*>, Any>()

    fun init(componentInstances: ComponentInstances) {
        this.components.putAll(componentInstances.get())
    }

    fun <T : Any> get(componentClass: Class<T>): T {
        val instance = components[componentClass]
            ?: throw IllegalStateException("${SERVICE_NOT_FOUND}: ${componentClass.name}")
        
        @Suppress("UNCHECKED_CAST")
        return instance as T
    }

    private const val SERVICE_NOT_FOUND = "해당 타입에 대한 컴포넌트를 찾을 수 없습니다"
}
