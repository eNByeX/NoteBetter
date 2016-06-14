package com.github.soniex2.notebetter.util

import net.minecraft.util.registry.RegistryNamespacedDefaultedByKey

/**
  * @author soniex2
  */
object MinecraftScalaHelper {

  type ResourceLocation = net.minecraft.util.ResourceLocation

  object ResourceLocation extends (String => ResourceLocation) with ((String, String) => ResourceLocation) {
    @inline override def apply(v1: String) = new ResourceLocation(v1)

    @inline override def apply(v1: String, v2: String) = new ResourceLocation(v1, v2)

    def unapply(resLoc: ResourceLocation): Option[String] = Some(resLoc.toString)
  }

  object CachedResourceLocation extends (String => CachedResourceLocation) with ((String, String) => CachedResourceLocation) {
    @inline override def apply(v1: String) = new CachedResourceLocation(v1)

    @inline override def apply(v1: String, v2: String) = new CachedResourceLocation(v1, v2)
  }

  type BlockPos = net.minecraft.util.math.BlockPos

  object BlockPos extends ((Int, Int, Int) => BlockPos) {
    @inline override def apply(x: Int, y: Int, z: Int) = new BlockPos(x, y, z)

    def unapply(blockPos: BlockPos): Option[(Int, Int, Int)] = {
      Some((blockPos.getX, blockPos.getY, blockPos.getZ))
    }
  }

  implicit class RegNamespacedDefaultedByKey[K, V](reg: RegistryNamespacedDefaultedByKey[K, V]) {
    def byKey(k: K) = Option(reg.getObject(k))

    def byVal(v: V) = Option(reg.getNameForObject(v))
  }

}
