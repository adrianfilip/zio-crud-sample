package com.adrianfilip.ziosample.domain.model

import zio.IO
import zio.Task
import zio.ZIO
import zio.Has

//id: Adrian Filip => afilip
case class Employee(val id: String, val firstName: String, val lastName: String)

object EmployeeRepository {

  trait Service {
    def create(employee: Employee): IO[CreateEmployeeFailure, Employee]
    def get(id: String): Task[Option[Employee]]
    def getAll(): Task[Seq[Employee]]
    def delete(id: String): IO[DeleteEmployeeFailure, Unit]
  }

  sealed trait CreateEmployeeFailure
  final case class EmployeeAlreadyExists(id: String)         extends CreateEmployeeFailure
  final case class UnexpectedCreationFailure(err: Throwable) extends CreateEmployeeFailure

  sealed trait GetEmployeeFailure
  final case class UnexpectedGetEmployeeFailure(id: String, err: Throwable) extends GetEmployeeFailure

  sealed trait DeleteEmployeeFailure
  final case class EmployeeDoesNotExist(id: String)          extends DeleteEmployeeFailure
  final case class UnexpectedDeletionFailure(err: Throwable) extends DeleteEmployeeFailure

  //   accessor methods
  def create(
    employee: Employee
  ): ZIO[Has[EmployeeRepository.Service], EmployeeRepository.CreateEmployeeFailure, Employee] =
    ZIO.accessM(_.get.create(employee))

  def get(id: String): ZIO[Has[EmployeeRepository.Service], Throwable, Option[Employee]] =
    ZIO.accessM(_.get.get(id))

  def getAll(): ZIO[Has[EmployeeRepository.Service], Throwable, Seq[Employee]] =
    ZIO.accessM(_.get.getAll())

  def delete(id: String): ZIO[Has[EmployeeRepository.Service], EmployeeRepository.DeleteEmployeeFailure, Unit] =
    ZIO.accessM(_.get.delete(id))
}
