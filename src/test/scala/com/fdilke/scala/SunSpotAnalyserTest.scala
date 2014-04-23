package com.fdilke.scala

import org.junit.Test
import org.junit.Assert._

/**
 * Author: fdilke
 */

class SunSpotAnalyserTest {
  @Test def handlesRawDataForFixedSizeGrid() {
    val inputs = "5 3 1 2 0 4 1 1 3 2 2 3 2 4 3 0 2 3 3 2 1 0 2 4 3"
    val expectedOutputs = "13 15 11 9 7 18 22 20 18 14 12 18 22 23 17 8 15 23 26 19 3 8 14 17 12"

    assertEquals(expectedOutputs, SunSpotAnalyser.analyseFixedSizeGrid(inputs))
  }

  @Test def handlesRawDataForVariableSizeGrid() {
    val inputs = "3 4 2 3 2 2 1 3 2 1"
    val expectedOutputs = "10 14 8 15 20 11 9 11 6"

    assertEquals(expectedOutputs, SunSpotAnalyser.analyseVariableSizeGrid(inputs))
  }

  @Test def handlesHighScores1() {
    val inputs = "1 5 5 3 1 2 0 4 1 1 3 2 2 3 2 4 3 0 2 3 3 2 1 0 2 4 3"
    val expectedOutputs = "(3,3 score:26)"
    assertEquals(expectedOutputs, SunSpotAnalyser.analyseVariableHighScores(inputs))
  }

  @Test def handlesHighScores2() {
    val inputs = "3 4 2 3 2 1 4 4 2 0 3 4 1 1 2 3 4 4"
    val expectedOutputs = "(1,2 score:27)(1,1 score:25)(2,2 score:23)"
    assertEquals(expectedOutputs, SunSpotAnalyser.analyseVariableHighScores(inputs))
  }
}

