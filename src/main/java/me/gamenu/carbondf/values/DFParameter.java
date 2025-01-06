package me.gamenu.carbondf.values;

import me.gamenu.carbondf.exceptions.DuplicateEntryException;
import me.gamenu.carbondf.exceptions.InvalidFieldException;
import me.gamenu.carbondf.exceptions.TypeException;
import org.json.JSONObject;

/**
 * This class represents a DiamondFire Parameter item.
 * Due to the complexity of this class, creating a new instance of DFParameter will be done using the
 * {@link DFParameter.Builder} class instead of {@link DFParameter#DFParameter}
 */
public class DFParameter extends DFItem {
    

    /**
     * Allows for building a DF Parameter
     */
    public static class Builder {

        private static final TypeSet INVALID_DEFAULT_VALUE_TYPES = new TypeSet(Type.LIST, Type.DICT, Type.VARIABLE);
        private static final TypeSet VALID_PARAM_TYPES = new TypeSet(
                Type.ANY,
                Type.VARIABLE,
                Type.ITEM,
                Type.STRING,
                Type.STYLED_TEXT,
                Type.NUMBER,
                Type.LOCATION,
                Type.VECTOR,
                Type.SOUND,
                Type.PARTICLE,
                Type.POTION,
                Type.LIST,
                Type.DICT,
                Type.NONE
        );

        /** The name of the parameter */
        String name;

        /** The type of the parameter */
        TypeSet paramTypes;

        /** The description of the parameter */
        String description;

        /** Whether this variable contains multiple values. */
        boolean plural;

        /** Whether this parameter is optional */
        boolean optional;

        /**
         * Whether this is a return parameter, and not a normal parameter.
         * Return parameter are of type VAR, and the variable's type would be the given type
         */
        boolean returned;

        /**
         * Whether this parameter should be type checked (Strongly typed).
         * This value affects whether the variable built from this parameter will be strongly typed as well.
         */
        boolean typeChecked;

        /**
         * Parameter's default value
         * Only available for optional, non-plural parameters, that aren't a complex type.
         */
        DFItem defaultValue;

        /**
         * The Parameter's VarManager to add the generated variable to.
         * Will not be added to the final parameter.
         */
        VarManager vm;

        /**
         * Creates a new (unchecked) Parameter Builder
         * @param vm VarManager to add the matching variable to
         * @param name Name of the parameter
         */
        public Builder(VarManager vm, String name) {
            this(vm, name, (TypeSet) null);
        }

        /**
         * Creates a new Parameter Builder
         * @param vm VarManager to add the matching variable to
         * @param name Name of the parameter
         * @param type Type of the parameter
         */
        public Builder(VarManager vm, String name, Type type) {
            this(vm, name, new TypeSet(type));
        }

        /**
         * Creates a new Parameter Builder
         *
         * @param name Name of the parameter
         * @param types Types of the parameter (set null for a non type-checked parameter)
         */
        public Builder(VarManager vm, String name, TypeSet types) {
            // Make sure we have a valid name
            if (name == null || name.isBlank()) throw new InvalidFieldException("Name cannot be empty!");


            // Make sure we have a valid type
            TypeSet paramTypes = types == null ? new TypeSet(Type.ANY) : types;

            // Make sure to prevent the user from initializing a VARIABLE-type param.
            // We want to use the returnValue system instead to enforce strong typing when necessary.
            /* if (paramTypes.contains(Type.VARIABLE))
             *    throw new InvalidFieldException("Cannot set the type to VARIABLE! Please use Parameter.Builder.setReturnParam() instead.");
             */

            paramTypes.forEach(type -> {
                if (!VALID_PARAM_TYPES.contains(type))
                    throw new InvalidFieldException("Cannot set the parameter type to " + type);
            });

            this.vm = vm;
            this.name = name;
            this.paramTypes = types;
            this.plural = false;
            this.optional = false;
            this.returned = false;
            // Set whether this parameter and the subsequent variable should be type-checked
            this.typeChecked = !(types == null);
            this.defaultValue = null;
        }

        /**
         * Change the name of the Parameter
         *
         * @param name Parameter new name
         * @return this
         */
        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        /**
         * Change the description for this parameter
         *
         * @param desc The new Description for the parameter
         * @return this
         */
        public Builder setDescription(String desc) {
            this.description = desc;
            return this;
        }

        /**
         * Set whether the parameter is plural (can get multiple values)
         *
         * @param plural Whether this parameter is plural
         * @return this
         */
        public Builder setPlural(boolean plural) {
            this.plural = plural;
            return this;
        }

        /**
         * A convenience method. Same as <code>Builder.setPlural(true);</code>
         *
         * @return this
         */
        public Builder plural() {
            return this.setPlural(true);
        }

        /**
         * Set whether the parameter is optional (can get no values)
         *
         * @param optional Whether this parameter is optional
         * @return this
         */
        public Builder setOptional(boolean optional) {
            this.optional = optional;
            return this;
        }

        /**
         * A convenience method. Same as <code>Builder.setOptional(true);</code>
         *
         * @return this
         */
        public Builder optional() {
            return this.setOptional(true);
        }

        /**
         * Set the default value for this parameter<br/>
         * Can only be used with optional, non-plural parameters
         *
         * @param value value to set
         * @return this
         */
        public Builder setDefaultValue(DFItem value) {
            this.defaultValue = value;
            return this;
        }

        /**
         * Set whether this parameter is a return parameter instead of an input one
         *
         * @param returned Whether this parameter is a return parameter
         * @return this
         */
        public Builder setReturned(boolean returned) {
            this.returned = returned;
            return this;
        }

        public DFParameter build() {
            // Make sure the parameter is valid for a default value
            if (this.defaultValue != null) {

                if (this.returned)
                    throw new InvalidFieldException("Returned parameters cannot have a default value");

                // Check if the param's type is qualified for a default value at all
                if (INVALID_DEFAULT_VALUE_TYPES.stream().anyMatch(this.paramTypes::contains))
                    throw new InvalidFieldException("Default values cannot be added to type " + this.paramTypes);

                // Check if the param is optional AND not plural
                if (!this.optional || this.plural)
                    throw new InvalidFieldException("Default values can only be added to optional, non-plural parameters");

                if (!paramTypes.canAcceptType(defaultValue.getType()))
                    throw new TypeException("Parameter type " + paramTypes + " cannot accept default value of type " + defaultValue.getType());
            }

            // Check whether this is a valid return param.
            // Return params must be singular and required
            if (this.returned && (this.plural || this.optional)) {
                throw new InvalidFieldException("Returned parameters cannot be optional or plural");
            }

            if (vm.has(name)) {
                throw new DuplicateEntryException(String.format("Variable name \"%s\" already exists", name));
            }

            if (typeChecked) {
                vm.typed(name, DFVariable.Scope.LINE, paramTypes);
            } else {
                vm.dynamic(name, DFVariable.Scope.LINE);
            }

            return new DFParameter(
                    name,
                    description,
                    vm.get(name),
                    paramTypes,
                    plural,
                    optional,
                    returned,
                    typeChecked,
                    defaultValue
            );
        }


    }

    String name;
    String description;
    TypeSet paramTypes;

    boolean plural;
    boolean optional;
    boolean returned;
    boolean typeChecked;

    DFItem defaultValue;

    /**
     * This saves the instance of the variable matching the parameter,
     * so that {@link DFParameter#getVariable()} always returns the same instance
     */
    DFVariable matchingVar;

    /**
     * This is an internal constructor for the DFParameter class.
     * Please do not use this constructor outside of {@link DFParameter.Builder}, to prevent illegal DFParameter states.
     * @param name Name of the DFParameter
     * @param description Description for the DFParameter
     * @param types Types of the DFParameter
     * @param plural Whether the DFParameter can contain more than one value
     * @param optional Whether the DFParameter can contain no value at all
     * @param returned Whether the DFParameter is "returned" - takes in a Variable and sets its value inside the function.<br/>
     *                 Used only in CarbonDF
     * @param typeChecked Whether the DFParameter (and its subsequent DFVariable value) is strongly typed.<br/>
     *                    Used only in CarbonDF
     * @param defaultValue The default value of the DFParameter, in case it is optional and did not receive a value.
     */
    protected DFParameter(
            String name,
            String description,
            DFVariable matchingVar,
            TypeSet types,
            boolean plural,
            boolean optional,
            boolean returned,
            boolean typeChecked,
            DFItem defaultValue
    ) {
        super(Type.PARAMETER);
        this.name = name;
        this.description = description;
        this.matchingVar = matchingVar;
        this.paramTypes = types;
        this.plural = plural;
        this.optional = optional;
        this.returned = returned;
        this.typeChecked = typeChecked;
        this.defaultValue = defaultValue;
    }

    /**
     * Returns the matching LINE variable built for this parameter
     *
     * @return the matching variable for this parameter
     */
    public DFVariable getVariable() {
        return matchingVar;
    }

    /**
     * Get the parameter's type
     * @return the parameter's current type
     */
    public TypeSet getParamTypes() {
        return paramTypes;
    }

    /**
     * <p>
     *   Get the parameter's real type. This is the type that will be on DiamondFire, without Carbon's abstractions
     * </p>
     * <p>
     *   For example, a returned parameter's real type will be {@link DFItem.Type#VARIABLE VARIABLE}
     * </p>
     * @return the parameter's current real type
     */
    public Type getParamRealType() {
        if (returned) return Type.VARIABLE;
        if (paramTypes.size() == 1) return paramTypes.stream().toList().get(0);
        return Type.ANY;
    }

    /**
     * Whether this can accept the values of a certain item
     * @param other item to check for
     * @return whether the other item is acceptable
     */
    public boolean canAcceptItem(DFItem other) {
        // Return parameters may only accept variables
        if (this.returned) return other.getRealType() == Type.VARIABLE;

        // Check if the other item is simply acceptable
        return this.getParamTypes().canAcceptType(other.getType());
    }

    @Override
    public JSONObject toJSON() {
        JSONObject data = new JSONObject()
                .put("name", name)
                .put("description", description)
                .put("plural", plural)
                .put("optional", optional)
                .put("type", getParamRealType().getId());

        if (defaultValue != null)
            data.put("default_value", defaultValue.toJSON());

        return createJSONFromData(data);
    }
}
