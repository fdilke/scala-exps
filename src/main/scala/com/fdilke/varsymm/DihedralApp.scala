package com.fdilke.varsymm

import java.awt.{Color, Graphics, GraphicsDevice, GraphicsEnvironment}

import GroupSugar._
import javax.swing.WindowConstants
import javax.swing.JFrame
import javax.swing.JPanel

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

object AltDihedralApp extends App {
  new JFrame { frame =>
    setContentPane(new JPanel {
      override def paintComponent(g: Graphics): Unit = {
        g.setColor(Color.PINK)
        g.fillRect(20, 20, 100, 200) // Draw on g here e.g.
      }
    })
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
    setUndecorated(true)

    pack()

    val device: GraphicsDevice =
      GraphicsEnvironment.getLocalGraphicsEnvironment.getScreenDevices()(0)

    device.setFullScreenWindow(frame)
    setVisible(true)
 }
}
