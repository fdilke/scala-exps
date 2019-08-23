package com.fdilke.varsymm

import java.awt.{Color, Graphics, GraphicsDevice, GraphicsEnvironment}

import javax.swing.{JFrame, JPanel, Timer}

import scala.util.Random

class BlotPanel extends JPanel {
  val random: Random = scala.util.Random
//  val shapes: Seq[Drawable] = randomCircles()

    new Timer(
      2000,
      { _ =>
        repaint()
      }
    ).start()

  override def paintComponent(gfx: Graphics) {
    gfx.clearRect(0, 0, getWidth, getHeight)
    for { shape <- randomCircles() }
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
}

object DihedralApp extends App {
  for { n <- 1 to 100 } {
    val group = Permutation.group(n)
    val marks = group.markTable
    println(s"S($n) -> ${marks.blocks.size}")
  }
}
