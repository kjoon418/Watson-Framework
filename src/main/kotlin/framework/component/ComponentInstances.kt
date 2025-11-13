package framework.component

class ComponentInstances {
    private val instances = mutableMapOf<Class<*>, Any>()

    fun get(): Map<Class<*>, Any> {
        return instances.toMap()
    }

    fun register(
        classKey: Class<*>,
        instanceValue: Any
    ) {
        validateDuplicate(classKey)
        instances[classKey] = instanceValue
    }

    fun registerSuperInterfaces(
        componentClass: Class<*>,
        componentInstance: Any
    ) {
        for (superInterface in componentClass.allInterfaces) {
            register(
                classKey = superInterface,
                instanceValue = componentInstance
            )
        }
    }

    private fun validateDuplicate(key: Class<*>) {
        if (instances.containsKey(key)) {
            throw IllegalStateException("$IMPLEMENTATION_DUPLICATE: ${key.name}")
        }
    }

    @Suppress("RecursivePropertyAccessor")
    private val Class<*>?.allInterfaces: Set<Class<*>>
        get() {
            val interfaces = mutableSetOf<Class<*>>()
            var currentClass = this

            while (!currentClass.isNullOrAny) {
                for (superInterface in currentClass!!.interfaces) {
                    if (!interfaces.contains(superInterface)) {
                        interfaces.add(superInterface)
                        interfaces.addAll(superInterface.allInterfaces)
                    }
                }

                currentClass = currentClass.superclass
            }

            return interfaces
        }

    private val Class<*>?.isNullOrAny: Boolean
        get() {
            return this == null || this == Any::class.java
        }

    companion object {
        private const val IMPLEMENTATION_DUPLICATE = "한 인터페이스에 대한 구현체가 둘 이상입니다"
    }
}
