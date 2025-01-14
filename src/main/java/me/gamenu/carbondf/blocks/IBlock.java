package me.gamenu.carbondf.blocks;

public interface IBlock extends TemplateValue {
    BlockType getBlock();

    enum Attribute {
        NOT("NOT"),
        LS_CANCEL("LS-CANCEL");

        final String id;
        Attribute(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }
    }
}
