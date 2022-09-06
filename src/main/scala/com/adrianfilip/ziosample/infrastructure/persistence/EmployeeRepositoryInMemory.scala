package com.adrianfilip.ziosample.infrastructure.persistence

import com.adrianfilip.ziosample.domain.model.EmployeeRepository
import com.adrianfilip.ziosample.domain.model.EmployeeRepository.PersistenceFailure
import com.adrianfilip.ziosample.domain.model.EmployeeRepository.PersistenceFailure._
import com.adrianfilip.ziosample.domain.model.Employee
import zio._

case class EmployeeRepositoryInMemory() extends EmployeeRepository {

  private val db: java.util.HashMap[String, Employee] = new java.util.HashMap()

  override def save(employee: Employee): IO[PersistenceFailure, Employee] =
    ZIO.attempt {
      db.put(employee.id, employee)
      employee
    }.mapError(UnexpectedPersistenceFailure(_))

  override def get(id: String): IO[PersistenceFailure, Option[Employee]] =
    ZIO.attempt(Option(db.get(id))).mapError(UnexpectedPersistenceFailure(_))

  import collection.JavaConverters._
  override def getAll(): IO[PersistenceFailure, Seq[Employee]] =
    ZIO.attempt(db.values().asScala.toSeq).mapError(UnexpectedPersistenceFailure(_))

  override def delete(id: String): IO[PersistenceFailure, Unit] =
    ZIO.attempt {
      db.remove(id)
      ()
    }.mapError(e => UnexpectedPersistenceFailure(e))

}

object EmployeeRepositoryInMemory extends (() => EmployeeRepository) {

  val layer: ULayer[EmployeeRepository] = 
    ZLayer.fromFunction(() => EmployeeRepositoryInMemory())
    //EmployeeRepositoryInMemory.toLayer // Removed in ZIO 2.0.0-RC4
}
