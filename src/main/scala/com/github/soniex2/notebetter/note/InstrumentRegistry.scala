package com.github.soniex2.notebetter.note

import com.github.soniex2.notebetter.NoteBetter
import com.github.soniex2.notebetter.api.{CustomSoundEvent, RegisteredSoundEvent, NoteBetterAPIInstance, NoteBetterInstrument}
import com.github.soniex2.notebetter.config.util.JsonHelper
import com.github.soniex2.notebetter.util.GsonScalaHelper._
import com.github.soniex2.notebetter.util.MinecraftScalaHelper._
//import com.github.soniex2.notebetter.util.OrderingHelper._
import com.google.gson._
import net.minecraft.block.Block.blockRegistry
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.{SoundEvent, ResourceLocation}
import net.minecraft.world.IBlockAccess

import scala.collection.immutable.{TreeMap, TreeSet}

/**
  * @author soniex2
  */
class InstrumentRegistry(defaultInstrument: Option[NoteBetterInstrument],
                         blocks: Map[ResourceLocation, InstrumentBlock],
                         materials: List[MaterialSound],
                         known: Set[String]) extends NoteBetterAPIInstance {

  override def getInstrument(worldTarget: IBlockAccess, blockPosTarget: BlockPos, worldSource: IBlockAccess, blockPosSource: BlockPos): NoteBetterInstrument = {
    getInstrument(worldTarget.getBlockState(blockPosTarget), worldTarget.getTileEntity(blockPosTarget))
  }

  def getBlockInstrument(blockState: IBlockState, tileEntity: Option[TileEntity]): Option[NoteBetterInstrument] = {
    for {
      block <- blockRegistry.byVal(blockState.getBlock)
      blockInstrument <- blocks.get(block)
      instrument <- blockInstrument.get(blockState)
    } yield instrument
  }

  def getMaterialInstrument(material: Material): Option[NoteBetterInstrument] = {
    materials.find((v) => blockRegistry.byKey(v.blockName).map(_.getDefaultState.getMaterial).contains(material)).map(_.instrument())
  }

  override def getInstrument(blockState: IBlockState, tileEntity: TileEntity): NoteBetterInstrument = {
    val tile = Option(tileEntity) // TODO 1.2+
    getBlockInstrument(blockState, tile) // Try blocks
      .orElse(getMaterialInstrument(blockState.getMaterial)) // Try materials
      .orElse(defaultInstrument).orNull // Try default and convert to java.
  }

  override def getInstrument(itemStack: ItemStack): NoteBetterInstrument = null // TODO 1.2+

  override def isNoteBetterInstrument(s: String): Boolean = known.contains(new ResourceLocation(s).toString)
}

object InstrumentRegistry {
  private val JSON_ADAPTER: Gson = (new GsonBuilder).registerTypeAdapter(classOf[InstrumentRegistry], new InstrumentRegistry.Serializer).setPrettyPrinting().create

  def fromString(s: String): InstrumentRegistry = {
    if (s.length == 0) {
      new InstrumentRegistry(None, Map.empty, List.empty, Set.empty)
    } else {
      try {
        JSON_ADAPTER.fromJson(s, classOf[InstrumentRegistry])
      }
      catch {
        case exception: Exception =>
          new InstrumentRegistry(None, Map.empty, List.empty, Set.empty)
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
      val soundEvent = for {
        v <- name
        event <- Option(SoundEvent.soundEventRegistry.getObject(new ResourceLocation(v))).map(new RegisteredSoundEvent(_)).orElse({
          NoteBetter.instance.log.warn(s"Unknown sound event: $v. Interpreting as custom sound.")
          // TODO hooks?!
          Option(new CustomSoundEvent(new ResourceLocation(v)))
        })
      } yield event
      new NoteBetterInstrument(soundEvent.orNull, volume getOrElse 3f)
    }

    private def readVariants(obj: WJsonObject): InstrumentBlock = {
      // TODO optimize for 1.2+
      val states = for {
        statesElem <- obj.get("states")
        statesArr <- statesElem.asJsonArray
      } yield statesArr.flatMap((x) => {
        x.flatMap(_.asJsonObject).map((o) => {
          val variant: TreeMap[String, TreeSet[String]] = o.get("variant").flatMap(_.asJsonObject).map((variant) => {
            val m = TreeMap.empty[String, TreeSet[String]] ++ variant.map({
              case (k, v) =>
              val opt = v collect {
                case e@WJsonArray(_) => e.flatMap(_.flatMap(_.asJsonString).map(_.string)).to[TreeSet]
                case WJsonString(s) => TreeSet(s)
              }
              (k, opt.getOrElse(TreeSet.empty[String]))
            }).toList
            m
          }).getOrElse(TreeMap.empty)
          val sound = getInstrument(o.get("sound"))
          (variant, sound)
        })
      }).toList
      new InstrumentBlock(states.getOrElse(List.empty))
    }

    private def readBlock(element: Option[WJsonElement]): Option[(ResourceLocation, InstrumentBlock)] = {
      for {
        blockObject <- element flatMap (_.asJsonObject)
        blockNameElem <- blockObject get "block"
        blockName <- blockNameElem.asJsonString
      } yield {
        val is: InstrumentBlock = readVariants(blockObject)
        if (blockObject.has("sound"))
          is.addPredicate(TreeMap.empty, getInstrument(blockObject get "sound"))
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

        // build instrument set
        val fromBlocks = blocks.values.flatMap(_.predicates.map(_._2)).flatMap((v) => Option(v.iSoundEvent())).map(_.toString)
        val fromMaterials = materials.flatMap((v) => Option(v.instrument().iSoundEvent())).map(_.toString)
        val fromDefault = defaultInstrument.flatMap((v) => Option(v.iSoundEvent())).map(_.toString)
        val instruments = fromBlocks.toSet ++ fromMaterials ++ fromDefault

        new InstrumentRegistry(defaultInstrument, blocks, materials, instruments)
      }
      catch {
        case e: Exception =>
          NoteBetter.instance.log.error("Error decoding config file", e)
          new InstrumentRegistry(None, Map.empty, List.empty, Set.empty)
      }
    }

    private def toSoundObject(instrument: NoteBetterInstrument): JsonObject = {
      val jsonObject: JsonObject = new JsonObject
      jsonObject.add("name", Option(instrument.iSoundEvent()).map((x) => {
        new JsonPrimitive(x.toString)
      }).orNull)
      jsonObject.addProperty("volume", instrument.volume())
      jsonObject
    }

    override def serialize(config: InstrumentRegistry, typeOfSrc: java.lang.reflect.Type, context: JsonSerializationContext): JsonElement = {
      // TODO 1.2+
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
      new JsonObject
    }
  }

}
