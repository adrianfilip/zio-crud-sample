package com.adrianfilip.ziosample

import zio.ZIO
import zio.console.Console
import zio.console.putStrLn
import zio.console.getStrLn
import zio.IO
import scala.util.Try
import com.adrianfilip.ziosample.infrastructure.CRUDOperation
import com.adrianfilip.ziosample.infrastructure.CRUDOperation._
import com.adrianfilip.ziosample.infrastructure.Controller
import com.adrianfilip.ziosample.domain.model.EmployeeRepository.EmployeeRepository
import com.adrianfilip.ziosample.domain.model.EmployeeRepository
import com.adrianfilip.ziosample.infrastructure.environments.EmployeeRepositoryEnv

object Application extends zio.App {

  def run(args: List[String]): ZIO[zio.ZEnv, Nothing, Int] =
    (for {
      _ <- putStrLn("Hello")
      _ <- program().provideLayer(Console.live ++ EmployeeRepositoryEnv.inMemory)
    } yield ()).map(_ => 0) orElse ZIO.succeed(1)

  def program(): ZIO[Console with EmployeeRepository, Any, Nothing] =
    (for {
      _ <- putStrLn("Please select next operation to perform:")
      _ <- putStrLn("1 for Create") *> putStrLn("2 for Read") *> putStrLn("3 for Update") *> putStrLn("4 for Delete") *> putStrLn(
            "5 for GetAll"
          )
      selection <- getStrLn
      _ <- (CRUDOperation.selectOperation(selection) match {
            case Some(op) =>
              dispatch(op).tapError(e => putStrLn(s"Failed with: $e")).flatMap(s => putStrLn(s"Succeeded with $s"))
            case None => putStrLn(s"$selection is not a valid selection, please try again!")
          }) *> program
    } yield ()) *> ZIO.never

  def dispatch(operation: CRUDOperation): ZIO[Console with EmployeeRepository, Any, Any] = operation match {
    case Create => Controller.create
    case Read   => Controller.read
    case Update => Controller.update
    case Delete => Controller.delete
    case GetAll => Controller.getAll
  }

}
