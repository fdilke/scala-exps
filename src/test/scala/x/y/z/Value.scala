package x.y.z

/**
 * Author: fdilke
 */
object Value {
  val ZERO = new Value(Set.empty, Set.empty)
  val ONE = new Value(Set(ZERO), Set.empty)
  val TWO = new Value(Set(ONE), Set.empty)
  val STAR = new Value(Set(ZERO), Set(ZERO))
  val UP = new Value(Set(ZERO), Set(STAR))
  val DOWN = -UP
}

class Value(val left : Set[Value], val right : Set[Value]) {
  def <=(H : Value) : Boolean = {
    (left forall (GL => ! (H <= GL))) &&
    (H.right forall (HR => ! (HR <= this)))
  }

  override def equals(that : Any) = that match {
    case h : Value => this <= h && h <= this
    case _ => false
  }

  def unary_-() : Value = new Value(right map (-_), left map (-_))

  def +(H : Value) : Value = new Value(
    (left map (_ + H)) union (H.left map (this + _)),
    (right map (_ + H)) union (H.right map (this + _))
  )

  def -(H : Value) = this + (-H)
}
