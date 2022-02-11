package com.adrianfilip.ziosample.domain.model

import zio._
import scala.util.Try

final case class Employee private (val id: String, val firstName: String, val lastName: String)

object Employee {

  def apply(firstName: String, lastName: String): Option[Employee] =
    Try(firstName.head.toLower + lastName.substring(0, 5).toLowerCase).toOption
      .map(id => Employee(id, firstName, lastName))

}

trait EmployeeRepository {
  import EmployeeRepository.PersistenceFailure
    def save(employee: Employee): IO[PersistenceFailure, Employee]
    def get(id: String): IO[PersistenceFailure, Option[Employee]]
    def getAll(): IO[PersistenceFailure, Seq[Employee]]
    def delete(id: String): IO[PersistenceFailure, Unit]
}

object EmployeeRepository {
  def save(employee: Employee) = ZIO.serviceWithZIO[EmployeeRepository](_.save(employee))
  def get(id: String) = ZIO.serviceWithZIO[EmployeeRepository](_.get(id))
  def getAll() = ZIO.serviceWithZIO[EmployeeRepository](_.getAll())
  def delete(id: String) = ZIO.serviceWithZIO[EmployeeRepository](_.delete(id))

  sealed trait PersistenceFailure
  object PersistenceFailure {
    final case class UnexpectedPersistenceFailure(err: Throwable) extends PersistenceFailure
  }
}