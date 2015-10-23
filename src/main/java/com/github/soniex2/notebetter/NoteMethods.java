package com.github.soniex2.notebetter;

import com.github.soniex2.notebetter.config.NoteBetterNoteConfig;
import com.github.soniex2.notebetter.util.CachedResourceLocation;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntityNote;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.NoteBlockEvent;

/**
 * @author soniex2
 */
public class NoteMethods {
    private static NoteBlockEvent.Instrument instrumentFromResLoc(ResourceLocation instrument) {
        if (instrument.getResourceDomain().equals("minecraft") && instrument.getResourcePath().startsWith("note.")) {
            String name = instrument.getResourcePath().substring(5); // skip above prefix
            switch (name.charAt(0)) {
                case 'h':
                    if (name.equals("harp"))
                        return NoteBlockEvent.Instrument.PIANO;
                    else if (name.equals("hat"))
                        return NoteBlockEvent.Instrument.CLICKS;
                    break;
                case 'b':
                    if (name.equals("bd"))
                        return NoteBlockEvent.Instrument.BASSDRUM;
                    else if (name.equals("bassattack"))
                        return NoteBlockEvent.Instrument.BASSGUITAR;
                    break;
                case 's':
                    if (name.equals("snare"))
                        return NoteBlockEvent.Instrument.SNARE;
                    break;
            }
        }
        return null;
    }

    private static ResourceLocation instrumentToResLoc(NoteBlockEvent.Instrument instrument) {
        switch (instrument) {
            default:
                return new CachedResourceLocation("minecraft:note.harp");
            case BASSDRUM:
                return new CachedResourceLocation("minecraft:note.bd");
            case SNARE:
                return new CachedResourceLocation("minecraft:note.snare");
            case CLICKS:
                return new CachedResourceLocation("minecraft:note.hat");
            case BASSGUITAR:
                return new CachedResourceLocation("minecraft:note.bassattack");
        }
    }

    private static void playNote(World world, BlockPos pos, ResourceLocation instrument, int note) {
        NoteBlockEvent.Instrument vanillaInstrument = instrumentFromResLoc(instrument);
        if (vanillaInstrument != null) {
            NoteBlockEvent.Play e = new NoteBlockEvent.Play(world, pos, world.getBlockState(pos), note, vanillaInstrument.ordinal());
            if (MinecraftForge.EVENT_BUS.post(e)) return;
            vanillaInstrument = e.instrument;
            instrument = instrumentToResLoc(vanillaInstrument);
            note = e.getVanillaNoteId();
        }
        float pitch = (float) Math.pow(2.0D, (note - 12) / 12.0D);
        world.playSoundEffect(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, instrument.toString(), 3.0F, pitch);
        if (world instanceof WorldServer) // just in case it *isn't* being called from a WorldServer
            ((WorldServer) world).spawnParticle(EnumParticleTypes.NOTE, false, pos.getX() + 0.5D, pos.getY() + 1.2D, pos.getZ() + 0.5D, 0, ((double) note) / 24.0D, 0.0D, 0.0D, 1.0D);
    }

    public static boolean handleTileEntity(World world, BlockPos pos) {
        if (world.getBlockState(pos.up()).getBlock().getMaterial() == Material.air) {
            if (!(world.getTileEntity(pos) instanceof TileEntityNote)) return false;
            TileEntityNote te = ((TileEntityNote) world.getTileEntity(pos));
            /* First we do blocks */
            IBlockState bs = world.getBlockState(pos.down());
            ResourceLocation rl = (ResourceLocation) Block.blockRegistry.getNameForObject(bs.getBlock());
            if (NoteBetter.instance.defaultConfig.blocks.containsKey(rl)) {
                ResourceLocation sound = NoteBetter.instance.defaultConfig.blocks.get(rl);
                if (sound == null) return true; // don't play anything
                playNote(world, pos, sound, te.note);
                return true;
            }
            /* Then we do materials */
            Material m = world.getBlockState(pos.down()).getBlock().getMaterial();
            for (NoteBetterNoteConfig.MaterialSound ms : NoteBetter.instance.defaultConfig.materials) {
                if (ms.material_of == null) continue;
                // we already have a ResourceLocation, skip getBlockFromName.
                Block b = null;
                if (Block.blockRegistry.containsKey(ms.material_of)) {
                    b = (Block) Block.blockRegistry.getObject(ms.material_of);
                }
                if (b != null && b.getMaterial() == m) {
                    if (ms.sound == null) return true; // don't play anything
                    playNote(world, pos, ms.sound, te.note);
                    return true;
                }
            }
            /* Fallback to default */
            if (NoteBetter.instance.defaultConfig.base != null) {
                playNote(world, pos, NoteBetter.instance.defaultConfig.base, te.note);
                return true;
            }
        }
        return false;
    }
}
