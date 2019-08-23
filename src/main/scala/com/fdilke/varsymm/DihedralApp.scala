package com.fdilke.varsymm

import java.awt.{Color, Graphics, GraphicsDevice, GraphicsEnvironment}

import javax.swing.{JFrame, JPanel}

import scala.util.Random

object DihedralApp extends App {
  implicit val group = DihedralGroup(12)
  println("Listing elements of order 6")
  for {
    x <- group.elements if group.orderOf(x) == 2
  } {
    val hasSqRoot = group.elements.exists { y =>
      y * y == x
    }
    val hasCubeRoot = group.elements.exists { y =>
      y * y * y == x
    }
    println("element of order 2: " + x +
      (if (hasSqRoot) "²" else "") +
      (if (hasCubeRoot) "³" else "")
    )
  }
}

class BlotPanel extends JPanel {
  val random: Random = scala.util.Random
  val shapes: Seq[Drawable] = randomCircles()

  override def paintComponent(gfx: Graphics) {
    for { shape <- shapes }
      shape.draw(gfx, getWidth, getHeight)
  }

  def randomCircles(): Seq[Drawable] = {
    (1 to 7) map { n =>
      val radius = random.nextDouble()/2
      val leeway = 2 * (1 - radius)
      val x = radius - 1 + leeway * random.nextDouble()
      val y = radius - 1 + leeway * random.nextDouble()
      val color = new Color(
        random.nextInt(256),
        random.nextInt(256),
        random.nextInt(256)
      )
      new DrawableCircle(x, y, radius, color)
    }
  }
}

trait Drawable {
  def draw(gfx: Graphics, width: Int, height: Int)
}

object ScaleFactor { // multiplicative fudge factor to give circular ellipses
  val VALUE = 0.62
}

class DrawableCircle(
  x : Double,
  y : Double,
  radius : Double,
  color: Color
) extends Drawable {
  override def draw(gfx: Graphics, width: Int, height: Int): Unit = {
    gfx.setColor(color)
    gfx.fillOval(
      ((1 + (x - radius) * ScaleFactor.VALUE) * width/2).toInt,
      ((1 + y - radius) * height/2).toInt,
      (radius * width * ScaleFactor.VALUE).toInt,
      (radius * height).toInt,
    )
  }
}

object AltDihedralApp extends App {
  val device: GraphicsDevice =
    GraphicsEnvironment.getLocalGraphicsEnvironment.getScreenDevices()(0)

  new JFrame { frame =>
    setContentPane(new BlotPanel)
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
    setUndecorated(true)

    pack()

    device.setFullScreenWindow(frame)
    setSize(300, 400)
    setVisible(true)
 }

  // GraphicsDevice.getLocalGraphicsEnvironment().getScreenDevices()
//  device.getDefaultConfiguration.getImageCapabilities.
//  device.getDefaultConfiguration.getBounds
//  Screen
//  javafx.stage.Screen.getPrimary().getDpi()
}

