package com.fdilke.varsymm

import java.awt.{Color, Graphics, GraphicsDevice, GraphicsEnvironment}

import javax.swing.{JFrame, JPanel, Timer, WindowConstants}

import scala.util.Random

class BlotPanel(group: Group[DihedralSymmetry]) extends JPanel {
  val random: Random = scala.util.Random
//  val shapes: Seq[Drawable] = randomCircles()

    new Timer(
      2000,
      { _ =>
        repaint()
      }
    ).start()

  private val generator: () => Int =
    { () => Math.abs(Random.nextInt()) }

  private val zzFactory =
    new AlternatingZigZagFactory(
      group.subgroupLattice,
      generator
    )
  private var zigzag: ZigZag[group.AnnotatedSubgroup, group.AnnotatedSubgroupInclusion] =
    zzFactory.initialZag

  private val shapes: Seq[Drawable] = randomCircles()
  private var unitMeasure: Double = 0
  private val INCREMENT = 0.05

  override def paintComponent(gfx: Graphics) {
    gfx.clearRect(0, 0, getWidth, getHeight)

    for {
      shape: Drawable <- shapes
      sym: DihedralSymmetry <- zigzag.next.toSubgroup.elements
    } {
      shape.through(
        Matrix22.convexity(Matrix22.identity, unitMeasure, sym.toMatrix)
      ).draw(gfx, getWidth, getHeight)
    }

    unitMeasure += INCREMENT
    if (unitMeasure > 1.0) {
      zigzag = zzFactory(zigzag)
      unitMeasure = 0
    }
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

  def through(
    matrix: Matrix22
  ): Drawable
}

object ScaleFactor {
  // multiplicative fudge factor (compensating for screen's aspect ratio)
  // to give circular ellipses
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
      (radius * height).toInt
    )
  }

  override def through(
    matrix: Matrix22
  ): Drawable = {
    val (newX, newY) : (Double, Double) =
      (x, y) *: matrix
    new DrawableCircle(newX, newY, radius, color)
  }
}

object DihedralApp extends App {
  val device: GraphicsDevice =
    GraphicsEnvironment.getLocalGraphicsEnvironment.getScreenDevices()(0)
  val group = DihedralGroup(8)

  new JFrame { frame =>
    setContentPane(new BlotPanel(group))
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
    setUndecorated(true)

    pack()

    device.setFullScreenWindow(frame)
    setVisible(true)
 }
}

object FunWithMarkTableApp extends App {
  for { n <- 1 to 100 } {
    val group = Permutation.group(n)
    val marks = group.markTable
    println(s"S($n) -> ${marks.blocks.size}")
  }
}
