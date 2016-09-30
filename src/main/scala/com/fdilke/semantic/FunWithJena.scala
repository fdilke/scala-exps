package com.fdilke.semantic

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype
import com.hp.hpl.jena.rdf.model.Model
import com.hp.hpl.jena.rdf.model.ModelFactory
import com.hp.hpl.jena.rdf.model.Property
import com.hp.hpl.jena.rdf.model.Resource
import org.apache.jena.query.{QueryExecution, QueryExecutionFactory, QueryFactory, ResultSet}
import org.apache.jena.rdf.model.RDFNode

import scala.collection.JavaConversions._

object FunWithJena extends App {
  val nameSpace = "http://example.org/test/"

  val model: Model =
    ModelFactory.createDefaultModel()

  val subject : Resource =
    model.createResource(nameSpace + "message")

  val property: Property =
    model.createProperty(nameSpace + "says")

  subject.addProperty(
    property,
    "Hello World!",
    XSDDatatype.XSDstring
  )

  model.write(System.out)
}

object MoreFunWithJena extends App {
  // See bumper list of standard prefixes at
  // http://dbpedia.org/sparql?nsdecl

  val queryString =
    """
      |PREFIX schema: <http://schema.org/>
      |
      |select distinct ?Band where {
      |  ?Band a schema:MusicGroup
      |} LIMIT 10
    """.stripMargin

  val query = QueryFactory.create(queryString)
  // initializing queryExecution factory with remote service.
  // **this actually was the main problem I couldn't figure out.**
  val qexec: QueryExecution  =
    QueryExecutionFactory.sparqlService(
      "http://dbpedia.org/sparql",
      query
    )

    try {
      val results: ResultSet  = qexec.execSelect()
      while (results.hasNext) {
        val solution = results.next()
//        solution.varNames().foreach { s =>
//          println("name: " + s)
//        }
        val node: RDFNode = solution.get("Band")
//        println("band as literal: " + node.asLiteral())
        val bandName = node.asResource().getURI.split('/').last
        println("bandName: " + bandName)
        // Result processing is done here.
      }
    }
    finally {
      qexec.close()
    }
}