package com.adrianfilip.ziosample.domain.api

import zio.ZIO
import com.adrianfilip.ziosample.domain.model.Employee
import zio.Has
import com.adrianfilip.ziosample.domain.model.EmployeeRepository
import com.adrianfilip.ziosample.domain.model.EmployeeRepository.CreateEmployeeFailure

object EmployeeApi {

  def create(req: CreateEmployeeRequest): ZIO[Has[EmployeeRepository.Service], CreateEmployeeFailure, Employee] = {
    val id       = req.firstName.head.toLower + req.lastName.substring(0, 5).toLowerCase
    val employee = Employee(id = id, firstName = req.firstName, lastName = req.lastName)
    EmployeeRepository.create(employee)
  }

  case class CreateEmployeeRequest(
    val firstName: String,
    val lastName: String
  )

}
