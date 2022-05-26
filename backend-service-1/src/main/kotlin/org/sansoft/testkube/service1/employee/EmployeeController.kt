package org.sansoft.testkube.service1.employee

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
class EmployeeController(val employeeService: EmployeeService) {

    @GetMapping("/employee/info/{empId}")
    fun getEmployeeProfile(@PathVariable("empId") empId: Int): Mono<EmployeeInfo> {
        return employeeService.getEmployeeProfile(empId)
    }
}