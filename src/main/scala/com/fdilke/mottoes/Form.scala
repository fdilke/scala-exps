package com.fdilke.mottoes

sealed trait Form {

}

final class BasicForm(index: Int) extends Form {
  override def toString : String =
    ('A'.toInt + index).toChar.toString
}
