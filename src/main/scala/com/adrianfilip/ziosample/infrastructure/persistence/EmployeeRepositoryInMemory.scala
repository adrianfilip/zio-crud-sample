package com.adrianfilip.ziosample.infrastructure.persistence

import com.adrianfilip.ziosample.domain.model.EmployeeRepository
import com.adrianfilip.ziosample.domain.model.EmployeeRepository.EmployeeAlreadyExists
import com.adrianfilip.ziosample.domain.model.EmployeeRepository.EmployeeDoesNotExist
import com.adrianfilip.ziosample.domain.model.Employee
import zio.IO
import zio.Task

object EmployeeRepositoryInMemory extends EmployeeRepository.Service {

  private val db: java.util.HashMap[String, Employee] = new java.util.HashMap()

  override def create(employee: Employee): zio.IO[EmployeeRepository.CreateEmployeeFailure, Employee] =
    if (db.containsKey(employee.id)) {
      IO.fail(EmployeeAlreadyExists(employee.id))
    } else {
      db.put(employee.id, employee)
      IO.succeed(employee)
    }

  override def get(id: String): zio.Task[Option[Employee]] = Task.succeed(Option(db.get(id)))

  import collection.JavaConverters._
  override def getAll(): zio.Task[Seq[Employee]] = Task.succeed(db.values().asScala.toSeq)

  override def delete(id: String): zio.IO[EmployeeRepository.DeleteEmployeeFailure, Unit] =
    if (db.containsKey(id)) {
      db.remove(id)
      IO.succeed(())
    } else {
      IO.fail(EmployeeDoesNotExist(id))
    }

}
