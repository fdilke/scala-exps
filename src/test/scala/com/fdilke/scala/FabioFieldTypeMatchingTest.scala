package com.fdilke.scala

import org.scalatest.FlatSpec
import org.scalatest.Matchers._

sealed trait DbType[T]
case object StringDbType extends DbType[String]
case object IntDbType extends DbType[Int]

trait FieldAdapter {
  type U
  def dbType: DbType[U]
}

class FabioFieldTypeMatchingTest extends FlatSpec {
  "Pattern matching" should "work for a generalised ADT" in {
    val x: FieldAdapter = new FieldAdapter {
      override type U = String
      override def dbType: DbType[String] = StringDbType
    }

    val dbTypeName = (x.dbType : DbType[_]) match {
      case StringDbType => "varchar"
      case IntDbType => "integer"
    }

    dbTypeName shouldBe "varchar"
  }
}