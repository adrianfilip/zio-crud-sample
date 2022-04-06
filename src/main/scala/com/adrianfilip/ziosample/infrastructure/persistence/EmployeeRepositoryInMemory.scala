package com.adrianfilip.ziosample.infrastructure.persistence

import com.adrianfilip.ziosample.domain.model.EmployeeRepository
import com.adrianfilip.ziosample.domain.model.EmployeeRepository.PersistenceFailure
import com.adrianfilip.ziosample.domain.model.EmployeeRepository.PersistenceFailure._
import com.adrianfilip.ziosample.domain.model.Employee
import zio._

case class EmployeeRepositoryInMemory() extends EmployeeRepository {

  private val db: java.util.HashMap[String, Employee] = new java.util.HashMap()

  override def save(employee: Employee): IO[PersistenceFailure, Employee] =
    IO {
      db.put(employee.id, employee)
      employee
    }.mapError(UnexpectedPersistenceFailure(_))

 

  override def get(id: String): IO[PersistenceFailure, Option[Employee]] =
    IO(Option(db.get(id))).mapError(UnexpectedPersistenceFailure(_))

  import collection.JavaConverters._
  override def getAll(): IO[PersistenceFailure, Seq[Employee]] =
    IO(db.values().asScala.toSeq).mapError(UnexpectedPersistenceFailure(_))

  override def delete(id: String): IO[PersistenceFailure, Unit] =
    IO {
      db.remove(id)
      ()
    }.mapError(e => UnexpectedPersistenceFailure(e))

  // if (db.containsKey(id)) {
  //   db.remove(id)
  //   IO.succeed(())
  // } else {
  //   IO.fail(EmployeeDoesNotExist(id))
  // }

}

object EmployeeRepositoryInMemory extends (() => EmployeeRepository) {
  val layer: ULayer[EmployeeRepository] = 
    ZLayer.fromFunction(_ => EmployeeRepositoryInMemory())
    //EmployeeRepositoryInMemory.toLayer // Removed in ZIO 2.0.0-RC4
}
