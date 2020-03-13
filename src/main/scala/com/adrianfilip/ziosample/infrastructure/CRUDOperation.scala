package com.adrianfilip.ziosample.infrastructure

import scala.util.Try

sealed trait CRUDOperation

object CRUDOperation {

  final case class Create private (val index: Int = 1) extends CRUDOperation
  object Create {
    val instance = Create()
  }
  final case class Read private (val index: Int = 2) extends CRUDOperation
  object Read {
    val instance = Read()
  }
  final case class Update private (val index: Int = 3) extends CRUDOperation
  object Update {
    val instance = Update()
  }
  final case class Delete private (val index: Int = 4) extends CRUDOperation
  object Delete {
    val instance = Delete()
  }
  final case class GetAll private (val index: Int = 5) extends CRUDOperation
  object GetAll {
    val instance = GetAll()
  }

  def selectOperation(selection: String): Option[CRUDOperation] = Try(Integer.parseInt(selection)).toOption match {
    case None => None
    case Some(index) =>
      index match {
        case Create.instance.index => Some(Create.instance)
        case Read.instance.index   => Some(Read.instance)
        case Update.instance.index => Some(Update.instance)
        case Delete.instance.index => Some(Delete.instance)
        case GetAll.instance.index => Some(GetAll.instance)
        case _                     => None
      }
  }
}
