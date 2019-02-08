package com.fdilke.mottoes

import java.util.concurrent.atomic.AtomicInteger

object DoEnumerateForms extends App {
  val count = new AtomicInteger
  for {
    length <- 1 to 4
    _ = println(s"### Length $length ###")
    form <- EnumerateForms(length) if form.isUniquelySolvable
//    _ = println(form)
//    _ = println(form.isUniquelySolvable)
  } {
    println(count.incrementAndGet() + "] " + form)
  }
//    println("###")
}
