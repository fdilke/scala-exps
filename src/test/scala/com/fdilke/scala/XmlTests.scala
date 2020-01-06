package com.fdilke.scala

import org.scalatest.matchers.should.Matchers._
import org.scalatest.funspec.AnyFunSpec

import scala.collection.mutable.ArrayBuffer
import scala.xml._

class XmlTests extends AnyFunSpec {
  val xml: Elem =
    <Animals>
      <Animal type="Cat">
        <name>Winston</name>
      </Animal>
      <Animal type="Dog">
        <name>Monty</name>
      </Animal>
    </Animals>

  describe("XML literals") {
    it("have type Element/Text/Node/NodeSeq") {
      assert(xml.isInstanceOf[Elem])
      assert((xml \ "Animal").isInstanceOf[NodeSeq])
      assert((xml \\ "name").isInstanceOf[NodeSeq])
    }

    it("can be traversed") {
      (xml \\ "name").size shouldBe 2
      (xml \\ "name").head shouldBe <name>Winston</name>

      assert(<n>W</n>.head.child.head.isInstanceOf[Text])
      val monty = (xml \\ "Animal")(1)
      monty \ "@type" shouldBe Group(Text("Dog"))
    }

    it("can be loaded from a file") {
      val fileXml = XML.loadFile("src/test/resources/sample.xml")
      fileXml \\ "Animal" should (have size 2)
    }

    it("can include code") {
      <x>
        {3 + 4}
      </x>.child(1).text.trim.toInt shouldBe 7
    }

    it("can be used in a 'for' comprehension to skip whitespace") {
      xml match {
        case <Animals>{animals @ _*}</Animals> =>
          animals should (have size 5)
          (
            for(animal @ <Animal>{_*}</Animal> <- animals)
              yield animal
          ) should ( have size 2 )
      }
    }
  }
}
