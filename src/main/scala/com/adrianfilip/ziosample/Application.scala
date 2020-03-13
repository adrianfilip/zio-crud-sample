package com.adrianfilip.ziosample

import zio.ZIO

object Application extends zio.App {

  def run(args: List[String]): ZIO[zio.ZEnv, Nothing, Int] =
    (for {
      _ <- zio.console.putStrLn("Hello")
    } yield ()).map(_ => 0)

}
