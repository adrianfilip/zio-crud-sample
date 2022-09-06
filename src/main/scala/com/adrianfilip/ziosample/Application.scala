package com.adrianfilip.ziosample

import zio._
import Console.{readLine,printLine}
import scala.util.Try
import com.adrianfilip.ziosample.infrastructure._
import com.adrianfilip.ziosample.domain.model._
import com.adrianfilip.ziosample.infrastructure.environments._
import CRUDOperation._

object Application extends ZIOAppDefault {

  type ApplicationEnvironment = Console with EmployeeRepository

  val localApplicationEnvironment = Console.live ++ EmployeeRepositoryEnv.inMemory

  def run = {
    getArgs.flatMap { args =>
      args.headOption match {
        case Some("local") => program().provideLayer(localApplicationEnvironment)
        case Some(profile) => ZIO.dieMessage(s"Unsupported profile $profile")
        case None => ZIO.dieMessage("Please provide a profile (e.g. 'local') in the arguments.")
      }
    }
    
  }

  def program(): ZIO[ApplicationEnvironment, Nothing, Int] =
    (for {
      _ <- printLine("Please select next operation to perform:")
      _ <- printLine(s"${Create.index} for Create") *> printLine(s"${Read.index} for Read") *> printLine(
            s"${Update.index} for Update"
          ) *> printLine(s"${Delete.index} for Delete") *> printLine(
            s"${GetAll.index} for GetAll"
          ) *> printLine(
            s"${ExitApp.index} for ExitApp"
          )
      selection <- readLine
      _ <- CRUDOperation.selectOperation(selection) match {
            case Some(op) =>
              op match {
                case ExitApp => printLine(s"Shutting down")
                case _ =>
                  dispatch(op)
                    .tapError(e => printLine(s"Failed with: $e"))
                    .flatMap(s => printLine(s"Succeeded with $s") *> program) orElse program
              }
            case None => printLine(s"$selection is not a valid selection, please try again!") *> program
          }
    } yield 0).tapError(e => printLine(s"Unexpected Failure $e")) orElse ZIO.succeed(1)

  def dispatch(operation: CRUDOperation): ZIO[Console with EmployeeRepository, Any, Any] = operation match {
    case Create  => Controller.create
    case Read    => Controller.read
    case Update  => Controller.update
    case Delete  => Controller.delete
    case GetAll  => Controller.getAll
    case ExitApp => ZIO.fail("How did I get here?")
  }

}
