package com.adrianfilip.ziosample.domain.model

import zio.IO
import zio.Task
import zio.ZIO
import zio.Has

//id: Adrian Filip => afilip
final case class Employee(val id: String, val firstName: String, val lastName: String)

object EmployeeRepository {

  type EmployeeRepository = Has[EmployeeRepository.Service]

  trait Service {
    def save(employee: Employee): IO[PersistenceFailure, Employee]
    def get(id: String): IO[PersistenceFailure, Option[Employee]]
    def getAll(): IO[PersistenceFailure, Seq[Employee]]
    def delete(id: String): IO[PersistenceFailure, Unit]
  }

  sealed trait PersistenceFailure
  final case class EmployeeAlreadyExists(id: String)            extends PersistenceFailure
  final case class UnexpectedPersistenceFailure(err: Throwable) extends PersistenceFailure
  final case class EmployeeDoesNotExist(id: String)             extends PersistenceFailure

  //   accessor methods
  def save(
    employee: Employee
  ): ZIO[EmployeeRepository, PersistenceFailure, Employee] =
    ZIO.accessM(_.get.save(employee))

  def get(id: String): ZIO[EmployeeRepository, PersistenceFailure, Option[Employee]] =
    ZIO.accessM(_.get.get(id))

  def getAll(): ZIO[EmployeeRepository, PersistenceFailure, Seq[Employee]] =
    ZIO.accessM(_.get.getAll())

  def delete(id: String): ZIO[EmployeeRepository, PersistenceFailure, Unit] =
    ZIO.accessM(_.get.delete(id))
}
