package framework.router

data class PathTemplate(
    val originalPath: String
) {
    val pattern: String = originalPath.replace(VARIABLE_PATTERN, REPLACEMENT)

    fun matches(actualPath: String): Boolean {
        val regex = Regex("^" + pattern.replace("{}", "[^/]+") + "$")
        return regex.matches(actualPath)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PathTemplate

        return pattern == other.pattern
    }

    override fun hashCode(): Int {
        return pattern.hashCode()
    }

    companion object {
        private val VARIABLE_PATTERN = Regex("\\{[^}]+\\}")
        private const val REPLACEMENT = "{}"
    }
}
