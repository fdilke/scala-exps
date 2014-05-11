package com.fdilke.scala

import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers
import ShouldMatchers._
import scala.xml._

// User: Felix Date: 08/05/2014 Time: 18:03

class XmlTests extends FunSpec {
  val xml = <Animals>
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
      (xml \\ "name")(0) shouldBe <name>Winston</name>

      assert(<n>W</n>(0).child(0).isInstanceOf[Text])
      val monty = (xml \\ "Animal")(1)
      monty \ "@type" shouldBe Group(Text("Dog"))
    }

    it("can be loaded from a file") {
      val fileXml = XML.loadFile("src/test/resources/sample.xml")
      fileXml \\ "Animal" should have ('size (2))
    }

    it("can include code") {
      <x>{ 3 + 4 }</x>.child(0).text.toInt shouldBe 7
    }

    it("can be used in a 'for' copmrehension to skip whitespace") {
      xml match {
        case <Animals>{animals @ _*}</Animals> =>
          animals should have (
            'class (classOf[NodeBuffer]),
            'size (5)
          )
          ( for(animal @ <Animal>{_*}</Animal> <- animals)
            yield animal ) should have (
              'class (classOf[Array[Node]]),
              'size (2)
          )
      }
    }
  }
}
