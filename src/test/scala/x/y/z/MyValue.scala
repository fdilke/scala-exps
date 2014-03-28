package x.y.z

//import RandomAccessSeq._

/**
 * Author: fdilke
 */

object MyValue {
  def apply(x : Int) = new MyValue(x)

//  def x() : RandomAccessSeq[Char]
//
//  implicit def stringWrapper(s : String) =
//    new RandomAccessSeq[Char] {
//      def length = s.length
//      def apply(i : Int) = s.charAt(i)
//    }
}

class MyValue(x : Int) {
}


//implicit def intToMyValue (x : Int) : MyValue = MyValue(x)