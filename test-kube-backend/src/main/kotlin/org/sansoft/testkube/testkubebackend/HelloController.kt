package org.sansoft.testkube.testkubebackend

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HelloController {

    @GetMapping("/hello")
    fun hello(): String {
        var getenv: String? = System.getenv("POD_NAME")
        return "Hello from [${getenv ?: "UNKNOW"}]"
    }

    @GetMapping("/env")
    fun envVariables(): String {
        var envVariable: MutableMap<String, String> = System.getenv()
        return envVariable.entries.joinToString("\n")
    }

}