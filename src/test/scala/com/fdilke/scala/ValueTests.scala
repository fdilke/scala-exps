package com.fdilke.scala

import org.junit.Test
import org.junit.Assert._

/**
 * Author: fdilke
 */
class ValueTests {
  @Test
  def testValuesLessEq() {
    assertTrue(ZERO <= ZERO)
    assertTrue(STAR <= STAR)
    assertFalse(ZERO <= STAR)
    assertFalse(STAR <= ZERO)
    assertTrue(ZERO <= UP)
    assertFalse(STAR <= UP)
  }

  @Test
  def testValuesEq() {
    val altZero = new Value(Set.empty, Set.empty)
    assertTrue(altZero == ZERO)
    assertEquals(altZero, ZERO)

    assertEquals(ZERO,ZERO)
    assertEquals(STAR, STAR)
    assertEquals(UP, UP)

    assertNotEquals(ZERO, STAR)
    assertNotEquals(ZERO, UP)
    assertNotEquals(UP, STAR)
  }

  @Test
  def testUnaryMinus() {
    assertEquals(-ZERO, ZERO)
    assertEquals(-STAR, STAR)
    assertEquals(-DOWN, UP)
  }

  @Test
  def testAddition() {
    assertEquals(ZERO + ZERO, ZERO)
    assertEquals(ZERO + ONE, ONE)
    assertEquals(ONE + ONE, TWO)
    assertEquals(STAR + STAR, ZERO)
    assertEquals(UP + DOWN, ZERO)
  }

  @Test
  def testSubtraction() {
    assertEquals(ZERO - ZERO, ZERO)
    assertEquals(ZERO - ONE, -ONE)
    assertEquals(ONE - ONE, ZERO)
    assertEquals(TWO - ONE, ONE)
    assertEquals(STAR - STAR, ZERO)
    assertEquals(ZERO - UP, DOWN)
    assertEquals(ZERO - DOWN, UP)
  }
}
