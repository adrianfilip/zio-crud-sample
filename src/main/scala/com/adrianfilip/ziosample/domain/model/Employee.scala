package com.adrianfilip.ziosample.domain.model

import zio.IO
import zio.Task
import zio.ZIO
import zio.Has
import scala.util.Try
import com.adrianfilip.ziosample.domain.model.EmployeeRepository.PersistenceFailure._

//id: Adrian Filip => afilip
final case class Employee private (val id: String, val firstName: String, val lastName: String)

object Employee {
  def apply(firstName: String, lastName: String): Option[Employee] =
    Try(firstName.head.toLower + lastName.substring(0, 5).toLowerCase).toOption
      .map(id => Employee(id, firstName, lastName))

}

object EmployeeRepository {

  type EmployeeRepository = Has[EmployeeRepository.Service]

  trait Service {
    def save(employee: Employee): IO[PersistenceFailure, Employee]
    def get(id: String): IO[PersistenceFailure, Option[Employee]]
    def getAll(): IO[PersistenceFailure, Seq[Employee]]
    def delete(id: String): IO[PersistenceFailure, Unit]
  }

  sealed trait PersistenceFailure
  object PersistenceFailure {
    final case class UnexpectedPersistenceFailure(err: Throwable) extends PersistenceFailure
  }

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
