package me.gamenu.carbondf.blocks;

public enum Target {
    SELECTION("Selection"),
    DEFAULT("Default"),
    KILLER("Killer"),
    DAMAGER("Damager"),
    VICTIM("Victim"),
    SHOOTER("Shooter"),
    PROJECTILE("Projectile"),
    LAST_ENTITY("LastEntity")
    ;
    private final String id;

    Target(String id){
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
