package framework.service

import java.util.concurrent.ConcurrentHashMap

object ServiceProvider {
    private const val SERVICE_NOT_FOUND = "해당 타입에 대한 서비스를 찾을 수 없습니다"

    private val services = ConcurrentHashMap<Class<*>, Any>()

    fun init(serviceInstances: ServiceInstances) {
        this.services.putAll(serviceInstances.get())
    }

    fun <T : Any> get(serviceClass: Class<T>): T {
        val instance = services[serviceClass]
            ?: throw IllegalStateException("${SERVICE_NOT_FOUND}: ${serviceClass.name}")
        
        @Suppress("UNCHECKED_CAST")
        return instance as T
    }
}
