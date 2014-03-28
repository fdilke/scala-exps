package x.y.z

import Element.elem

/**
 * Author: fdilke
 */

object Element {
  private class ArrayElement(val contents : Array[String]) extends Element {}

  private class LineElement(line : String) extends ArrayElement(Array(line)) {}

  private class UniformElement(
                                ch : Char,
                                override val width : Int,
                                override val height : Int
                                ) extends Element {
    private val line = (ch toString) * width
    def contents: Array[String] = Array.fill(height) { line }
  }

  def elem(contents : Array[String]) : Element =
    new ArrayElement(contents)

  def elem(line : String) : Element =
    new LineElement(line)

  def elem(ch : Char, width : Int, height : Int) : Element =
    new UniformElement(ch, width, height)
}

abstract class Element {
  def contents : Array[String]
  def height = contents.length
  def width = if (height == 0) 0 else contents(0).length

  def above(that : Element) : Element =  {
    val this1 = this widen that.width
    val that1 = that widen this.width
    elem(this1.contents ++ that1.contents)
  }

  def beside(that : Element) : Element = {
    val this1 = this heighten that.height
    val that1 = that heighten this.height
    elem (
      for ( (line1, line2) <- this1.contents zip that1.contents)
        yield line1 + line2
    )
  }

  def widen(w : Int) : Element =
    if (w <= width) this
    else {
      val left = elem(' ', (w - width) / 2, height)
      val right = elem(' ', w - width - left.width, height)
      left beside this beside right
    }

  def heighten(h : Int) : Element =
    if (h <= height) this
    else {
      val top = elem(' ', width, (h - height)/2)
      val bottom = elem(' ', width, h - height - top.height)
      top above this above bottom
    }

  override def toString = contents mkString "\n"
}

object Spiral {
  val space = elem(" ")
  val corner = elem("+")

  def spiral(nEdges : Int, direction : Int) : Element = {
    if (nEdges == 1) {
      elem("+")
    } else {
      val sp = spiral(nEdges - 1, (direction + 3)%4)
      def verticalBar = elem('|', 1, sp.height)
      def horizontalBar = elem('-', sp.width, 1)
      direction match {
        case 0 => (corner beside horizontalBar) above (sp beside space)
        case 1 => (sp above space) beside (corner above verticalBar)
        case 2 => (space beside sp) above (horizontalBar beside corner)
        case _ => (verticalBar above corner) beside (space above sp)
      }
    }
  }

  def main(args : Array[String]) {
    // val nSides = args(0).toInt
    val nSides = 17

    println(spiral(nSides, 0))
  }
}





