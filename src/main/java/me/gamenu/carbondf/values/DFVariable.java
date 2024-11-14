package me.gamenu.carbondf.values;

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
     * Current value of the variable
     */
    private DFItem value;

    private Type type;

    /**
     * Whether this variable's type is enforced (strongly typed)
     */
    private final VarKind varKind;

    /**
     * Create a new constant variable
     * @param name name of the constant
     * @param scope scope of the constant
     * @param value value of the constant
     * @return the newly created variable
     */
    public static DFVariable constant(String name, Scope scope, DFItem value) {
        return new DFVariable(name, scope, value, VarKind.CONSTANT);
    }

    /**
     * Create a new type-checked (strongly typed) variable
     * @param name name of the variable
     * @param scope scope of the variable
     * @param value value of the variable
     * @return the newly created variable
     */
    public static DFVariable typed(String name, Scope scope, DFItem value) {
        return new DFVariable(name, scope, value, VarKind.TYPED);
    }

    /**
     * Instantiate a new mutable variable with a name
     * @param name name of the variable to be created
     */
    public static DFVariable mutable(String name) {
        return mutable(name, Scope.GLOBAL);
    }

    /**
     * Instantiate a new mutable variable
     * @param name name of the variable
     * @param scope scope of the variable
     */
    public static DFVariable mutable(String name, Scope scope) {
        return mutable(name, scope, null);
    }

    public static DFVariable mutable(String name, Scope scope, DFItem value) {
        return new DFVariable(name, scope, value, VarKind.MUTABLE);
    }

    private DFVariable(String name, Scope scope, DFItem value, VarKind kind) {
        super(Type.VARIABLE);
        this.name = name;
        this.scope = scope;
        this.value = value;
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
     * @return
     */
    public DFVariable setValue(DFItem newValue) {
        this.value = newValue;
        return this;
    }

    public DFItem getValue() {
        return value;
    }

    public Type getValueType() {
        return value.getType();
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
        MUTABLE,
        TYPED,
        CONSTANT
    }
}
