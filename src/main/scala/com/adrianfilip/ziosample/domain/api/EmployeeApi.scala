package com.adrianfilip.ziosample.domain.api

import zio.ZIO
import com.adrianfilip.ziosample.domain.model.EmployeeRepository
import com.adrianfilip.ziosample.domain.model.Employee
import com.adrianfilip.ziosample.domain.model.EmployeeRepository.CreateEmployeeFailure

//api
object EmployeeApi {

  def create(req: CreateEmployeeRequest): ZIO[EmployeeRepository, CreateEmployeeFailure, Employee] = {
    val id       = req.firstName.head.toLower + req.lastName.substring(0, 5).toLowerCase
    val employee = Employee(id = id, firstName = req.firstName, lastName = req.lastName)
    ZIO.accessM[EmployeeRepository](_.create(employee))
  }

  case class CreateEmployeeRequest(
    val firstName: String,
    val lastName: String
  )

}
