package me.gamenu.carbondf.actions;

public enum TargetType {
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

    TargetType(String id){
        this.id = id;
    }
}
