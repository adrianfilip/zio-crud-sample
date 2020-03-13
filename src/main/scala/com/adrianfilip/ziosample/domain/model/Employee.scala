package com.adrianfilip.ziosample.domain.model

import zio.Task
import zio.IO
import zio.ZIO
import EmployeeRepository.CreateEmployeeFailure
import EmployeeRepository.DeleteEmployeeFailure

//id: Adrian Filip => afilip
case class Employee(val id: String, val firstName: String, val lastName: String)

trait EmployeeRepository {
  def create(employee: Employee): IO[CreateEmployeeFailure, Employee]
  def get(id: String): Task[Option[Employee]]
  def getAll(): Task[Seq[Employee]]
  def delete(id: String): IO[DeleteEmployeeFailure, Unit]
}

object EmployeeRepository {
  sealed trait CreateEmployeeFailure
  final case class EmployeeAlreadyExists(id: String)         extends CreateEmployeeFailure
  final case class UnexpectedCreationFailure(err: Throwable) extends CreateEmployeeFailure

  sealed trait GetEmployeeFailure
  final case class UnexpectedGetEmployeeFailure(id: String, err: Throwable) extends GetEmployeeFailure

  sealed trait DeleteEmployeeFailure
  final case class EmployeeDoesNotExist(id: String)          extends DeleteEmployeeFailure
  final case class UnexpectedDeletionFailure(err: Throwable) extends DeleteEmployeeFailure
}


// la environment o sa fac asa EmployeeRepositoryInMemory & EmployeeRepositoryLive

