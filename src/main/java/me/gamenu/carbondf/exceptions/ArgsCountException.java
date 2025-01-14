package me.gamenu.carbondf.exceptions;

import me.gamenu.carbondf.blocks.IBlock;

public class ArgsCountException extends CarbonRuntimeException {
    public ArgsCountException() {
    }

    public ArgsCountException(String message) {
        super(message);
    }

    public ArgsCountException(String message, int blockIndex, IBlock block) {
        super(message, blockIndex, block);
    }

    public ArgsCountException(String message, int blockIndex, IBlock block, Throwable cause) {
        super(message, blockIndex, block, cause);
    }

    public ArgsCountException(String message, Throwable cause) {
        super(message, cause);
    }

    public ArgsCountException(Throwable cause) {
        super(cause);
    }
}
