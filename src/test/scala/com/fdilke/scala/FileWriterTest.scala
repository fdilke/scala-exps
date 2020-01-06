package com.fdilke.scala

import java.io.{BufferedReader, File, FileReader, FileWriter}

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers._

import scala.language.implicitConversions

class FileWriterTest extends AnyFunSpec {
  describe("A FileWriter") {
    it("can write to a file") {
      val newFile = new File("/tmp/my-test-file")
      val writer = new FileWriter(newFile)
      val myStrings = Seq(
        "Hello Yasu",
        "This is a message",
        "Just for you"
      )
      myStrings foreach { text =>
        writer.write(text + "\n")
      }
      writer.flush()

      val reader = new BufferedReader(new FileReader(newFile))
      reader.lines().toArray.toSeq shouldBe myStrings
    }
  }
}
