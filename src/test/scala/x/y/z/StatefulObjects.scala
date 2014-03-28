package x.y.z

import org.scalamock.scalatest.MockFactory
import org.scalatest.FunSuite

/**
 * Author: fdilke
 */

class Temperature {
  var celsius : Double = _

  def fahrenheit = (celsius + 40) * (9.0/5.0) - 40
  def fahrenheit_= (x : Double) {
    celsius = (x + 40) * (5.0/9.0) - 40
  }
}

abstract class Simulation {
  type Action = () => Unit

  case class WorkItem(time : Int, action : Action)

  private var curTime = 0

  def currentTime: Int = curTime

  private var agenda : List[WorkItem] = List()

  private def insert(ag : List[WorkItem], item : WorkItem) : List[WorkItem] =
    if (ag.isEmpty || item.time < ag.head.time)
      item :: ag
    else ag.head :: insert(ag.tail, item)

  def afterDelay(delay : Int)(block : =>Unit) {
    val item = WorkItem(currentTime + delay, () => block)
    agenda = insert(agenda, item)
  }

  private def next() {
    agenda match {
      case item :: rest =>
        agenda = rest
        curTime = item.time
        item.action()
      case _ => throw new UnsupportedOperationException
    }
  }

  private def run() {
    afterDelay(0) {
      println("Simulation started! time = " + currentTime)
    }
    while (!agenda.isEmpty)
      next()
  }
}

class StatefulObjectsTest extends FunSuite with MockFactory {
  test("setting and getting values on Temperature") {
    val t = new Temperature
    t.celsius = 0
    assert(t.fahrenheit === 32)
    t.fahrenheit = 212
    assert(t.celsius === 100)
    t.celsius = -40
    assert(t.fahrenheit === t.celsius)
  }
}
