package com.github.soniex2.notebetter.note;

import com.github.soniex2.notebetter.api.NoteBetterInstrument;
import com.google.common.base.Predicate;
import net.minecraft.block.state.IBlockState;

import java.util.ArrayList;
import java.util.List;

/**
 * @author soniex2
 */
public class InstrumentBlock {
    private List<Predicate<IBlockState>> predicates = new ArrayList<Predicate<IBlockState>>();
    private List<NoteBetterInstrument> instruments = new ArrayList<NoteBetterInstrument>();
    private NoteBetterInstrument fallback = null;

    public void addPredicate(Predicate<IBlockState> predicate, NoteBetterInstrument instrument) {
        predicates.add(predicate);
        instruments.add(instrument);
    }

    // badly named, but will do
    public void setDefaultInstrument(NoteBetterInstrument instrument) {
        this.fallback = instrument;
    }

    public NoteBetterInstrument get(IBlockState state) {
        for (int i = 0; i < predicates.size(); i++) {
            if (predicates.get(i).apply(state)) {
                return instruments.get(i);
            }
        }
        return fallback;
    }
}
