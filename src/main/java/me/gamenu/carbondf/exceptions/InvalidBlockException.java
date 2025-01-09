package me.gamenu.carbondf.exceptions;

import me.gamenu.carbondf.blocks.IBlock;

public class InvalidBlockException extends CarbonRuntimeException {
  public InvalidBlockException() {
  }

  public InvalidBlockException(String message) {
    super(message);
  }

  public InvalidBlockException(String message, int blockIndex, IBlock block) {
    super(message, blockIndex, block);
  }

  public InvalidBlockException(String message, int blockIndex, IBlock block, Throwable cause) {
    super(message, blockIndex, block, cause);
  }

  public InvalidBlockException(String message, Throwable cause) {
    super(message, cause);
  }

  public InvalidBlockException(Throwable cause) {
    super(cause);
  }
}
