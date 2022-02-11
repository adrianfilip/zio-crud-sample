package com.adrianfilip.ziosample.infrastructure

import zio._
import Console.{printLine,readLine}
import com.adrianfilip.ziosample.domain.model._
import com.adrianfilip.ziosample.domain.api.EmployeeApi
import com.adrianfilip.ziosample.domain.api.EmployeeApi.{CreateEmployeeRequest,UpdateEmployeeRequest}
import com.adrianfilip.ziosample.infrastructure.environments.EmployeeRepositoryEnv

object Controller {

  val create: ZIO[Console with EmployeeRepository, Any, Employee] =
    for {
      _             <- printLine("Please insert Employee first name:")
      firstName     <- readLine
      _             <- printLine("Please insert Employee last name:")
      lastName      <- readLine
      savedEmployee <- EmployeeApi.create(CreateEmployeeRequest(firstName, lastName))
    } yield savedEmployee

  val read: ZIO[Console with EmployeeRepository, Any, Option[Employee]] =
    for {
      _        <- printLine("Please insert id of Employee you want to read:")
      id       <- readLine
      employee <- EmployeeApi.get(id)
    } yield employee

  val update: ZIO[Console with EmployeeRepository, Any, Employee] =
    for {
      _             <- printLine("Please insert Employee id:")
      id            <- readLine
      _             <- printLine("Please insert Employee new first name:")
      firstName     <- readLine
      _             <- printLine("Please insert Employee new last name:")
      lastName      <- readLine
      savedEmployee <- EmployeeApi.update(UpdateEmployeeRequest(id, firstName, lastName))
    } yield savedEmployee

  val delete: ZIO[Console with EmployeeRepository, Any, Unit] =
    for {
      _  <- printLine("Please insert id of Employee you want to delete:")
      id <- readLine
      _  <- EmployeeApi.delete(id)
    } yield ()

  val getAll: ZIO[Console with EmployeeRepository, Any, Seq[Employee]] =
    EmployeeApi.getAll()
}
