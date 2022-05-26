package org.sansoft.testkube.service2.tasks

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
class TasksController(val taskService: TaskService) {

    @GetMapping("/tasks/{empId}")
    fun getTasks(@PathVariable("empId") empId: Int): Mono<List<Task>> {
        return taskService.getTasks(empId)
    }
}