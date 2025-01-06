package me.gamenu.carbondf.values;

import me.gamenu.carbondf.exceptions.DuplicateEntryException;

import java.util.HashMap;
import java.util.Map;

public class VarManager {
    private final Map<String, DFVariable> varMap;

    public VarManager() {
        this.varMap = new HashMap<>();
    }

    private boolean checkVarMap(String name) {
        return !varMap.containsKey(name);
    }

    private DFVariable updateVarMap(DFVariable var) {
        if (!checkVarMap(var.getName()))
            throw new DuplicateEntryException("Variable with the name \"" + var.getName() + "\" already exists. Please use VarManager#get() to get the existing instance.");
        varMap.put(var.getName(), var);
        return var;
    }

    /**
     * <p>Create a new constant (immutable) variable.</p>
     * <p>For more info, check {@link me.gamenu.carbondf.values.DFVariable.VarKind}</p>
     *
     * @param name name of the new variable
     * @param scope scope of the new variable
     * @param value initial value of the new variable
     * @return new variable
     */
    public DFVariable constant(String name, DFVariable.Scope scope, DFItem value) {
        return updateVarMap(DFVariable.constant(name, scope, value));
    }

    /**
     * <p>Create a new typed (type checking enabled) variable.</p>
     * <p>For more info, check {@link me.gamenu.carbondf.values.DFVariable.VarKind}</p>
     *
     * @param name name of the new variable
     * @param scope scope of the new variable
     * @param types types of the new variable
     * @return the new variable
     */
    public DFVariable typed(String name, DFVariable.Scope scope, TypeSet types) {
        return updateVarMap(DFVariable.typed(name, scope, types));
    }

    /**
     * <p>Create a new typed (type checking enabled) variable.</p>
     * <p>For more info, check {@link me.gamenu.carbondf.values.DFVariable.VarKind}</p>
     *
     * @param name name of the new variable
     * @param scope scope of the new variable
     * @param type type of the new variable
     * @return the new variable
     */
    public DFVariable typed(String name, DFVariable.Scope scope, DFItem.Type type) {
        return updateVarMap(DFVariable.typed(name, scope, type));
    }

    /**
     * <p>Create a new dynamic (type checking disabled) variable.</p>
     * <p>For more info, check {@link me.gamenu.carbondf.values.DFVariable.VarKind}</p>
     * @param name name of the new variable
     * @param scope scope of the new variable
     * @return the new variable
     */
    public DFVariable dynamic(String name, DFVariable.Scope scope) {
        return updateVarMap(DFVariable.dynamic(name, scope));
    }

    /**
     * Get a previously defined variable
     * @param name name of the variable to get
     * @return variable
     */
    public DFVariable get(String name) {
        return varMap.get(name);
    }

    /**
     * Check whether the variable was previously defined
     * @param name name of the variable to check
     * @return whether the variable was already created
     */
    public boolean has(String name) {
        return varMap.containsKey(name);
    }

    /**
     * Removes all {@link DFVariable.Scope#LINE} variables from the variable map.
     */
    public void clearLineScope() {
        varMap.entrySet().removeIf(entry -> entry.getValue().getScope() == DFVariable.Scope.LINE);
    }

    /**
     * Creates a new (unchecked) {@link DFParameter.Builder}
     * @param name Name of the new parameter
     * @return the new Parameter Builder
     */
    public DFParameter.Builder param(String name) {
        return new DFParameter.Builder(this, name);
    }

    /**
     * Creates a new {@link DFParameter.Builder}
     * @param name Name of the new parameter
     * @param type Type of the new parameter
     * @return the new Parameter Builder
     */
    public DFParameter.Builder param(String name, DFItem.Type type) {
        return new DFParameter.Builder(this, name, type);
    }

    /**
     * Creates a new {@link DFParameter.Builder}
     * @param name Name of the new parameter
     * @param types Types of the new parameter
     * @return the new Parameter Builder
     */
    public DFParameter.Builder param(String name, TypeSet types) {
        return new DFParameter.Builder(this, name, types);
    }
}
