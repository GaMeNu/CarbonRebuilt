package me.gamenu.carbondf.blocks;

import me.gamenu.carbondf.code.Template;
import me.gamenu.carbondf.exceptions.InvalidFieldException;

public interface IDataBlock extends IArgsBlock {
    String getName();

    default void verifyNamedBlocks(String name) {
        boolean actionExists = false;

        for (Template.TemplateType tt : Template.TemplateType.values()) {
            if (ActionType.getByName(tt.getBlockType(), name) != null)
                actionExists = true;
        }

        if (actionExists)
            throw new InvalidFieldException("Named block cannot use Action name");
    }
}
