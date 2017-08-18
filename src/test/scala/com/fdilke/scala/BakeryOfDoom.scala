package com.fdilke.scala

object BakeryOfDoom {

  trait LogicalOperations {
    Ɛ: BaseTopos with
      ToposStructures with
      AlgebraicStructures =>
  }

  trait ToposStructures extends
    Monads with
    StrongMonads with
    MonadCaching {
    Ɛ: BaseTopos with ToposEnrichments =>
  }

  trait AlgebraicStructures extends
    BaseTopos with
    ToposEnrichments with
    ToposStructures with
    AlgebraicMachinery {
    builder =>
  }

  trait AlgebraicMachinery {
    topos: BaseTopos =>
  }

  trait ElementEnrichments {
    Ɛ: BaseTopos with ToposStructures =>
  }

  trait Monads {
    Ɛ: BaseTopos =>
  }

  trait StrongMonads {
    Ɛ: BaseTopos with
      ToposStructures with
      MonadicPlumbing =>
  }

  trait MonadicPlumbing {
    Ɛ: BaseTopos with ToposStructures =>
  }

  trait MonadCaching {
    Ɛ: BaseTopos with
      Monads with
      StrongMonads =>
  }

  trait ToposEnrichments extends
    LogicalOperations with
    ElementEnrichments with
    MonadicPlumbing {
    Ɛ: BaseTopos with
      ToposStructures with
      AlgebraicStructures =>
  }

  trait BaseTopos {
    Ɛ: ToposEnrichments with
      ToposStructures =>
    type ~
  }

  // Adding an extension to the bakery of doom

  trait MonadOfMonoidActions {
    Ɛ: BaseTopos with
      ToposStructures with
      AlgebraicStructures =>
  }

  trait ContinuationMonad {
    Ɛ: BaseTopos with ToposStructures =>
  }

  trait MonadConstructions extends
    ContinuationMonad with
    MonadOfMonoidActions {
    Ɛ: BaseTopos with
      ToposStructures with
      AlgebraicStructures =>
  }

  trait ConstructDefaultMonoidAssistant extends
    BaseTopos with
    ToposEnrichments {

    Ɛ: AlgebraicStructures with AlgebraicMachinery =>
  }

  trait ConstructToposOfAutomorphisms extends
    BaseTopos with
    ToposEnrichments {
    Ɛ: AlgebraicStructures with AlgebraicMachinery =>
  }

  trait ConstructToposOfGroupActions extends
    BaseTopos with ToposEnrichments {
    Ɛ: AlgebraicStructures with
      AlgebraicMachinery =>
  }

  trait ConstructToposOfMonoidActions extends
    BaseTopos with
    ToposEnrichments with
    ConstructDefaultMonoidAssistant {
    Ɛ: AlgebraicStructures with AlgebraicMachinery =>
  }

  trait ToposConstructions extends
    BaseTopos with
    ConstructToposOfMonoidActions with
    ConstructToposOfGroupActions with
    ConstructToposOfAutomorphisms {

    Ɛ: AlgebraicStructures with
      AlgebraicMachinery with
      ToposEnrichments with
      ToposStructures =>
  }

  trait AlgebraicConstructions {
    topos: BaseTopos with
      AlgebraicMachinery with
      AlgebraicStructures =>
  }

  trait ToposAlgebra extends
    AlgebraicMachinery with
    AlgebraicConstructions with
    AlgebraicStructures

  trait Topos[BASE] extends BaseTopos with
    Monads with
    MonadConstructions with
    ToposEnrichments with
    ToposAlgebra with
    ToposConstructions {
    override type ~ = BASE
  }
}