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
     * Type(s) of the current variable
     */
    private final TypeSet valueTypes;

    /**
     * Internal value of the variable
     */
    private DFItem internalValue;

    /**
     * Indicates whether this variable is a constant, strongly typed, or weakly typed.
     */
    private final VarKind varKind;

    /**
     * Create a new constant variable
     *
     * @param name  name of the constant
     * @param scope scope of the constant
     * @return the newly created variable
     */
    public static DFVariable constant(String name, Scope scope, DFItem value) {
        if (value.getType() == Type.VARIABLE && value instanceof DFVariable) {
            value = ((DFVariable) value).getValue();
        }
        return new DFVariable(name, scope, new TypeSet(value.getType()), VarKind.CONSTANT, value);
    }

    /**
     * Create a new type-checked (strongly typed) variable
     *
     * @param name  name of the variable
     * @param scope scope of the variable
     * @param types type of the variable
     * @return the newly created variable
     */
    public static DFVariable typed(String name, Scope scope, TypeSet types) {
        return new DFVariable(name, scope, types, VarKind.TYPED, null);
    }

    /**
     * Instantiate a new dynamic variable
     *
     * @param name  name of the variable
     * @param scope scope of the variable
     */
    public static DFVariable dynamic(String name, Scope scope) {
        return new DFVariable(name, scope, new TypeSet(), VarKind.DYNAMIC, null);
    }

    /**
     * The default constructor for DFVariable.<br/>
     * In general, it is recommended to instead use the following methods:<br/><br/>
     * Dynamic variables: {@link DFVariable#dynamic(String name, Scope scope)}<br/>
     * Typed variables: {@link DFVariable#typed(String name, Scope scope, TypeSet types)}<br/>
     * Constants: {@link DFVariable#constant(String name, Scope scope, DFItem value)}<br/>
     *
     * @param name          Name of the variable
     * @param scope         Scope of the variable
     * @param types         Valid variable types (for typed values)
     * @param kind          Variable kind
     * @param internalValue initial, internal value of the variable
     */
    private DFVariable(String name, Scope scope, TypeSet types, VarKind kind, DFItem internalValue) {
        super(Type.VARIABLE);
        this.name = name;
        this.scope = scope;
        this.varKind = kind;

        if (types == null || types.isEmpty())
            this.valueTypes = new TypeSet(Type.ANY);
        else
            this.valueTypes = types;

        this.internalValue = internalValue;
    }

    public String getName() {
        return name;
    }

    public Scope getScope() {
        return scope;
    }

    /**
     * <p>Set the variable's internal value.</p>
     * <p>This is NOT the same as the SetVariable DiamondFire Code Block,
     * and will not create a corresponding CodeBlock either.</p>
     *
     * @param valueToSet internal value to set
     * @return this
     */
    public DFVariable setValue(DFItem valueToSet) {
        if (varKind == VarKind.CONSTANT)
            throw new TypeException("Cannot set a value on a constant variable");

        TypeSet newTypes;
        if (valueToSet.getType() == Type.VARIABLE
                && valueToSet instanceof DFVariable varToSet) {
            // Set the new value to the variable's internal value
            valueToSet = varToSet.getValue();

            // If the variable's value is null, we'll use its static TypeSet.
            // If it does have a set value, we'll use its set value instead.
            // Variable's may be declared but not have a value during compile, for example, with parameters
            newTypes = (valueToSet == null) ?
                    varToSet.getValueTypes() :
                    new TypeSet(varToSet.getRealValueType());

        } else {
            // Set the currently set type to the currently set (runtime) type
            newTypes = new TypeSet(valueToSet.getType());
        }

        if (varKind == VarKind.TYPED && !valueTypes.contains(Type.ANY)) {
            TypeSet common = new TypeSet(valueTypes);

            common.retainAll(newTypes);
            if (common.isEmpty()) {
                throw new TypeException("Cannot assign value of type(s) " + newTypes + " to a variable \"" + name + "\" of type(s) " + valueTypes);
            }
        }


        if (varKind != VarKind.TYPED)
            // This is probably a bad idea
            // TODO: Remove adding things to the TypeUnion at all
            this.valueTypes.addAll(newTypes);

        this.internalValue = valueToSet;

        return this;
    }

    /**
     * Get the variable's possible types
     *
     * @return the variable's possible types
     */
    public TypeSet getValueTypes() {
        return valueTypes;
    }

    public DFItem getValue() {
        return internalValue;
    }

    /**
     * Returns the variable's current value
     *
     * @return variable's current value OR {@link Type#NONE} if there is no internalValue
     */
    public Type getRealValueType() {
        if (internalValue == null) return Type.NONE;
        return internalValue.getType();
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
        LINE("line");

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
