package com.adrianfilip.ziosample.infrastructure

import scala.util.Try

sealed trait CRUDOperation

object CRUDOperation {

  final object Create extends CRUDOperation {
    val index: Int = 1
  }

  final object Read extends CRUDOperation {
    val index: Int = 2
  }

  final object Update extends CRUDOperation {
    val index: Int = 3
  }

  final object Delete extends CRUDOperation {
    val index: Int = 4
  }

  final object GetAll extends CRUDOperation {
    val index: Int = 5
  }

  final object ExitApp extends CRUDOperation {
    val index: Int = 6
  }

  def selectOperation(selection: String): Option[CRUDOperation] = Try(Integer.parseInt(selection)).toOption match {
    case None => None
    case Some(index) =>
      index match {
        case Create.index  => Some(Create)
        case Read.index    => Some(Read)
        case Update.index  => Some(Update)
        case Delete.index  => Some(Delete)
        case GetAll.index  => Some(GetAll)
        case ExitApp.index => Some(ExitApp)
        case _             => None
      }
  }
}
