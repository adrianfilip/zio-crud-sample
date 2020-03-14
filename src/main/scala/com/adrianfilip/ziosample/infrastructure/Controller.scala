package com.adrianfilip.ziosample.infrastructure

import zio.ZIO
import zio.console.Console
import zio.console.putStrLn
import zio.console.getStrLn

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