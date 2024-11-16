package me.gamenu.carbondf.values;

import me.gamenu.carbondf.exceptions.TypeException;
import org.json.JSONObject;

public class DFVariable extends DFItem {

    /**
     * Name of the variable
     */
    private final String name;

    /**
     * Scope of the variable
     */
    private final Scope scope;

    /**
     * Type of the current variable's value
     */
    private Type valueType;

    /**
     * Indicates whether this variable is a constant, strongly typed, or weakly typed.
     */
    private final VarKind varKind;

    /**
     * Create a new constant variable
     * @param name name of the constant
     * @param scope scope of the constant
     * @param type type of the constant
     * @return the newly created variable
     */
    public static DFVariable constant(String name, Scope scope, Type type) {
        return new DFVariable(name, scope, type, VarKind.CONSTANT);
    }

    /**
     * Create a new type-checked (strongly typed) variable
     * @param name name of the variable
     * @param scope scope of the variable
     * @param type type of the variable
     * @return the newly created variable
     */
    public static DFVariable typed(String name, Scope scope, Type type) {
        return new DFVariable(name, scope, type, VarKind.TYPED);
    }

    /**
     * Instantiate a new dynamic variable
     * @param name name of the variable
     * @param scope scope of the variable
     */
    public static DFVariable dynamic(String name, Scope scope) {
        return dynamic(name, scope, null);
    }

    public static DFVariable dynamic(String name, Scope scope, Type type) {
        return new DFVariable(name, scope, type, VarKind.DYNAMIC);
    }

    public DFVariable(String name, Scope scope, Type type, VarKind kind) {
        super(Type.VARIABLE);
        this.name = name;
        this.scope = scope;
        this.valueType = type;
        this.varKind = kind;
    }

    public String getName() {
        return name;
    }

    public Scope getScope() {
        return scope;
    }

    /**
     * Set the variable's internal value. This is NOT the same as the SetVariable DiamondFire Code Block, and will not create a corresponding CodeBlock either.
     * @param newValue internal value to set
     * @return this
     */
    public DFVariable setValue(DFItem newValue) {
        if (varKind == VarKind.CONSTANT)
            throw new TypeException("Cannot set a value on a constant variable");

        if (varKind == VarKind.TYPED && valueType != Type.ANY) {
            if (valueType != newValue.getType())
                throw new TypeException("Cannot assign value of type " + newValue.getType() + " to a variable of type " + valueType);
        }

        if (varKind != VarKind.TYPED)
            this.valueType = newValue.getType();
        return this;
    }

    public Type getValueType() {
        return valueType;
    }

    @Override
    public JSONObject toJSON() {
        return createJSONFromData(
                new JSONObject()
                        .put("name", name)
                        .put("scope", scope.getId())
        );
    }

    public enum Scope {

        GLOBAL("unsaved"),
        SAVED("saved"),
        LOCAL("local"),
        LINE("line")
        ;

        private final String id;

        Scope(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }
    }

    public enum VarKind {
        DYNAMIC,
        TYPED,
        CONSTANT
    }
}
