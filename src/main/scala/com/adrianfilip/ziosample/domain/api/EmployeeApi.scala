package com.adrianfilip.ziosample.domain.api

import zio.ZIO
import com.adrianfilip.ziosample.domain.model.Employee
import zio.Has
import com.adrianfilip.ziosample.domain.model.EmployeeRepository
import com.adrianfilip.ziosample.domain.model.EmployeeRepository.EmployeeRepository
import com.adrianfilip.ziosample.domain.model.EmployeeRepository.EmployeeDoesNotExist
import com.adrianfilip.ziosample.domain.model.EmployeeRepository.PersistenceFailure

object EmployeeApi {

  def create(req: CreateEmployeeRequest): ZIO[EmployeeRepository, PersistenceFailure, Employee] = {
    val id       = req.firstName.head.toLower + req.lastName.substring(0, 5).toLowerCase
    val employee = Employee(id = id, firstName = req.firstName, lastName = req.lastName)
    EmployeeRepository.save(employee)
  }

  def update(req: UpdateEmployeeRequest): ZIO[EmployeeRepository, PersistenceFailure, Employee] = {
    for {
      oldVersion <- EmployeeRepository.get(req.id).flatMap {
                     case Some(emp) => ZIO.succeed(emp)
                     case None      => ZIO.fail(EmployeeDoesNotExist(req.id))
                   }
      result <- EmployeeRepository.save(oldVersion.copy(firstName = req.firstName, lastName = req.lastName))
    } yield result
  }

  def getAll(): ZIO[EmployeeRepository, PersistenceFailure, Seq[Employee]] =
    EmployeeRepository.getAll()

  def get(id: String): ZIO[EmployeeRepository, PersistenceFailure, Option[Employee]] =
    EmployeeRepository.get(id)

  def delete(id: String): ZIO[EmployeeRepository, PersistenceFailure, Unit] =
    EmployeeRepository.delete(id)

  case class CreateEmployeeRequest(
    val firstName: String,
    val lastName: String
  )

  case class UpdateEmployeeRequest(
    val id: String,
    val firstName: String,
    val lastName: String
  )

}
