package org.sansoft.testkube.testkubebackend

import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HelloController {

    companion object{
        val LOGGER = LoggerFactory.getLogger(HelloController::class.java)
    }

    @GetMapping("/hello")
    fun hello(): String {
        LOGGER.info("Hello request received")
        var getenv: String? = System.getenv("POD_NAME")
        return "Hello from [${getenv ?: "UNKNOW"}]"
    }

    @GetMapping("/env")
    fun envVariables(): String {
        LOGGER.info("Env request received")
        var envVariable: MutableMap<String, String> = System.getenv()
        return envVariable.entries.joinToString("\n")
    }

}