package com.adrianfilip.ziosample.infrastructure.environments

import com.adrianfilip.ziosample.domain.model.Employee
import com.adrianfilip.ziosample.domain.model.EmployeeRepository
import zio.Task
import zio.IO
import zio.{ Has, ZLayer }
import com.adrianfilip.ziosample.infrastructure.persistence.EmployeeRepositoryInMemory
import zio.ZIO
import zio.Layer

object EmployeeRepositoryEnv {

  val inMemory: Layer[Nothing, Has[EmployeeRepository.Service]] = ZLayer.succeed(EmployeeRepositoryInMemory)

}
