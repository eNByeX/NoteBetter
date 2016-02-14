package com.github.soniex2.notebetter.config.util

import com.github.soniex2.notebetter.util.GsonScalaHelper.{WJsonArray, WJsonObject}

/**
  * @author soniex2
  */
object JsonHelper {
  @inline def optJsonArray(jsonObject: WJsonObject, key: String): Option[WJsonArray] = {
    jsonObject get key flatMap (_.asJsonArray)
  }
}