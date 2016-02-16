package com.github.soniex2.notebetter.note

import com.github.soniex2.notebetter.api.NoteBetterInstrument
import net.minecraft.block.state.IBlockState

import scala.collection.JavaConverters._
import scala.collection.immutable.{TreeMap, TreeSet}

/**
  * @author soniex2
  */
class InstrumentBlock(pred: List[(TreeMap[String, TreeSet[String]], NoteBetterInstrument)]) {

  def this() = this(List.empty)

  private var _predicates: List[(TreeMap[String, TreeSet[String]], NoteBetterInstrument)] = pred.sortBy(-_._1.size)
  //private var propTrie: PropTrie[Any with Comparable[Any], NoteBetterInstrument] = new PropTrie()

  def predicates = _predicates

  def addPredicate(predicate: TreeMap[String, TreeSet[String]], instrument: NoteBetterInstrument) {
    _predicates = ((predicate, instrument) :: _predicates).sortBy(-_._1.size)
  }

  def get(state: IBlockState): Option[NoteBetterInstrument] = {
    _predicates.find((v) => {
      state.getPropertyNames.asScala.flatMap((p) => v._1.get(p.getName).map((p, _))).forall((x) => x._2.contains(state.getValue(x._1).toString))
    }).map(_._2)
  }

  override def toString = {
    pred.toString()
  }
}