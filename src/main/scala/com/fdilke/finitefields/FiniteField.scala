package com.fdilke.finitefields

import scala.tools.nsc.interpreter.InputStream
import scala.language.postfixOps

case class NontrivialPrimePower(p: Int, n: Int) {
  val power = Seq.fill(n)(p)./:(1)(_ * _)
}

object NontrivialPrimePower {
  def unapply(n: Int): Option[(Int, Int)] =
    if (n < 1)
      None
    else (2 to n).find { d => 0 == n % d }.flatMap { p => {
      def logP: Int => Option[Int] = {
        case 1 => Some(0)
        case pr if pr % p == 0 => logP(pr/p).map { _ + 1 }
        case _ => None
      }
      logP(n) map { r => p -> r }
    }}
}

object FiniteField {

  private val conwayRegex = "\\[(.*),(.*),\\[(.*)\\]\\],".r

  private val stream : InputStream =
    getClass.getResourceAsStream("CPimport.txt")

  private val lines =
    scala.io.Source.fromInputStream(
      stream
    ) getLines

  private val allFields =
    lines.foldLeft(
        Map[NontrivialPrimePower, FiniteField]()
    ) {
      (map, line) =>
        if ((line startsWith "[") && (line endsWith "],")) {
          val conwayRegex(pp, nn, coefftsCSV) = line
          val pn = NontrivialPrimePower(pp toInt, nn toInt)
          val coeffts = coefftsCSV.split(",").map {
            _.toInt
          }
          map + (pn -> new FiniteField(pn, coeffts))
        }
        else map
    }

  def GF: Int => FiniteField = {
    case NontrivialPrimePower(p, n) =>
      allFields(NontrivialPrimePower(p, n))
    case _ =>
      throw new IllegalArgumentException
  }
}

class FiniteField(
  pn: NontrivialPrimePower,
  coeffts: Seq[Int]
  ) extends Traversable[Element] {
  def O = Element(0)
  def I = Element(1)
  private val elements = 0 until pn.power
  lazy val polyTable: Seq[Seq[Int]] =
    elements map toPoly

  lazy val additionTable: Seq[Seq[Element]] =
    elements.map { i =>
      val polyI = polyTable(i)
      elements.map { j =>
        val polyJ = polyTable(j)
        Element(fromPoly(
          (polyI zip polyJ) map { case (a, b) => (a + b) % pn.p }
        ))
      }
    }

  lazy val scalarMultiplicationTable: Seq[Seq[Element]] =
    (0 until pn.p) map { i =>
      elements.map { j =>
        val polyJ = polyTable(j)
        Element(fromPoly(
          polyJ map { k => (k * i) % pn.p }
        ))
      }
    }

  lazy val minusTable: Seq[Element] =
    scalarMultiplicationTable(pn.p - 1)

  lazy val x_n = fromPoly(coeffts.init.map { _ * (pn.p - 1) % pn.p })

  lazy val shiftTable: Seq[Element] =
    elements.map { i =>
      polyTable(i) match {
        case tail :+ head =>
          additionTable(
            fromPoly(0 +: tail)
          )(
            scalarMultiplicationTable(head)(x_n).index
          )
      }
    }

  lazy val multiplicationTable: Seq[Seq[Element]] =
    elements.map { i =>
      val polyI = polyTable(i)
      elements.map { j =>
        (Seq.iterate(j, pn.n) { j => shiftTable(j).index } zip polyI map {
          case (r, s) => scalarMultiplicationTable(s)(r)
        })./:(O)(_ + _)
      }
    }

  lazy val inversionTable: Seq[Element] =
    elements.map { i =>
      Element(elements.find {
        multiplicationTable(i)(_) == I
      } getOrElse 0)
    }

  override def foreach[U](f: Element => U) {
    elements foreach { n => f(Element(n))}
  }

  implicit class RichElement(e: Element) { element =>
    def +(f: Element): Element =
      additionTable(e.index)(f.index)

    def -(f: Element): Element =
      element + (-f)

    def *(f: Element): Element =
      multiplicationTable(e.index)(f.index)

    def /(f: Element): Element =
      element * ~f

    def unary_- : Element =
      minusTable(e.index)

    def unary_~ : Element =
      inversionTable(e.index)
  }

  def toPoly(m: Int): Seq[Int] =
    Seq.iterate(m, pn.n) { _ / pn.p } map { _ % pn.p }

  def fromPoly(poly: Seq[Int]): Int =
    poly.:\(0) {
      (a, b) => a + pn.p * b
    }
}

case class Element(index: Int) extends AnyVal

