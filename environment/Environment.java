package environment;

import java.util.HashMap;
import java.util.Map;

import ast.ProcedureDeclaration;

import java.util.ArrayList;

/**
 * Environment of variables
 *
 * @author Leo Tuckey
 * @version March 25, 2022
 */
public class Environment implements Cloneable
{
    // instance variables - replace the example below with your own
    private HashMap<String, Object> variables;
    private ArrayList<ProcedureDeclaration> procedures;
    private Environment parent;

    /**
     * Constructor for objects of class Environment
     *
     * @param parent the parent environment
     */
    public Environment(Environment parent)
    {
        this(new HashMap<>(), new ArrayList<>());
        setParent(parent);
    }

    /**
     * Constructor for objects of class Environment
     *
     * @param procedures list of procedure declarations
     */
    public Environment(ArrayList<ProcedureDeclaration> procedures)
    {
        this();
        setProcedures(procedures);
    }

    /**
     * Constructor for objects of class Environment
     *
     * @param variable  hashmap of variables
     * @param procedure list of procedure declarations
     * @param env       used environment
     */
    public Environment(HashMap<String, Object> variable, ArrayList<ProcedureDeclaration> procedure,
                       Environment env)
    {
        this(variable, procedure);
        setParent(env);
    }

    public Environment(ArrayList<ProcedureDeclaration> newProcedures, ArrayList<String> vars)
    {
        this();
        setProcedures(newProcedures);
        vars.forEach(item -> variables.put(item, 0));
    }

    /**
     * Constructor for objects of class Environment
     */
    public Environment()
    {
        this(new HashMap<>(), new ArrayList<>());
    }

    /**
     * Constructor for objects of class Environment
     *
     * @param variables hashmap of variables
     * @param procedure list of procedure declarations
     */
    public Environment(HashMap<String, Object> variables,
                       ArrayList<ProcedureDeclaration> procedure)
    {
        this.variables = variables;
        this.setProcedures(procedure);
    }

    /**
     * Sets the given variable with the given value
     *
     * @param var the given variable
     * @param val the given value
     */
    public void setVariable(String var, Object val)
    {
        // put your code here
        variables.put(var, val);
    }

    /**
     * Gets the value of the given variable
     *
     * @param variable the given variable
     * @return the value of the given variable
     */
    public Object getVariable(String variable)
    {
        return variables.get(variable);
    }

    /**
     * Checks if there's a variable with the given name
     *
     * @param a the given name
     * @return true if there is a variable with the given name
     * otherwise, false
     */
    public boolean hasVariable(String a)
    {
        return getAllVariables().containsKey(a);
    }

    /**
     * Sets all procedures to the given procedures.
     *
     * @param procedures list of procedure declarations
     */
    public void setProcedures(ArrayList<ProcedureDeclaration> procedures)
    {
        this.procedures = procedures;
    }

    /**
     * Returns all procedures
     *
     * @return a list of all the procedure declarations
     */
    public ArrayList<ProcedureDeclaration> getProcedures()
    {
        return procedures;
    }

    /**
     * Gets all variables
     *
     * @return variables
     */
    public HashMap<String, Object> getAllVariables()
    {
        return variables;
    }

    /**
     * Sets the parent environment to the given environment
     *
     * @param parent the given environment
     */
    private void setParent(Environment parent)
    {
        this.parent = parent;
    }

    /**
     * Returns the parent environment
     *
     * @return the parent environment
     */
    public Environment getParent()
    {
        if (parent != null)
        {
            return parent;
        }
        return this;
    }

    /**
     * Returns global
     *
     * @return global
     */
    public Environment global()
    {
        if (parent != null)
            return parent.global();
        else
            return this;
    }

    /**
     * Adds a given procedure
     *
     * @param decl the given procedure's procedure declaration
     */
    public void addProcedure(ProcedureDeclaration decl)
    {
        procedures.add(decl);
    }

    /**
     * Checks if there is the given procedure
     *
     * @param called the called procedure
     * @return true if there is the given procedure
     * otherwise, false
     */
    public boolean hasProcedure(String called)
    {
        try
        {
            getSpecificProcedure(called);
        }
        catch (IllegalArgumentException e)
        {
            return false;
        }
        return true;
    }

    /**
     * Gets a specific procedure
     *
     * @param called the called procedure
     * @return the specific procedure
     */
    public ProcedureDeclaration getSpecificProcedure(String called)
    {
        for (int i = 0; i < getProcedures().size(); i++)
        {
            if (getProcedures().get(i).getName().equals(called))
                return getProcedures().get(i);
        }
        throw new IllegalArgumentException("No procedure with name " + called + " found.");
    }

    /**
     * Clones
     *
     * @return new environment
     */
    @Override
    public Environment clone()
    {
        return new Environment(new HashMap<>(getAllVariables()),
                (ArrayList<ProcedureDeclaration>) getProcedures().clone(),
                this);
    }

    /**
     * Finds program in string form
     *
     * @return the string form of the program
     */
    @Override
    public String toString()
    {
        return "Environment (" + getAllVariables().toString() + ")";
    }

    /**
     * Sets the global environment to the given global environment
     *
     * @param globalEnv the given global environment
     */
    public void setGlobalEnvironment(Environment globalEnv)
    {
        this.parent = globalEnv;
    }
}
