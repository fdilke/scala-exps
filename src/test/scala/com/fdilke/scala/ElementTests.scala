package com.fdilke.scala

import org.junit.Test
import org.junit.Assert._
import Element.elem

/**
 * Author: fdilke
 */

class ElementTests {
  @Test
  def lineElement() {
    val lineElement = elem("a line")
    assertEquals(1, lineElement height)
    assertEquals(6, lineElement width)
    assertEquals("a line", lineElement toString)
  }

  @Test
  def uniformElement() {
    val uniformElement = elem('@', 3, 4)
    assertEquals(4, uniformElement height)
    assertEquals(3, uniformElement width)
    assertEquals("@@@\n@@@\n@@@\n@@@", uniformElement toString)
  }

  @Test
  def above() {
    val composite = elem('#', 2, 3) above elem("hehhey")
    assertEquals(4, composite height)
    assertEquals(6, composite width)
    assertEquals("  ##  \n  ##  \n  ##  \nhehhey", composite toString)
  }

  @Test
  def beside() {
    val composite = elem('#', 2, 3) beside elem('x', 4, 5)
    assertEquals(5, composite height)
    assertEquals(6, composite width)
    assertEquals("  xxxx\n##xxxx\n##xxxx\n##xxxx\n  xxxx", composite toString)
  }
}

