package com.github.soniex2.notebetter;

import com.github.soniex2.notebetter.config.NoteBetterNoteConfig;
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
    private static NoteBlockEvent.Instrument instrumentFromString(String instrument) {
        if (instrument.startsWith("minecraft:note.")) {
            String name = instrument.substring(15); // skip above prefix
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

    private static String instrumentToString(NoteBlockEvent.Instrument instrument) {
        switch (instrument) {
            default:
                return "minecraft:note.harp";
            case BASSDRUM:
                return "minecraft:note.bd";
            case SNARE:
                return "minecraft:note.snare";
            case CLICKS:
                return "minecraft:note.hat";
            case BASSGUITAR:
                return "minecraft:note.bassattack";
        }
    }

    private static void playNote(World world, BlockPos pos, String instrument, int note) {
        NoteBlockEvent.Instrument vanillaInstrument = instrumentFromString(instrument);
        if (vanillaInstrument != null) {
            NoteBlockEvent.Play e = new NoteBlockEvent.Play(world, pos, world.getBlockState(pos), note, vanillaInstrument.ordinal());
            if (MinecraftForge.EVENT_BUS.post(e)) return;
            vanillaInstrument = e.instrument;
            instrument = instrumentToString(vanillaInstrument);
            note = e.getVanillaNoteId();
        }
        float pitch = (float) Math.pow(2.0D, (note - 12) / 12.0D);
        world.playSoundEffect(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, instrument, 3.0F, pitch);
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
            if (NoteBetter.instance.defaultConfig.blocks.containsKey(rl.toString())) {
                String sound = NoteBetter.instance.defaultConfig.blocks.get(rl.toString());
                if (sound == null) return true; // don't play anything
                playNote(world, pos, sound, te.note);
                return true;
            }
            /* Then we do materials */
            Material m = world.getBlockState(pos.down()).getBlock().getMaterial();
            for (NoteBetterNoteConfig.MaterialSound ms : NoteBetter.instance.defaultConfig.materials) {
                if (ms.material_of == null) continue;
                if (Block.getBlockFromName(ms.material_of).getMaterial() == m) {
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
