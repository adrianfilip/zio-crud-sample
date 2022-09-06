package com.adrianfilip.ziosample.infrastructure.environments

import com.adrianfilip.ziosample.domain.model.Employee
import com.adrianfilip.ziosample.domain.model.EmployeeRepository
import zio._
import com.adrianfilip.ziosample.infrastructure.persistence.EmployeeRepositoryInMemory


object EmployeeRepositoryEnv {
  
  val inMemory: ULayer[EmployeeRepository] = EmployeeRepositoryInMemory.layer

}
