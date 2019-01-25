package com.fdilke.mottoes

object DoEnumerateForms extends App {
  for {
    length <- 1 to 9
    _ = println(s"### Length $length ###")
    form <- EnumerateForms(length)
    _ = println(form)
    _ = println(form.isUniquelySolvable)
  }
    println("###")
}
