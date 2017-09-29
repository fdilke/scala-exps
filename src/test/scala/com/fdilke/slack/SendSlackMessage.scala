package com.fdilke.slack

import akka.actor.ActorSystem
import slack.api.SlackApiClient
import slack.models.{Channel, User}

import scala.concurrent.Future
import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.io.Source

// See https://github.com/gilbertw1/slack-scala-client

object SendSlackMessage extends App {
  val SLACK_TOKEN_PATH =
    "config/slack.cfg"

  val SLACK_TOKEN: String =
    Source.fromFile(
      SLACK_TOKEN_PATH
    ).getLines.toSeq find {
      _.startsWith("token=")
    } match {
      case None =>
        throw new IllegalArgumentException(
          "Can't find Slack token in config: " + SLACK_TOKEN_PATH
        )
      case Some(line) =>
        line.split("=")(1)
    }

  println(s"found token: $SLACK_TOKEN")
  val client = SlackApiClient(SLACK_TOKEN)

  implicit val system = ActorSystem("slack")

  client.listChannels().onComplete {
    case Success(channels) =>
      println("found channels:" + channels.map { _.name }.mkString(" ; "))

    case Failure(err) =>
      println("call failed")
  }

  client.listUsers().onComplete {
    case Success(users) =>
      println("found users:" + users.map { _.name }.mkString(" ; "))

      users.find { _.name == "felix" }.foreach { user =>
        println("Felix: " + user)
      }
    case Failure(err) =>
      println("list users failed")
  }

  if (false) {
    client.getUserInfo("felix").onComplete {
      case Success(user) =>
        println("id, name = " + user.id + "," + user.name)

      case Failure(err) =>
        println("no user info")
    }

    client.openIm("felix").onComplete {
      case Success(id) =>
        println("started session: id = " + id)

      case Failure(err) =>
        println("no IM")
    }
  }
}
