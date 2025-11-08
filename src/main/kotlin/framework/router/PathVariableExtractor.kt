package framework.router

import framework.exception.IllegalRequestException
import java.net.URLDecoder
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

class PathVariableExtractor(
    private val variableTokenRegex: Regex = DEFAULT_VARIABLE_TOKEN_REGEX,
    private val decodeCharset: Charset = DEFAULT_DECODE_CHARSET
) {
    fun extract(routeKey: RouteKey, actualPath: String): Map<String, String> {
        val template = routeKey.pathTemplate.originalPath

        val normalizedTemplate = normalizePath(template)
        val normalizedActual = normalizePath(actualPath)

        val variableNames = template.pathVariableNames
        val fullMatchRegex = compileFullMatchRegexFromTemplate(normalizedTemplate)

        val match = match(fullMatchRegex, normalizedActual, template, actualPath)
        val variableValues = decodeCapturedGroups(match)

        return zipNamesWithValues(variableNames, variableValues)
    }

    private val String.pathVariableNames: List<String>
        get() {
            return variableTokenRegex.findAll(this)
                .map { it.groupValues[VARIABLE_NAME_GROUP_INDEX] }
                .toList()
        }

    private fun compileFullMatchRegexFromTemplate(template: String): Regex {
        val builder = StringBuilder()
        var cursor = 0

        val tokens = variableTokenRegex.findAll(template)
        for (token in tokens) {
            if (token.range.first > cursor) {
                val literal = template.substring(cursor, token.range.first)
                builder.append(Regex.escape(literal))
            }
            builder.append(REGEX_SEGMENT_CAPTURE)
            cursor = token.range.last + 1
        }

        if (cursor < template.length) {
            val tail = template.substring(cursor)
            builder.append(Regex.escape(tail))
        }

        return Regex(REGEX_ANCHOR_START + builder.toString() + REGEX_ANCHOR_END)
    }

    private fun match(
        regex: Regex,
        normalizedActualPath: String,
        originalTemplate: String,
        originalActualPath: String
    ): MatchResult {
        return regex.matchEntire(normalizedActualPath)
            ?: throw IllegalRequestException(
                "$PATH_MISMATCH template=$originalTemplate, path=$originalActualPath"
            )
    }

    private fun decodeCapturedGroups(matchResult: MatchResult): List<String> {
        return matchResult.groupValues
            .drop(INDEX_WHOLE_MATCH)
            .map { decodeUrl(it) }
    }

    private fun zipNamesWithValues(
        names: List<String>,
        values: List<String>
    ): Map<String, String> {
        if (names.size != values.size) {
            throw IllegalRequestException(
                "$COUNT_MISMATCH names=${names.size}, values=${values.size}"
            )
        }

        return names.zip(values).toMap()
    }

    private fun normalizePath(path: String): String {
        if (path.isEmpty()) {
            return ROOT_PATH
        }

        var normalized = ensureStartsWithSlash(path)
        if (normalized.length > 1 && normalized.endsWith(SLASH)) {
            normalized = normalized.dropLast(1)
        }

        return normalized
    }

    private fun ensureStartsWithSlash(path: String): String {
        return if (path.startsWith(SLASH)) path else SLASH + path
    }

    private fun decodeUrl(value: String): String {
        return URLDecoder.decode(value, decodeCharset)
    }

    companion object {
        // 정규식 빌딩용 상수
        private const val REGEX_SEGMENT_CAPTURE: String = "([^/]+)"
        private const val REGEX_ANCHOR_START: String = "^"
        private const val REGEX_ANCHOR_END: String = "$"

        // 에러 메시지
        private const val PATH_MISMATCH: String = "경로가 액션 템플릿과 일치하지 않습니다."
        private const val COUNT_MISMATCH: String = "변수명 개수와 값 개수가 일치하지 않습니다."

        // 경로 상수
        private const val SLASH: String = "/"
        private const val ROOT_PATH: String = "/"

        // 그룹 인덱스
        private const val VARIABLE_NAME_GROUP_INDEX: Int = 1
        private const val INDEX_WHOLE_MATCH: Int = 1

        // 변수 토큰: {name}
        private val DEFAULT_VARIABLE_TOKEN_REGEX: Regex = "\\{([^/}]+)}".toRegex()

        // 디코딩 문자셋
        private val DEFAULT_DECODE_CHARSET: Charset = StandardCharsets.UTF_8
    }
}
