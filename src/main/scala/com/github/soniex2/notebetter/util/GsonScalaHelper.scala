package com.github.soniex2.notebetter.util

import com.google.gson._
import scala.collection.JavaConverters.asScalaIteratorConverter

/**
  * @author soniex2
  */
object GsonScalaHelper {

  sealed abstract class WJsonElement {
    def asJsonString = this match {
      case e@WJsonString(_) => Some(e)
      case _ => None
    }

    def asJsonBoolean = this match {
      case e@WJsonBoolean(_) => Some(e)
      case _ => None
    }

    def asJsonNumber = this match {
      case e@WJsonNumber(_) => Some(e)
      case _ => None
    }

    def asJsonArray = this match {
      case e@WJsonArray(_) => Some(e)
      case _ => None
    }

    def asJsonObject = this match {
      case e@WJsonObject(_) => Some(e)
      case _ => None
    }
  }

  sealed abstract class WJsonPrimitive extends WJsonElement

  final case class WJsonString(string: String) extends WJsonPrimitive

  final case class WJsonBoolean(boolean: Boolean) extends WJsonPrimitive

  final case class WJsonNumber(number: Number) extends WJsonPrimitive {
    def floatValue = number.floatValue()
  }

  final case class WJsonArray(jsonArray: JsonArray) extends WJsonElement with Iterable[Option[WJsonElement]] {
    @inline def get(i: Int) = Option(jsonArray.get(i)) flatMap WJsonElement

    override def iterator: Iterator[Option[WJsonElement]] = {
      jsonArray.iterator().asScala.map(WJsonElement)
    }
  }

  final case class WJsonObject(jsonObject: JsonObject) extends WJsonElement {
    @inline def has(name: String) = jsonObject.has(name)

    @inline def get(name: String) = Option(jsonObject.get(name)) flatMap WJsonElement
  }

  //final case class WJsonNull() extends WJsonElement // this wrapper prefers None

  object WJsonElement extends (JsonElement => Option[WJsonElement]) {
    override def apply(e: JsonElement): Option[WJsonElement] = {
      e match {
        case _: JsonNull => None
        case _ => Some(e match {
          case e: JsonPrimitive if e.isString => WJsonString(e.getAsString)
          case e: JsonPrimitive if e.isBoolean => WJsonBoolean(e.getAsBoolean)
          case e: JsonPrimitive if e.isNumber => WJsonNumber(e.getAsNumber)
          case e: JsonArray => WJsonArray(e)
          case e: JsonObject => WJsonObject(e)
        })
      }
    }
  }

}
