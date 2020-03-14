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
import zio.ZLayer
import zio.Task

object Application extends zio.App {

  type ApplicationEnvironment = Console with EmployeeRepository

  val localApplicationEnvironment = Console.live ++ EmployeeRepositoryEnv.inMemory

  def run(args: List[String]): ZIO[zio.ZEnv, Nothing, Int] = {
    val profile = Try(args.head).getOrElse("")

    if (profile == "local") program().provideLayer(localApplicationEnvironment)
    else putStrLn(s"Unsupported profile $profile") *> ZIO.succeed(1)

  }

  def program(): ZIO[ApplicationEnvironment, Nothing, Int] =
    (for {
      _ <- putStrLn("Please select next operation to perform:")
      _ <- putStrLn(s"${Create.index} for Create") *> putStrLn(s"${Read.index} for Read") *> putStrLn(
            s"${Update.index} for Update"
          ) *> putStrLn(s"${Delete.index} for Delete") *> putStrLn(
            s"${GetAll.index} for GetAll"
          ) *> putStrLn(
            s"${ExitApp.index} for ExitApp"
          )
      selection <- getStrLn
      _ <- CRUDOperation.selectOperation(selection) match {
            case Some(op) =>
              op match {
                case ExitApp => putStrLn(s"Shutting down")
                case _ =>
                  dispatch(op)
                    .tapError(e => putStrLn(s"Failed with: $e"))
                    .flatMap(s => putStrLn(s"Succeeded with $s") *> program) orElse program
              }
            case None => putStrLn(s"$selection is not a valid selection, please try again!") *> program
          }
    } yield 0).tapError(e => putStrLn(s"Unexpected Failure $e")) orElse ZIO.succeed(1)

  def dispatch(operation: CRUDOperation): ZIO[Console with EmployeeRepository, Any, Any] = operation match {
    case Create  => Controller.create
    case Read    => Controller.read
    case Update  => Controller.update
    case Delete  => Controller.delete
    case GetAll  => Controller.getAll
    case ExitApp => ZIO.fail("How did I get here?")
  }

}
