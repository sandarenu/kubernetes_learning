package org.sansoft.testkube.service1.employee

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import java.util.*


@Component
class EmployeeTaskService( @Value("\${task.service.url}") val taskServiceUrl: String) {

    var client = WebClient.builder()
        .baseUrl(taskServiceUrl)
        .defaultCookie("cookieKey", "cookieValue")
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .build()

    fun getTasks(empId: Int): Mono<List<Task>> {
        return client
            .get()
            .uri("/tasks/{empId}", empId)
            .retrieve()
            .bodyToFlux(Task::class.java)
            .collectList()
    }
}
