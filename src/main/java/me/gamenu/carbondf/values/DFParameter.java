package me.gamenu.carbondf.values;

import me.gamenu.carbondf.exceptions.InvalidFieldException;
import me.gamenu.carbondf.exceptions.TypeMismatchException;
import org.json.JSONObject;

import java.util.Arrays;


public class DFParameter extends DFItem {
    

    /**
     * This class allows for building a DF Parameter
     */
    public static class Builder {

        private static final Type[] INVALID_DEFAULT_VALUE_TYPES = {Type.LIST, Type.DICT, Type.VARIABLE};
        private static final Type[] VALID_PARAM_TYPES = {
                Type.ANY,
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
                Type.DICT
        };

        /**
         * The name of the parameter
         */
        String name;

        /**
         * The type of the parameter
         */
        Type type;

        /**
         * The description of the parameter
         */
        String description;

        /**
         * Whether this variable contains multiple values.
         * If a param is plural, its type would be a LIST of the given type
         */
        boolean plural;

        /**
         * Whether this parameter is optional
         */
        boolean optional;

        /**
         * Whether this is a return parameter, and not a normal parameter.
         * Return parameter are of type VAR, and the VAR's type would be the given type
         */
        boolean returned;

        /**
         * Whether this parameter should be type checked (Strongly typed).
         * This value affects whether the variable built from this parameter will be strongly typed as well
         */
        boolean typeChecked;

        /**
         * Parameter's default value
         * Only available for optional, non-plural parameters, that aren't a complex type.
         */
        DFItem defaultValue;

        /**
         * Create a new Parameter.Builder
         *
         * @param name The name of the parameter
         * @param type The type of the parameter (leave for a non type-checked parameter)
         */
        public Builder(String name, Type type) {
            // Make sure we have a valid name
            if (name == null || name.isBlank()) throw new InvalidFieldException("Name cannot be empty!");


            // Make sure we have a valid type
            Type paramType = type == null ? Type.ANY : type;

            // Make sure to prevent the user from initializing a VARIABLE-type param.
            // We want to use the returnValue system instead to enforce strong typing when necessary.
            if (paramType == Type.VARIABLE)
                throw new InvalidFieldException("Cannot set the type to VARIABLE! Please use Parameter.Builder.setReturnParam() instead.");


            if (Arrays.stream(VALID_PARAM_TYPES).noneMatch(value -> value.equals(paramType)))
                throw new InvalidFieldException("Cannot set the parameter type to " + type);

            this.name = name;
            this.type = type;
            this.plural = false;
            this.optional = false;
            this.returned = false;
            // Set whether this parameter and the subsequent variable should be type-checked
            this.typeChecked = !(type == null);
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
                if (Arrays.stream(INVALID_DEFAULT_VALUE_TYPES).anyMatch(value -> value.equals(this.type)))
                    throw new InvalidFieldException("Default values cannot be added to type " + this.type);

                // Check if the param is optional AND not plural
                if (!this.optional || this.plural)
                    throw new InvalidFieldException("Default values can only be added to optional, non-plural parameters");

                if (!type.canAcceptType(defaultValue.getType()))
                    throw new TypeMismatchException("Parameter type " + type + " cannot accept default value of type " + defaultValue.getType());
            }

            if (this.returned && (this.plural || this.optional)) {
                throw new InvalidFieldException("Returned parameters cannot be optional or plural");
            }



            return new DFParameter(name, description, type, plural, optional, returned, defaultValue);
        }


    }

    String name;
    String description;
    Type type;

    boolean plural;
    boolean optional;
    boolean returned;
    boolean typeChecked;

    DFItem defaultValue;

    protected DFParameter(String name,
                          String description,
                          Type type,
                          boolean plural,
                          boolean optional,
                          boolean returned,
                          boolean typeChecked,
                          DFItem defaultValue
    ) {
        super(Type.PARAMETER);
        this.name = name;
        this.description = description;
        this.type = type;
        this.plural = plural;
        this.optional = optional;
        this.returned = returned;
        this.typeChecked = typeChecked;
        this.defaultValue = defaultValue;
    }

    /**
     * This method returns the parameter's matching variable.
     *
     * @implNote The returned variable will be value-less
     * @return
     */
    public DFVariable buildVariable() {

    }

    @Override
    public Type getType() {
        return type;
    }

    public Type getRealType() {
        if (returned) return Type.VARIABLE;
        return type;
    }

    @Override
    public JSONObject toJSON() {
        JSONObject data = new JSONObject()
                .put("name", name)
                .put("description", description)
                .put("type", getRealType().getId())
                .put("plural", plural)
                .put("optional", optional);

        if (defaultValue != null)
            data.put("default_value", defaultValue.toJSON());

        return createJSONFromData(data);
    }
}
