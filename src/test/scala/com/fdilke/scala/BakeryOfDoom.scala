package com.fdilke.scala

object BakeryOfDoom {

  trait BaseWidget
  trait WidgetExtensions extends BaseWidget {

  }
  object Widget extends
    BaseWidget with WidgetExtensions

  object Doodad extends DoodadExtensions
  trait DoodadExtensions {
    self: Doodad.type =>
  }
}