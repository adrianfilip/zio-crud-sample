package com.adrianfilip.ziosample.infrastructure

import zio.ZIO
import zio.Has
import zio.console.Console
import zio.console.putStrLn
import zio.console.getStrLn
import com.adrianfilip.ziosample.domain.model.Employee
import com.adrianfilip.ziosample.domain.model.EmployeeRepository
import com.adrianfilip.ziosample.domain.model.EmployeeRepository.EmployeeRepository
import com.adrianfilip.ziosample.domain.api.EmployeeApi
import com.adrianfilip.ziosample.domain.api.EmployeeApi.CreateEmployeeRequest
import com.adrianfilip.ziosample.domain.api.EmployeeApi.UpdateEmployeeRequest
import com.adrianfilip.ziosample.infrastructure.environments.EmployeeRepositoryEnv

object Controller {

  val create: ZIO[Console with EmployeeRepository, Any, Employee] =
    for {
      _             <- putStrLn("Please insert Employee first name:")
      firstName     <- getStrLn
      _             <- putStrLn("Please insert Employee last name:")
      lastName      <- getStrLn
      savedEmployee <- EmployeeApi.create(CreateEmployeeRequest(firstName, lastName))
    } yield savedEmployee

  val read: ZIO[Console with EmployeeRepository, Any, Option[Employee]] =
    for {
      _        <- putStrLn("Please insert id of Employee you want to read:")
      id       <- getStrLn
      employee <- EmployeeApi.get(id)
    } yield employee

  val update: ZIO[Console with EmployeeRepository, Any, Employee] =
    for {
      _             <- putStrLn("Please insert Employee id:")
      id            <- getStrLn
      _             <- putStrLn("Please insert Employee new first name:")
      firstName     <- getStrLn
      _             <- putStrLn("Please insert Employee new last name:")
      lastName      <- getStrLn
      savedEmployee <- EmployeeApi.update(UpdateEmployeeRequest(id, firstName, lastName))
    } yield savedEmployee

  val delete: ZIO[Console with EmployeeRepository, Any, Unit] =
    for {
      _  <- putStrLn("Please insert id of Employee you want to delete:")
      id <- getStrLn
      _  <- EmployeeApi.delete(id)
    } yield ()

  val getAll: ZIO[Console with EmployeeRepository, Any, Seq[Employee]] =
    EmployeeApi.getAll()
}
