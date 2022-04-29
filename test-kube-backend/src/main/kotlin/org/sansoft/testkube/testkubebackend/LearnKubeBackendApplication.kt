package org.sansoft.testkube.testkubebackend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class LearnKubeBackendApplication

fun main(args: Array<String>) {
	runApplication<LearnKubeBackendApplication>(*args)
}
