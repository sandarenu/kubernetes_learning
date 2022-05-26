package org.sansoft.testkube.service1.employee

import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

interface EmployeeService {
    fun getEmployeeProfile(empId: Int): Mono<EmployeeInfo>

}

data class Employee(val empId: Int, val name: String)
data class EmployeeInfo(val empId: Int, val name: String, val tasks: List<Task>)
data class Task(val id: Int, val name: String, val description: String)

@Component
class EmployeeServiceImpl(val empTaskService: EmployeeTaskService) : EmployeeService {

    override fun getEmployeeProfile(empId: Int): Mono<EmployeeInfo> {
        val emp = Mono.just(Employee(1, "Sandarenu"))
        val tasks = empTaskService.getTasks(empId)

        return Mono.zip(emp, tasks)
            .map { r -> EmployeeInfo(r.t1.empId, r.t1.name, r.t2) }
    }
}
