package com.fdilke.runtime

import java.net.URLClassLoader

import org.scalatest.FunSpec
import org.scalatest.matchers.{MatchResult, Matcher}

import scala.tools.nsc.Settings
import scala.tools.nsc.interpreter.IMain
import scala.tools.nsc.interpreter.Results
import scala.tools.nsc.util.ClassPath

trait RunTimeCompilation { self: FunSpec =>
  private lazy val engine = {
    val settings = new Settings
    settings.classpath.value = System.getProperty("java.class.path")
    new IMain(settings)
  }

  def inContextOf(imports: String*)(executeBlock: => Unit) =
    engine.beSilentDuring {
      engine.reset
      for (importArgument <- imports)
        engine.interpret(s"import $importArgument")
      executeBlock
    }

  val compile = Matcher { code: String =>
    MatchResult(
      try {
        engine.interpret(code) == Results.Success
      } catch {
        case ex: Exception =>
          false
      },
      s""""$code" did not compile""",
      s""""$code" compiled ok"""
    )
  }

  // Some other things we can do:
  //  println (engine.put("n", 10))
  //  println(engine.put("@", 5))
  //  val result = engine.eval("1 to n.asInstanceOf[Int] foreach println")
  //  println(s"result = $result")
}
