package com.adrianfilip.ziosample

import zio.ZIO
import zio.console.Console
import zio.console.putStrLn
import zio.console.getStrLn
import zio.IO
import scala.util.Try
import com.adrianfilip.ziosample.infrastructure.CRUDOperation
import com.adrianfilip.ziosample.infrastructure.CRUDOperation._

object Application extends zio.App {

  def run(args: List[String]): ZIO[zio.ZEnv, Nothing, Int] =
    (for {
      _ <- putStrLn("Hello")
      _ <- program()
    } yield ()).map(_ => 0) orElse ZIO.succeed(1)

  def program(): ZIO[Console, Throwable, Nothing] =
    (for {
      _ <- putStrLn("Please select next operation to perform:")
      _ <- putStrLn("1 for Create") *> putStrLn("2 for Read") *> putStrLn("3 for Update") *> putStrLn("4 for Delete") *> putStrLn(
            "5 for GetAll"
          )
      selection <- getStrLn
      _ <- (CRUDOperation.selectOperation(selection) match {
            case Some(op) => dispatch(op)
            case None     => putStrLn(s"$selection is not a valid selection, please try again!")
          }) *> program
    } yield ()) *> ZIO.never

  def dispatch(operation: CRUDOperation): ZIO[Console, Throwable, Unit] = operation match {
    case Create(_) => Controller.create
    case Read(_)   => Controller.read
    case Update(_) => Controller.update
    case Delete(_) => Controller.delete
    case GetAll(_) => Controller.getAll
  }

  object Controller {
    val create: ZIO[Console, Throwable, Unit] =
      for {
        _            <- putStrLn("Please insert Employee as json:")
        employeeJson <- getStrLn
        _            <- putStrLn(s"You inserted $employeeJson")
      } yield ()

    val read: ZIO[Console, Throwable, Unit] =
      for {
        _  <- putStrLn("Please insert id of Employee you want to read:")
        id <- getStrLn
        _  <- putStrLn(s"You inserted id $id")
      } yield ()

    val update: ZIO[Console, Throwable, Unit] =
      for {
        _            <- putStrLn("Please insert Employee as json:")
        employeeJson <- getStrLn
        //   daca nu are ID o sa dau mesaj cannot update employee,  missing id
        _ <- putStrLn(s"You inserted $employeeJson")
      } yield ()

    val delete: ZIO[Console, Throwable, Unit] =
      for {
        _  <- putStrLn("Please insert id of Employee you want to delete:")
        id <- getStrLn
        _  <- putStrLn(s"You inserted id $id")
      } yield ()

    val getAll: ZIO[Console, Throwable, Unit] =
      for {
        _ <- putStrLn("Here are all the employees:")
        _ <- putStrLn("emp1")
        _ <- putStrLn("emp2")
      } yield ()
  }

}
