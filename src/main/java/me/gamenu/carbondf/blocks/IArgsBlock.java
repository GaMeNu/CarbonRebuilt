package me.gamenu.carbondf.blocks;

import me.gamenu.carbondf.values.DFVariable;

public interface IArgsBlock extends IBlock {
    IBlockArgs getArgs();

    IArgsBlock setTagOption(String name, String option);

    IArgsBlock setTagVariable(String name, DFVariable variable);

}
