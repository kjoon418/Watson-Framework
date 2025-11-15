package framework.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.InputStream

object PropertyLoader {
    private const val YML_NAME = "watson.yml"

    private val properties: Map<String, Any>

    init {
        val ymlMapper = buildYmlMapper()

        val inputStream = connectYmlInputStream()

        inputStream.use {
            properties = ymlMapper.readValue(it)
        }
    }

    fun get(key: String): Any? {
        val splitKey = key.split('.')

        val searchResult = searchProperties(splitKey)

        return searchResult
    }

    private fun buildYmlMapper(): ObjectMapper {
        return ObjectMapper(YAMLFactory()).apply {
            registerModule(KotlinModule.Builder().build())
        }
    }

    private fun connectYmlInputStream(): InputStream {
        return PropertyLoader::class.java.classLoader.getResourceAsStream(YML_NAME)
            ?: throw IllegalStateException(YML_NOT_EXIST)
    }

    @Suppress("UNCHECKED_CAST")
    private fun searchProperties(key: List<String>): Any? {
        var currentPoint: Any? = properties

        key.forEach { currentKey ->
            if (!currentPoint.isMap) return null
            currentPoint = (currentPoint as Map<String, Any>).nextPoint(currentKey)
        }

        return currentPoint
    }

    private val Any?.isMap: Boolean
        get() {
            return this is Map<*, *>
        }

    private fun Map<String, Any>.nextPoint(key: String): Any? {
        return this[key]
    }

    private const val YML_NOT_EXIST = "'$YML_NAME'이 'resources'에 존재하지 않습니다."
}
