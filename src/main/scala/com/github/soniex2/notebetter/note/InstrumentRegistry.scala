package com.github.soniex2.notebetter.note

import com.github.soniex2.notebetter.NoteBetter
import com.github.soniex2.notebetter.api.{NoteBetterAPIInstance, NoteBetterInstrument}
import com.github.soniex2.notebetter.config.util.JsonHelper
import com.github.soniex2.notebetter.util.GsonScalaHelper._
import com.github.soniex2.notebetter.util.MinecraftScalaHelper._
import com.google.common.base.Predicates
import com.google.gson._
import net.minecraft.block.Block.blockRegistry
import net.minecraft.block.state.IBlockState
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.{BlockPos, ResourceLocation}
import net.minecraft.world.IBlockAccess

/**
  * @author soniex2
  */
class InstrumentRegistry(defaultInstrument: Option[NoteBetterInstrument],
                         blocks: Map[ResourceLocation, InstrumentBlock],
                         materials: List[MaterialSound]) extends NoteBetterAPIInstance {

  override def getInstrument(worldTarget: IBlockAccess, blockPosTarget: BlockPos, worldSource: IBlockAccess, blockPosSource: BlockPos): NoteBetterInstrument = {
    getInstrument(worldTarget.getBlockState(blockPosTarget), worldTarget.getTileEntity(blockPosTarget))
  }

  override def getInstrument(blockState: IBlockState, tileEntity: TileEntity): NoteBetterInstrument = {
    val tile = Option(tileEntity)
    val block = blockState.getBlock
    Option(blockRegistry.getNameForObject(block)).flatMap(blocks.get).flatMap((v) => Option(v.get(blockState))).orElse(materials.find((v) => Option(blockRegistry.getObject(v.getBlockName)).map(_.getMaterial).contains(block.getMaterial)).map(_.getInstrument)).orElse(defaultInstrument).orNull
  }

  override def getInstrument(itemStack: ItemStack): NoteBetterInstrument = null

}

object InstrumentRegistry {
  private val JSON_ADAPTER: Gson = (new GsonBuilder).registerTypeAdapter(classOf[InstrumentRegistry], new InstrumentRegistry.Serializer).setPrettyPrinting().create

  def fromString(s: String): InstrumentRegistry = {
    if (s.length == 0) {
      new InstrumentRegistry(None, Map.empty, List.empty)
    } else {
      try {
        JSON_ADAPTER.fromJson(s, classOf[InstrumentRegistry])
      }
      catch {
        case exception: Exception =>
          new InstrumentRegistry(None, Map.empty, List.empty)
      }
    }
  }

  class Serializer extends JsonDeserializer[InstrumentRegistry] with JsonSerializer[InstrumentRegistry] {
    private def getInstrument(jsonElement: Option[WJsonElement]) = {
      val name = jsonElement flatMap {
        case WJsonString(s) => Some(s)
        case o@WJsonObject(_) if o has "name" => o get "name" map {
          case WJsonString(s) => s
          case _ => throw new JsonSyntaxException(s"Invalid instrument: $jsonElement")
        }
        case _ => throw new JsonSyntaxException(s"Invalid instrument: $jsonElement")
      }
      val volume = for {
        elem <- jsonElement
        obj <- elem.asJsonObject
        volElem <- obj get "volume"
        volNum <- volElem.asJsonNumber
      } yield volNum.floatValue
      new NoteBetterInstrument((name map CachedResourceLocation).orNull, volume getOrElse 3f)
    }

    private def readVariants(element: Option[WJsonElement]) = {
      // TODO
    }

    private def readBlock(element: Option[WJsonElement]): Option[(ResourceLocation, InstrumentBlock)] = {
      for {
        blockObject <- element flatMap (_.asJsonObject)
        blockNameElem <- blockObject get "block"
        blockName <- blockNameElem.asJsonString
      } yield {
        // TODO
        if (!blockObject.has("sound")) throw new JsonSyntaxException("Invalid instrument")
        val soundElement = blockObject get "sound"
        val is: InstrumentBlock = new InstrumentBlock
        is.addPredicate(Predicates.alwaysTrue(), getInstrument(soundElement)) // TODO
        (CachedResourceLocation(blockName.string), is)
      }
    }

    private def readMaterial(element: Option[WJsonElement]) = {
      for {
        materialObject <- element flatMap (_.asJsonObject)
        materialNameElem <- materialObject.get("material_of") orElse materialObject.get("material")
        materialName <- materialNameElem.asJsonString
      } yield {
        if (!materialObject.has("sound")) throw new JsonSyntaxException("Invalid instrument")
        new MaterialSound(CachedResourceLocation(materialName.string), getInstrument(materialObject get "sound"))
      }
    }

    @throws(classOf[JsonParseException])
    override def deserialize(json: JsonElement, typeOfT: java.lang.reflect.Type, context: JsonDeserializationContext): InstrumentRegistry = {
      try {
        val Some(jsonObject) = WJsonElement(json) flatMap (_.asJsonObject)
        val defaultInstrument = jsonObject get "default" map (e => getInstrument(Some(e)))
        val blocks = JsonHelper.optJsonArray(jsonObject, "blocks") map (_.flatMap(readBlock).toMap) getOrElse Map.empty
        val materials = JsonHelper.optJsonArray(jsonObject, "materials") map (_.flatMap(readMaterial).toList) getOrElse List.empty
        return new InstrumentRegistry(defaultInstrument, blocks, materials)
      }
      catch {
        case e: Exception => {
          NoteBetter.instance.log.error("Error decoding config file", e)
          return new InstrumentRegistry(None, Map.empty, List.empty)
        }
      }
    }

    private def toSoundObject(instrument: NoteBetterInstrument): JsonObject = {
      val jsonObject: JsonObject = new JsonObject
      jsonObject.add("name", Option(instrument.getSoundEvent).map((x) => {
        new JsonPrimitive(x.toString)
      }).orNull)
      jsonObject.addProperty("volume", instrument.getVolume)
      jsonObject
    }

    override def serialize(config: InstrumentRegistry, typeOfSrc: java.lang.reflect.Type, context: JsonSerializationContext): JsonElement = {
      /*val jsonObject: JsonObject = new JsonObject
      if (config.defaultInstrument != null) {
        jsonObject.add("default", toSoundObject(config.defaultInstrument))
      }
      val blocks: JsonArray = new JsonArray
      val materials: JsonArray = new JsonArray
      for (sound <- config.materials) {
        val materialObject: JsonObject = new JsonObject
        materialObject.addProperty("block", sound.getBlockName.toString)
        materialObject.add("sound", toSoundObject(sound.getInstrument))
        materials.add(materialObject)
      }
      for (sound <- config.blocks.entrySet) {
        val blockObject: JsonObject = new JsonObject
        blockObject.addProperty("block", sound.getKey.toString)
        blocks.add(blockObject)
      }
      jsonObject.add("materials", materials)
      jsonObject.add("blocks", blocks)
      return jsonObject*/
      return new JsonObject
    }
  }

}
