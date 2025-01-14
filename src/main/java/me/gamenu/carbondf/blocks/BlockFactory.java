package me.gamenu.carbondf.blocks;

public class BlockFactory {
    public static CodeBlock codeBlock(String blockID, String actionName) {
        return new CodeBlock(blockID, actionName);
    }

    public static CodeBlock codeBlock(ActionType action) {
        return new CodeBlock(action);
    }

    public static CodeBlock codeBlock(BlockType block, ActionType action) {
        return new CodeBlock(block, action);
    }

    public static DataBlock dataBlock(String blockID, String name) {
        return new DataBlock(blockID, name);
    }

    public static DataBlock dataBlock(BlockType block, String name) {
        return new DataBlock(block, name);
    }

    public static FuncBlock funcBlock(String name) {
        return new FuncBlock(name);
    }

    public static CallFuncBlock callFuncBlock(String name) {
        return new CallFuncBlock(name);
    }

}
