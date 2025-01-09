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
    private TypeSet valueTypes;

    /**
     * Internal value of the variable
     */
    private Type runtimeType;

    /**
     * Indicates whether this variable is a constant, strongly typed, or weakly typed.
     */
    private final VarKind varKind;

    /**
     * Create a new constant variable
     *
     * @param name  name of the constant
     * @param scope scope of the constant
     * @param value initial value of the variable
     * @return the newly created variable
     */
    static DFVariable constant(String name, Scope scope, DFItem value) {
        Type varType;
        if (value.getRealType() == Type.VARIABLE && value instanceof DFVariable) {
            varType = ((DFVariable) value).getRuntimeType();
        } else if (value.getType().size() == 1) {
            // Attempt to get the value's type if it's only 1 long
            varType = value.getType().stream().toList().get(0);
        } else {
            varType = Type.ANY;
        }

        return new DFVariable(name, scope, new TypeSet(value.getType()), VarKind.CONSTANT, varType);
    }

    /**
     * <p>Create a new type-checked (strongly typed) variable</p>
     *
     * <p><b>NOTE:</b> In general, it is not recommended to mix the use of Typed and Dynamic variables,
     * as it may result in unexpected behavior when assigning from Dynamic to Typed variables.</p>
     *
     * @param name  name of the variable
     * @param scope scope of the variable
     * @param type type of the variable
     * @return the newly created variable
     */
    static DFVariable typed(String name, Scope scope, Type type) {
        return typed(name, scope, new TypeSet(type));
    }

    /**
     * <p>Create a new type-checked (strongly typed) variable</p>
     *
     * <p><b>NOTE:</b> In general, it is not recommended to mix the use of Typed and Dynamic variables,
     * as it may result in unexpected behavior when assigning from Dynamic to Typed variables.</p>
     *
     * @param name  name of the variable
     * @param scope scope of the variable
     * @param types type of the variable
     * @return the newly created variable
     */
    static DFVariable typed(String name, Scope scope, TypeSet types) {
        return new DFVariable(name, scope, types, VarKind.TYPED, null);
    }

    /**
     * Instantiate a new dynamic variable
     *
     * <p><b>NOTE:</b> In general, it is not recommended to mix the use of Typed and Dynamic variables,
     * as it may result in unexpected behavior when assigning from Dynamic to Typed variables.</p>
     *
     * @param name  name of the variable
     * @param scope scope of the variable
     */
    static DFVariable dynamic(String name, Scope scope) {
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
     * @param runtimeType initial, internal runtime type of the variable.
     *                         This attribute may be mutated in dynamic and typed (multi) variables
     */
    private DFVariable(String name, Scope scope, TypeSet types, VarKind kind, Type runtimeType) {
        super(Type.VARIABLE);

        this.name = name;
        this.scope = scope;
        this.varKind = kind;

        if (types == null || types.isEmpty())
            this.valueTypes = new TypeSet(Type.ANY);
        else
            this.valueTypes = types;

        this.runtimeType = runtimeType;
    }

    public String getName() {
        return name;
    }

    public Scope getScope() {
        return scope;
    }

    public Type getRuntimeType() {
        return runtimeType;
    }

    /**
     * Safely sets the Variable's runtime type.
     * @param typeToSet The Variable's type to set
     * @return the variable
     * @throws TypeException if the assignment is illegal
     */
    public DFVariable setValueType(Type typeToSet) {
        if (varKind == VarKind.CONSTANT) {
            throw new TypeException("Cannot set a value on a CONSTANT variable");
        }

        if (varKind == VarKind.TYPED && !valueTypes.contains(typeToSet)) {
            throw new TypeException("Cannot assign value of type " + typeToSet + " to variable \"" + name + "\" of types " + valueTypes);
        }

        this.runtimeType = typeToSet;

        // Make sure we do this, because now this dynamic variable contains these new types
        // (This will be used for type safety if we want to go from Dynamic to Typed, even though
        // the two should not be mixed together.)
        if (varKind == VarKind.DYNAMIC && !valueTypes.contains(typeToSet))
            this.valueTypes = new TypeSet(typeToSet);

        return this;
    }

    /**
     * Safely sets the variable's type according to a TypeSet of possible options.
     * A TypeSet of 1 item will also set the variable's runtime type.
     * @param typesToSet Variable's possible types
     * @return this
     * @throws TypeException if the assignment is illegal
     */
    public DFVariable setValueType(TypeSet typesToSet) {

        if (this.getVarKind() == VarKind.CONSTANT) {
            throw new TypeException("Cannot set a value on a CONSTANT variable");
        }

        if (typesToSet.size() == 1) {
            // Attempt to operate on given wrapped type as if it were a RUNTIME type, since it's non-ambiguous
            return setValueType(typesToSet.stream().toList().get(0));
        }

        if (getVarKind() == VarKind.DYNAMIC) {
            this.runtimeType = null;
            this.valueTypes = typesToSet;
            return this;
        }

        if (getType().containsAll(typesToSet)) {
            // We just set our own runtime type to null, since we don't know what it really is,
            // so if we were to now assign THIS variable,
            // we would be operating on valueTypes instead of the (now non-existent) runtime type
            // (again, runtimeType should only be used when it is NOT ambiguous).
            this.runtimeType = null;
            return this;
        }
        throw new TypeException("Cannot guarantee type safety for assignment from types " + typesToSet
                + " to types " + name + valueTypes);
    }


    /**
     * <p>This method sets the "internal value" of this variable to the given value
     * (or its own internal value, in case of a variable).</p>
     *
     * <p>(In reality, this method only sets the runtime/possible types of this variable.
     * To follow the actual values, I'll have to just implement DF itself.)</p>
     *
     * @param value value to set
     * @return this
     * @throws TypeException if the assignment is illegal
     */
    public DFVariable setValue(DFItem value) {
        // we assume getType() does its magic CORRECTLY and gives us the type(s) of the value
        // (either runtime or possible, in case of variable (NOT the actual type of the value))
        return setValueType(value.getType());
    }

    /**
     * Get the variable's possible types, or the variable's current runtime type, if non-ambiguous.
     *
     * @return the variable's possible types
     */
    @Override
    public TypeSet getType() {
        if (runtimeType != null) {
            // Wrap the ACTUAL runtime type of the variable
            return new TypeSet(runtimeType);
        }
        return valueTypes;
    }

    /**
     * This function is guaranteed to return the possible types of the variable,
     * regardless of the current runtime type
     * @return the possible runtime types
     */
    public TypeSet getPossibleTypes() {
        return valueTypes;
    }

    /**
     * Gets the {@link VarKind Kind} of the Variable.
     * @return the Kind of the variable
     */
    public VarKind getVarKind() {
        return varKind;
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

    /**
     *
     * Specifies what Kind a DFVariable is.
     * <table>
     *     <caption>Summary of VarKind values</caption>
     *     <tr>
     *         <th>VarKind</th>
     *         <th>Type checked?</th>
     *         <th>Immutable?</th>
     *     </tr>
     *     <tr>
     *         <td>{@link VarKind#DYNAMIC}</td>
     *         <td>X</td>
     *         <td>X</td>
     *     </tr>
     *     <tr>
     *         <td>{@link VarKind#TYPED}</td>
     *         <td>V</td>
     *         <td>X</td>
     *     </tr>
     *     <tr>
     *         <td>{@link VarKind#CONSTANT}</td>
     *         <td>V</td>
     *         <td>V</td>
     *     </tr>
     * </table>
     */
    public enum VarKind {
        /** This Variable can be freely mutated. */
        DYNAMIC,
        TYPED,
        CONSTANT
    }
}
