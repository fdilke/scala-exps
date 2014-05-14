package com.fdilke.util
import scala.io.Source
import Source._

// User: Felix Date: 14/05/2014 Time: 21:28

object ReadProperties {
  private val Property = "(.*?)=(.*)".r
  private val Comment = "\\#.*".r
  private val Whitespace = "\\s*".r

  def apply(filename: String): Map[String, String] =
    Map((for (line <- fromFile(filename).getLines().toList ;
      property = line match {
        case Property(key, value) => Some(key -> value)
        case Comment() => None
        case Whitespace() => None
        case _ => throw new IllegalArgumentException
      } if property.isDefined)
      yield property.get
     ) :_*)
}
