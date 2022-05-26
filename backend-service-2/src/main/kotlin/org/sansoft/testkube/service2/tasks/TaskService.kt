package org.sansoft.testkube.service2.tasks

import org.springframework.stereotype.Component
import reactor.core.publisher.Mono


interface TaskService {
    fun getTasks(empId: Int): Mono<List<Task>>

}

data class Task(val id: Int, val name: String, val description: String)

@Component
class TaskServiceImpl : TaskService {
    override fun getTasks(empId: Int): Mono<List<Task>> {
        return Mono.just(listOf(
            Task(1, "Task1", "Task1 description"),
            Task(2, "Task2", "Task2 description")
        ))
    }
}
