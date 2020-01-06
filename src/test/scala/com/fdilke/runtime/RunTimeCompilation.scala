package com.fdilke.runtime

import java.io.PrintWriter
import java.net.URLClassLoader

import com.fdilke.scala.MinimalErrorReporter
import javax.script.{ScriptContext, SimpleScriptContext}
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.{MatchResult, Matcher}
import scala.jdk.CollectionConverters._
import scala.reflect.internal.util.Position
import scala.tools.nsc.{Interpreter, Settings}
import scala.tools.nsc.interpreter.shell.ReplReporterImpl
import scala.tools.nsc.interpreter.{IMain, ImportContextPreamble, Results, ScriptedInterpreter, ScriptedRepl}
import scala.tools.nsc.util.ClassPath

trait RunTimeCompilation { self: AnyFunSpec =>
  private lazy val engine: ScriptedRepl = {
    val settings = new Settings
    settings.classpath.value = System.getProperty("java.class.path")
//    new IMain(settings)
    val ctx = s"$$ctx"
    val compileContext: ScriptContext = new SimpleScriptContext

    // names available in current dynamic context
    def contextNames: Set[String] = {
      val ctx = compileContext
        val terms = for {
           scope <- ctx.getScopes.asScala
           binding <- Option(ctx.getBindings(scope)) map (_.asScala) getOrElse Nil
           key = binding._1
         } yield key
         Set.from(terms)
    }

    def importContextPreamble(wanted: Set[String]): ImportContextPreamble = {
      // cull references that can be satisfied from the current dynamic context
      val contextual = wanted & contextNames

      if (contextual.isEmpty) ImportContextPreamble.empty
      else {
        val adjusted = contextual.map { valname =>
          s"""def `$valname` = $ctx.`$valname`; """ +
            s"""def `${valname}_=`(x: _root_.java.lang.Object) = $ctx.`$valname` = x;"""
        }.mkString("", "\n", "\n")
        ImportContextPreamble(contextual, Set(ctx), adjusted)
      }
    }
    val out: PrintWriter = new PrintWriter(System.err)
    val reporter: MinimalErrorReporter = new MinimalErrorReporter(settings, out)
    val engine : ScriptedRepl =
      new ScriptedInterpreter(settings, reporter, importContextPreamble)
    engine.initializeCompiler()
    engine
  }

//  private lazy val engine = {
//    val settings = new Settings
//    settings.classpath.value = System.getProperty("java.class.path")
//
//    new ScriptedInterpreter(settings, )
//  }

  def inContextOf(imports: String*)(executeBlock: => Unit): Unit =
    engine.beQuietDuring {
      engine.reset
      for (importArgument <- imports)
        engine.interpret(s"import $importArgument")
      executeBlock
    }

  val compile: Matcher[String] =
    Matcher { code: String =>
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
