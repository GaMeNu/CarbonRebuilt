package me.gamenu.carbondf.exceptions;

import me.gamenu.carbondf.blocks.Block;
import me.gamenu.carbondf.blocks.CodeBlock;

public class CarbonRuntimeException extends RuntimeException {
    Block block;
    int blockIndex;

    public CarbonRuntimeException() {
    }

    public CarbonRuntimeException(String message) {
        this(message, -1, null);
    }

    public CarbonRuntimeException(String message, int blockIndex, Block block) {
        this(message, blockIndex, block, null);
    }

    public CarbonRuntimeException(String message, int blockIndex, Block block, Throwable cause) {
        super(message, cause);
        this.block = block;
        this.blockIndex = blockIndex;
    }

    public CarbonRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public CarbonRuntimeException(Throwable cause) {
        super(cause);
    }

    protected CarbonRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    @Override
    public String getMessage() {
        String msg = super.getMessage();
        // We don't have an index defined yet
        if (blockIndex == -1) {
            return msg;
        }

        msg += "\n at block " + blockIndex;
        // We don't have a Block defined yet
        if (block == null) {
            return msg;
        }

        msg += ": BlockID=\"" + block.getBlock().getId() + "\"";
        // We don't know if the Block has an Action yet (else block or action not defined)
        if (!(block instanceof CodeBlock cb) || cb.getAction() == null) {
            return msg;
        }

        msg += ", ActionName=\"" + cb.getAction().getName() + "\"";

        return msg;
    }
}
