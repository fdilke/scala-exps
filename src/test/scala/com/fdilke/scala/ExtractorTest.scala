package com.fdilke.scala

import org.scalatest.{Matchers, FunSpec}
import Matchers._

class ExtractorTest extends FunSpec {
  describe("extractors") {
    it("can be invoked from regexes") {
      val propertyRegex = "(.*?)=(.*)".r
      val property = "shoeSize=5"

      val propertyRegex(key, value) = property
      key shouldBe "shoeSize"
      value shouldBe "5"
    }

    it("can be invoked from fancier regexes") {
      val fileRegex = ".*/(\\w*)\\.jar".r
      val fileRegex(prefix) = "path/to/a/snapshot.jar"

      prefix shouldBe "snapshot"
    }

    it("can be invoked from even fancier regexes") {
      val fileRegex = "file:[/\\w\\-]*?(scala-[\\.\\w]+)[/\\w\\-]*?".r
      val fileRegex(prefix) = getClass.getProtectionDomain.getCodeSource.getLocation.toString

      prefix shouldBe "scala-2.12"
    }

    it("can be invoked from yet fancier regexes") {
      val url = "file:/opt/springer/bw-streamer/bw-streamer-0.170/lib/bw-streamer-no-dependencies-0.170.jar"
      val fileRegex = "file:[/\\.\\w\\-]*?bw-streamer-no-dependencies-([\\.\\w]+)[/\\w\\-].jar".r
      val fileRegex(prefix) = url

      prefix shouldBe "0.17"
    }

    it("can be invoked from yet fancier regexes with multiple groups") {
      val nquadRegex = "(.*) (.*) (.*) \\.".r

      {
        val nquad = "<http://lod.springer.com/data/confseries/icycsee> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://lod.springer.com/data/ontology/class/ConferenceSeries> ."
        val nquadRegex(subj, pred, obj) = nquad

        subj shouldBe "<http://lod.springer.com/data/confseries/icycsee>"
        pred shouldBe "<http://www.w3.org/1999/02/22-rdf-syntax-ns#type>"
        obj shouldBe "<http://lod.springer.com/data/ontology/class/ConferenceSeries>"
      }

      {
        val nquad = "<http://lod.springer.com/data/book/978-1-4020-5331-3> <http://lod.springer.com/data/ontology/property/EISBN> \"978-1-4020-5331-3\"^^<http://www.w3.org/2000/01/rdf-schema#literal> ."
        val nquadRegex(subj, pred, obj) = nquad

        subj shouldBe "<http://lod.springer.com/data/book/978-1-4020-5331-3>"
        pred shouldBe "<http://lod.springer.com/data/ontology/property/EISBN>"
        obj shouldBe "\"978-1-4020-5331-3\"^^<http://www.w3.org/2000/01/rdf-schema#literal>"
      }
    }

    it("can extract everything after a hash or a slash") {
      val regex = ".*[\\#\\/](.*)".r

      for { (input, output) <- Map(
        "test/something/xx" -> "xx",
        "test/xx" -> "xx",
        "test#xx" -> "xx",
        "test#something#xx" -> "xx",
        "test/some#thing#xx" -> "xx",
        "http://www.w3.org/1999/02/22-rdf-syntax-ns#type" -> "type",
        "http://ns.nature.com/terms/id" -> "id"
      )} {
        val regex(expectedSuffix) = input
        output shouldBe expectedSuffix
      }
    }

    it("can perform a boolean test") {
      object Even {
        def unapply(n: Int):Boolean =
          n % 2 == 0
      }
      intercept[MatchError] {
        3 match {
          case Even() =>
        }
      }
      2 match { case Even() => }
    }

    it("can extract a single value") {
      object Twice {
        def unapply(n: Int): Option[Int] = if (n % 2 == 0) Some(n / 2) else None
      }

      val Twice(m) = 6
      m shouldBe 3

      intercept[MatchError] {
        val Twice(q) = 5
      }
    }

    it("can extract several values") {
      object PrimePower {
        def unapply(n : Int) : Option[(Int, Int)] =
          if (n == 8)
            Some(2, 3)
        else None  // simplistic - only handles case 2^3
      }

      val PrimePower(p, n) = 8
      p shouldBe 2
      n shouldBe 3
    }

    it("can extract a variable number of values") {
      object PossibleTriplet {
        def unapplySeq(s: List[Int]): Option[Seq[Int]] =
          if (s.size == 3)
            Some(s)
          else
            None
      }

      val PossibleTriplet(a, b, c) = List(4, 5, 6)
      (a, b, c) shouldBe (4, 5, 6)
    }

    it("can extract a fixed number of values, followed by a variable number") {
      object PossibleTriplet {
        def unapplySeq(s: List[Int]) : Option[(Int, Seq[Int])] =
          if (s.size == 3)
            Some((s(0), s.slice(1, s.size)))
          else
            None
      }

      val PossibleTriplet(a,b,c) = List(4, 5, 6)
      (a, b, c) shouldBe (4, 5, 6)
    }

    it("can implicitly extract members from a case class with generics") {
      case class Pair[T <: Comparable[T]](left: T, right: T)
      Pair("a", "b") match {
        case Pair(x, y) =>
          x shouldBe "a"
          y shouldBe "b"
        case _ =>
          fail("can't apply extractor")
      }
    }
  }
}
