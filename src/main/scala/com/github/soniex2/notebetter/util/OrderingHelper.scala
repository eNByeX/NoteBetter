package com.github.soniex2.notebetter.util

import scala.collection.immutable.{TreeMap, TreeSet}

/**
  * @author soniex2
  */
object OrderingHelper {

  // TODO optimize these

  trait TreeSetOrdering[T] extends Ordering[TreeSet[T]] {
    def ordering: Ordering[T]
    override def compare(x: TreeSet[T], y: TreeSet[T]): Int = {
      x.size - y.size match {
        case 0 => x.iterator.zip(y.iterator).map((t) => ordering.compare(t._1, t._2)).find(_ != 0).getOrElse(0)
        case e => e
      }
    }
  }

  implicit def treeSetOrdering[T](implicit ord: scala.Ordering[T]): Ordering[TreeSet[T]] = {
    new TreeSetOrdering[T] {
      val ordering: Ordering[T] = ord
    }
  }


  trait TreeMapOrdering[A, B] extends Ordering[TreeMap[A, B]] {
    def ordering: scala.Ordering[(A, B)]
    override def compare(x: TreeMap[A, B], y: TreeMap[A, B]): Int = {
      x.size - y.size match {
        case 0 => x.iterator.zip(y.iterator).map((v) => ordering.compare(v._1, v._2)).find(_ != 0).getOrElse(0)
        case e => e
      }
    }
  }

  implicit def treeMapOrdering[A, B](implicit ord: scala.Ordering[(A, B)]): Ordering[TreeMap[A, B]] = {
    new TreeMapOrdering[A, B] {
      val ordering: Ordering[(A, B)] = ord
    }
  }

}
