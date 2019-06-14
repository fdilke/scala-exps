package com.fdilke.varsymm

object DihedralGroup {
  def apply(two_n: Int): Group[Int] = {
    if (two_n <= 0)
      throw new IllegalArgumentException("order should be +ve")
    else if (two_n % 2 != 0)
      throw new IllegalArgumentException("order should be even")

    new Group[Int] { }
  }
}
