package com.adrianfilip.ziosample.domain.api

import zio.ZIO
import com.adrianfilip.ziosample.domain.model.Employee
import com.adrianfilip.ziosample.domain.model.EmployeeRepository
import com.adrianfilip.ziosample.domain.model.EmployeeRepository.PersistenceFailure
import com.adrianfilip.ziosample.domain.api.EmployeeApi.ValidationError._
import com.adrianfilip.ziosample.domain.api.EmployeeApi.BusinessFailure._

object EmployeeApi {

  def create(
    req: CreateEmployeeRequest
  ): ZIO[EmployeeRepository, Any, Employee] =
    Employee(firstName = req.firstName, lastName = req.lastName) match {
      case None => ZIO.fail(InvalidInput(req.firstName, req.lastName))
      case Some(employee) =>
        EmployeeRepository.get(employee.id).flatMap {
          case Some(_) => ZIO.fail(EmployeeAlreadyExists(employee.id))
          case None    => EmployeeRepository.save(employee)
        }
    }

  sealed trait ValidationError
  object ValidationError {
    final case class InvalidInput(val fistName: String, val lastName: String) extends ValidationError
  }

  sealed trait BusinessFailure
  object BusinessFailure {
    final case class EmployeeAlreadyExists(id: String) extends BusinessFailure
    final case class EmployeeDoesNotExist(id: String)  extends BusinessFailure
  }

  def update(
    req: UpdateEmployeeRequest
  ): ZIO[EmployeeRepository, Any, Employee] =
    for {
      oldVersion <- if (req.firstName.isEmpty() || req.lastName.isEmpty())
                     ZIO.fail(InvalidInput(req.firstName, req.lastName))
                   else
                     EmployeeRepository
                       .get(req.id)
                       .flatMap {
                         case Some(emp) => ZIO.succeed(emp)
                         case None      => ZIO.fail(EmployeeDoesNotExist(req.id))
                       }
      result <- EmployeeRepository
                 .save(oldVersion.copy(firstName = req.firstName, lastName = req.lastName))
    } yield result

  def getAll(): ZIO[EmployeeRepository, PersistenceFailure, Seq[Employee]] =
    EmployeeRepository.getAll()

  def get(id: String): ZIO[EmployeeRepository, PersistenceFailure, Option[Employee]] =
    EmployeeRepository.get(id)

  def delete(id: String): ZIO[EmployeeRepository, PersistenceFailure, Unit] =
    EmployeeRepository.delete(id)

  final case class CreateEmployeeRequest(
    val firstName: String,
    val lastName: String
  )

  final case class UpdateEmployeeRequest(
    val id: String,
    val firstName: String,
    val lastName: String
  )

}
