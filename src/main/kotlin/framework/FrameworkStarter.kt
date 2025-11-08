package framework

import framework.config.FrameworkConfig

private val frameworkConfig = FrameworkConfig()

fun runWithFramework() {
    val server = frameworkConfig.server

    server.run()
}
