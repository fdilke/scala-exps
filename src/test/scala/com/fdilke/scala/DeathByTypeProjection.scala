package com.fdilke.scala

import scala.language.existentials

case class Pair[L, R](left: L, right: R)

object DeathByTypeProjection {
	trait ToposLite { Ɛ =>
		type ~[T]
		type *[T <: ~[T], S] <: (() => T) with ~[S]

		class MonoidsLite extends ToposLite {
			trait ~~[T <: Ɛ.~[T], TT <: ~~[T, TT]] {
				type BASE = T
				type STAR[TTT, S] <: (() => TTT) with ~[S]
			}

			type ~[TT] = TT forSome {
				type T <: Ɛ.~[T]
				type TT <: ~~[T, TT]
			}

			override type *[TT <: ~[TT], S] = TT#STAR[TT, S]

			trait Widget[T <: Ɛ.~[T], TT <: ~~[T, TT]]

			// def widget[T <: Ɛ.~[T], TT <: ~~[T]]() =
			// 	new Widget[Ɛ.*[T, X], *[TT, Y]]

			// Need a class extending ~~...
		}
	}
}

object PossibleSolutionByTypeProjectionWithBiproducts {
	trait ToposLite { Ɛ =>
		type ~
		type x[S <: ~, T <: ~] <: (S, T) with ~

		class ActionsLite extends ToposLite {
			trait ~~[T <: Ɛ.~] {
				type BASE = T
			}

			type ~ = ~~[T] forSome { type T <: Ɛ.~ }

			// override type x[SS <: ~, TT <: ~] = (SS, TT) with ~~[Ɛ.x[SS#BASE, TT#BASE]]
			override type x[SS <: ~, TT <: ~] = Helper[SS, TT]#BIP

			class Widget[T <: Ɛ.~, TT <: ~~[T]]

			class Helper[SS <: ~, TT <: ~] {
				type EX = Ɛ.x[SS#BASE, TT#BASE]
				type BIP = (SS, TT) with ~~[EX]

				def check(bip: BIP): SS x TT = bip
				def check2(ssXtt: SS x TT): BIP = ssXtt

				type WIDGET = Widget[EX, BIP]

				def makeWidget = new Widget[EX, BIP]
			}

			// def widget[S <: Ɛ.~, SS <: ~~[S], T <: Ɛ.~, TT <: ~~[T]]() =
			// 	new Widget[Ɛ.x[SS#BASE, TT#BASE], x[SS, TT]]
			def widget[S <: Ɛ.~, SS <: ~~[S], T <: Ɛ.~, TT <: ~~[T]]() =
				new Helper[SS, TT].makeWidget
		}
	}
}

/*

x[S, T] = (S, T) with ~
and the bright idea was to tighten this up to:
x[SS, TT] = (SS, TT) with ~~[Ɛ.x[SS#BASE, TT#BASE]]
because then... Why did that seem like a good idea?

Because: when you multiply an ss = Widget[S, SS] and a tt = Widget[T, TT]
the result should be a Widget[S x T, xx] which is an S x T appropriately dressed up
So we need a type xx which is an appropriate extension of Ɛ.x[S x T]

Constraint is:
	ActionStar[Z, ZZ] x ActionStar[A, AA] has to be an ActionStar[Ɛ.x[ZZ#BASE, AA#BASE], ZZ x AA]

so: ZZ x AA = (ZZ, AA) with ~~[Ɛ.x[ZZ#BASE, AA#BASE]]
Why did we tighten? It used to be just:
    ZZ x AA = (ZZ, AA) with ~~[...]
	but then ZZ x AA and Z x A weren't properly interchangeable. 
	We needed to know that the lhs wrapped the rhs - not just by convention.

So under 'tightening' we have:
 override type x[SS <: ELEMENT, TT <: ELEMENT] = (SS, TT) with Ɛ.ElementWrapper[Ɛ.x[SS#BASE, TT#BASE]]
 RightActionStar[A <: Ɛ.ELEMENT, AA <: Ɛ.ElementWrapper[A]]
 and now we want to construct a
 RightActionStar[Ɛ.x[ZZ#BASE, AA#BASE], ZZ x AA]

 and we can't. 

 The following type constraints need to be satisfied:
 Ɛ.x[ZZ#BASE, AA#BASE] <: Ɛ.ELEMENT
 ZZ x AA <: Ɛ.ElementWrapper[Ɛ.x[ZZ#BASE, AA#BASE]]

 Looks like they are to me. What's the problem????
*/

object CompilerBeingSillyAboutUnit {
	trait ToposLite { Ɛ =>
		type ~
		type UNIT <: ~

		class ActionsLite extends ToposLite {
			trait ~~[T <: Ɛ.~, TT <: ~~[T, TT]] {
				val element: T
			}

	      	override type ~ = TT forSome {
		        // type T <: Ɛ.~
		        // type TT <: ~~[T, TT]
		        type TT <: ~~[_ <: Ɛ.~, TT]
		     }

			class VanillaWrapper[A <: Ɛ.~](a: A) extends
		    	~~[A, VanillaWrapper[A]] {
		        override val element = a
		  	}

			override type UNIT = VanillaWrapper[Ɛ.UNIT]
		}
	}
}

object CompilerBeingSillyAboutBiproducts {
	trait ToposLite { Ɛ =>
		type ~
		type x[S <: ~, T <: ~] <: (S, T) with ~

		class ActionsLite extends ToposLite {
			trait ~~[T <: Ɛ.~, TT <: ~~[T, TT]] {
				val element: T

				type PREBIPRODUCT[SS <: ~] = SS#POSTBIPRODUCT[T, TT] 
				type POSTBIPRODUCT[U <: Ɛ.~, UU <: ~~[T, TT]] = H forSome {
					type H <: ~~[Ɛ.x[T, U], H]
				}
			}

	      	override type ~ = TT forSome {
		        type TT <: ~~[_ <: Ɛ.~, TT]
		     }

		    override type x[SS <: ~, TT <: ~] = (SS, TT) with TT#PREBIPRODUCT[SS]

			class VanillaWrapper[A <: Ɛ.~](a: A) extends
		    	~~[A, VanillaWrapper[A]] {
		        override val element = a
		  	}

		}
	}
}

object AvoidingRevealAndReceive {
	type ~
	trait ~~[T <: ~, TT <: ~~[T, TT]] 

  	type Doubleton = TT forSome {
        type TT <: ~~[_ <: ~, TT]
     }

	trait InnerStar[TT <: Doubleton] {
		def preProduct[S <: ~, SS <: ~~[S, SS]](pre: Star[S, SS]): BiproductStar[SS, TT]
	}

	trait BiproductStar[SS <: Doubleton, TT <: Doubleton] 

	trait Star[T <: ~, TT <: ~~[T, TT]] extends InnerStar[TT] {
		def product[U <: ~, UU <: ~~[U, UU]](that: Star[U, UU]): BiproductStar[TT, UU]

		def preProduct[S <: ~, SS <: ~~[S, SS]](pre: Star[S, SS]): BiproductStar[SS, TT] =
			pre.product(this)

		def product[UU <: Doubleton](that: InnerStar[UU]): BiproductStar[TT, UU] =
			that.preProduct(this)
	}
}

object CompilerStillBeingSillyAboutBiproducts {
	trait ToposLite { Ɛ =>
		type ~
		type x[S <: ~, T <: ~] <: Pair[S, T] with ~
		type STAR[S <: ~]
		type BIPRODUCT[L <: ~, R <: ~] = BiproductStar[L, R] with STAR[L x R]
		trait BiproductStar[L <: ~, R <: ~] { star: STAR[L x R] =>
		}

		class ActionsLite extends ToposLite {
			trait ~~[T <: Ɛ.~, TT <: ~~[T, TT]] {
				val element: T

				// def preBiproduct[SS <: ~](bogusSS: SS, bogusTT: TT): BIPRODUCT[SS, TT] = 
				// 	null
					// bogusSS.postBiproduct[T, TT](bogusTT, bogusSS)

				// def postBiproduct[U <: Ɛ.~, UU <: ~~[U, UU]](bogusTT: TT, bogusUU: UU): BIPRODUCT[TT, UU] =
				// 	null

					// new Star[Ɛ.x[T, U], x[TT, UU]] with BiproductStar[TT, UU] {
					// 	def ^(sXt: Ɛ.x[T, U]): x[TT, UU] = null
					// }

					// new Star[Ɛ.x[S, T], x[SS, TT]] with BiproductStar[SS, TT] {
					// 	def ^(sXt: Ɛ.x[S, T]): x[SS, TT] = null
					// }

					// new Star[S <: Ɛ.~, SS <: ~~[S, SS]r[]
			}

			class BiproductWrapper[
		        A <: Ɛ.~,
		        AA <: ~~[A, AA],
		        B <: Ɛ.~,
		    	BB <: ~~[B, BB]
		    ] (
		        aa: AA,
		        bb: BB,
		        aXb: Ɛ.x[A, B]
		    ) extends Pair[AA, BB](aa, bb) with ~~[
		        Ɛ.x[A, B],
		        BiproductWrapper[A, AA, B, BB]
		    ] {
		        override val element = aXb
		    }

	      	override type ~ = TT forSome {
		        type TT <: ~~[_ <: Ɛ.~, TT]
		    }

			trait DoubleLinkContextFacade[SS <: ~, TT <: ~] {
				type LINKBIPRODUCT <: Pair[SS, TT] with ~
				// type PREBIPRODUCT[SS <: ~] = SS#POSTBIPRODUCT[T, TT] 
				// type POSTBIPRODUCT[U <: Ɛ.~, UU <: ~~[U, UU]] = BiproductWrapper[T, TT, U, UU]
			}

			class DoubleLinkContext[
				S <: Ɛ.~,
				SS <: ~~[S, SS],
				T <: Ɛ.~,
				TT <: ~~[T, TT]
			](left: Star[S, SS], right: Star[T, TT]) extends DoubleLinkContextFacade[SS, TT] {
				override type LINKBIPRODUCT = BiproductWrapper[S, SS, T, TT]

				lazy val biproduct: BIPRODUCT[SS, TT] =
					null
					// new Star[Ɛ.x[S, T], x[SS, TT] /* LINKBIPRODUCT */] with BiproductStar[SS, TT] {
					// 	def ^(sXt: Ɛ.x[S, T]): x[SS, TT] = null.asInstanceOf[x[SS, TT]]
					// }.asInstanceOf[BIPRODUCT[SS, TT]]
			}

		    override type x[SS <: ~, TT <: ~] = DoubleLinkContextFacade[SS, TT]#LINKBIPRODUCT
			type STAR[S <: ~] = StarFacade[S]

			class VanillaWrapper[A <: Ɛ.~](a: A) extends
		    	~~[A, VanillaWrapper[A]] {
		        override val element = a
		  	}

		  	trait StarFacade[SS <: ~]
		  	trait Star[S <: Ɛ.~, SS <: ~~[S, SS]] extends StarFacade[SS] {
		  		def ^(s: S) : SS
		  		def biproduct[T <: Ɛ.~, TT <: ~~[T, TT]](that: Star[T, TT]): BIPRODUCT[SS, TT] = {
		  			// val bogusSS = this.^(null.asInstanceOf[S])
		  			// val bogusTT = that.^(null.asInstanceOf[T])
		  			// bogusTT.preBiproduct(bogusSS, bogusTT)
		  			new DoubleLinkContext[S, SS, T, TT](this, that).biproduct
		  		}
		  	}
		}
	}
}

object SuccessWithDoubleLinkContextFacadeAndSimpleWrapperAndWeakBiproduct {
	trait ToposLite { Ɛ =>
		type ~
		type x[S <: ~, T <: ~] <: Pair[S, T] with ~
		type STAR[S <: ~]
		type BIPRODUCT[L <: ~, R <: ~, LXR <: ~] = BiproductStar[L, R, LXR] with STAR[LXR]
		trait BiproductStar[L <: ~, R <: ~, LXR <: ~] { star: STAR[LXR] =>
		}

		class ActionsLite extends ToposLite {
			trait ~~[T <: Ɛ.~] {
				val element: T
			}

			class BiproductWrapper[
		        A <: Ɛ.~,
		        AA <: ~~[A],
		        B <: Ɛ.~,
		    	BB <: ~~[B]
		    ] (
		        aa: AA,
		        bb: BB,
		        aXb: Ɛ.x[A, B]
		    ) extends Pair[AA, BB](aa, bb) with ~~[
		        Ɛ.x[A, B]
		    ] {
		        override val element = aXb
		    }

	      	override type ~ = ~~[_ <: Ɛ.~]

			trait DoubleLinkContextFacade[SS <: ~, TT <: ~] {
				type LINKBIPRODUCT <: Pair[SS, TT] with ~
			}

			class DoubleLinkContext[
				S <: Ɛ.~,
				SS <: ~~[S],
				T <: Ɛ.~,
				TT <: ~~[T]
			](left: Star[S, SS], right: Star[T, TT]) extends DoubleLinkContextFacade[SS, TT] {
				override type LINKBIPRODUCT = BiproductWrapper[S, SS, T, TT]

				lazy val biproduct: BIPRODUCT[SS, TT, LINKBIPRODUCT] =
					new Star[Ɛ.x[S, T], LINKBIPRODUCT ] with BiproductStar[SS, TT, LINKBIPRODUCT ] {
						def ^(sXt: Ɛ.x[S, T]): LINKBIPRODUCT = null.asInstanceOf[LINKBIPRODUCT]
					}
			}

		    override type x[SS <: ~, TT <: ~] = DoubleLinkContextFacade[SS, TT]#LINKBIPRODUCT
			type STAR[S <: ~] = StarFacade[S]

			class VanillaWrapper[A <: Ɛ.~](a: A) extends
		    	~~[A] {
		        override val element = a
		  	}

		  	trait StarFacade[SS <: ~]
		  	trait Star[S <: Ɛ.~, SS <: ~~[S]] extends StarFacade[SS] {
		  		def ^(s: S) : SS
		  		def biproduct[T <: Ɛ.~, TT <: ~~[T]](that: Star[T, TT]): BIPRODUCT[SS, TT, _ <: ~] = {
		  			new DoubleLinkContext[S, SS, T, TT](this, that).biproduct
		  		}
		  	}
		}
	}
}

case class ↔[A, B] (
  / : A => B,
  \ : B => A
) {
  // def o[C](Δ: B ↔ C) = 
  // 	 new ↔[A, C](/ andThen Δ./, Δ.\ andThen \)
  // def unary_~() =
  //   new ↔[B, A](\, /)
}

/*
object SelfDescribingElements {
	trait ToposLite { Ɛ =>
		type ~[S]
		type x[S <: ~[S], T <: ~[T]] = SxT forSome { 
			type SxT <: ~[SxT]
		}
		type BIPRODUCT[L <: ~[L], R <: ~[R]] = BiproductStar[L, R, L x R] 
		trait BiproductStar[L <: ~[L], R <: ~[R], LXR <: ~[LXR]] 
	}
}
*/

/*
object LinkagesUnbound {
	trait ToposLite { Ɛ =>
		type ~[S]
		type x[S <: ~[S], T <: ~[T]] <: (S, T) with ~[x[S, T]]
		type STAR[S <: ~[S]] <: Star[S]
		type BIPRODUCT[L <: ~[L], R <: ~[R]] = BiproductStar[L, R, L x R] with STAR[L x R]
		trait BiproductStar[L <: ~[L], R <: ~[R], LXR <: ~[LXR]] { star: STAR[LXR] => }

		trait Star[S <: ~[S]] {
		    def x[T <: ~[T]](that: STAR[T]): BIPRODUCT[S, T]
		}

		class ActionsLite extends ToposLite {
			trait ~~~ // can be a type?
			trait ~~[T <: Ɛ.~[T]] extends ~~~ {
				val element: T
			}

			trait Link[THIS <: Link[THIS]] { link =>
				// type THIS <: Link { type THIS = link.THIS }
				type BASE <: Ɛ.~[BASE]
				type LIFT <: ~~~
				val ↔ : ↔[BASE, LIFT]

				class ActionStar(val star: Ɛ.STAR[BASE]) extends Star[THIS] {
				    override def x[THAT <: ~[THAT]](that: STAR[THAT]): BIPRODUCT[THIS, THAT] = {
				    	val productLinkage: THIS x THAT = null.asInstanceOf[THIS x THAT]
				    		// linkage x that.linkage
				    	// productLinkage.biproductStar(star, that.star)
				    	null
				    	// new ActionStar[T x U](productLinkage) with BiproductStar[T, U, T x U]
				    }
				}
			}

			case class BiproductLink[
				L <: Link[L], 
				R <: Link[R]
			] (left: L, right: R) extends Link[BiproductLink[L, R]] {
				// override type THIS = 
				override type BASE = Ɛ.x[left.BASE, right.BASE]
				override type LIFT = (left.LIFT, right.LIFT) with ~~~
				val ↔ = null
			}

			class BiproductWrapper[
		        A <: Ɛ.~[A],
		        AA <: ~~~,
		        B <: Ɛ.~[B],
		    	BB <: ~~~
		    ] (
		        aa: AA,
		        bb: BB,
		        aXb: Ɛ.x[A, B]
		    ) extends (AA, BB)(aa, bb) with ~~[
		        Ɛ.x[A, B]
		    ] {
		        override val element = aXb
		    }

	      	override type ~[S] = Link[S]
			override type x[S <: ~[S], T <: ~[T]] = (S, T) with ~[x[S, T]] // BiproductLink[S, T]#LIFT // TODO amalgamate?
			override type STAR[S <: ~[S]] = S#ActionStar
		}
	}
}
*/

object LinkagesUnbound {
	trait ToposLite { Ɛ =>
		type ~
		type x[S <: ~, T <: ~] <: Pair[S, T] with ~
		type STAR[S <: ~] <: Star[S]
		type BIPRODUCT[L <: ~, R <: ~] = BiproductStar[L, R, L x R] with STAR[L x R]
		trait BiproductStar[L <: ~, R <: ~, LXR <: ~] { star: STAR[LXR] => }

		trait Star[S <: ~] {
		    def x[T <: ~](that: STAR[T]): BIPRODUCT[S, T]
		}

		class ActionsLite extends ToposLite {
			trait ~~~ // can be a type?
			trait ~~[T <: Ɛ.~] extends ~~~ {
				val element: T
			}

			trait Link { link =>
				type THIS >: link.type <: (Link { type THIS = link.THIS }) 
				type BASE <: Ɛ.~
				type LIFT <: ~~~
				val ↔ : ↔[BASE, LIFT]

				class ActionStar(val star: Ɛ.STAR[BASE]) extends Star[THIS] { actionStar =>
					val uplink = link : THIS
				    override def x[THAT <: ~](that: STAR[THAT]): BIPRODUCT[THIS, THAT] = {
				    	val productLink = new BiproductLink(uplink, that.uplink)
				    	// productLink(star, that.star)
				    	null
				    }
				}
			}

			class BiproductLink[
				L <: Link, 
				R <: Link
			] (override val left: L, override val right: R) extends
		  Pair[L, R](left, right) with Link {
				override type THIS = BiproductLink[L, R]
				override type BASE = Ɛ.x[left.BASE, right.BASE]
				override type LIFT = (left.LIFT, right.LIFT) with ~~~
				val ↔ = null

				def apply(
					leftStar: Ɛ.STAR[left.BASE], 
					rightStar: Ɛ.STAR[right.BASE]
				) =
					new ActionStar(leftStar x rightStar) with BiproductStar[L, R, BiproductLink[L, R]]
			}

			class BiproductWrapper[
		        A <: Ɛ.~,
		        AA <: ~~~,
		        B <: Ɛ.~,
		    	BB <: ~~~
		    ] (
		        aa: AA,
		        bb: BB,
		        aXb: Ɛ.x[A, B]
		    ) extends Pair[AA, BB](aa, bb) with ~~[
		        Ɛ.x[A, B]
		    ] {
		        override val element = aXb
		    }

	      	override type ~ = Link

			override type x[S <: ~, T <: ~] = BiproductLink[S, T]

			override type STAR[S <: ~] = (Link { type THIS = S })#ActionStar 
		}
	}
}

