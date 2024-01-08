package ast;
import environment.*;
import emitter.Emitter;

/**
 * BinOp has arithmetic and conditional operations
 *
 * @author Leo Tuckey
 * @version March 25, 2022
 */
public class BinOp implements Expression
{
    // instance variables - replace the example below with your own
    private Expression val1;
    private Expression val2;
    private String operator;

    /**
     * Makes a BinOp object that will evaluate
     * 
     * @param val1 first expression
     * @param val2 second expression
     * @param operator given operator
     */
    public BinOp(Expression val1, Expression val2, String operator)
    {
        this.val1 = val1;
        this.val2 = val2;
        this.operator = operator;
    }

    /**
     * Makes a BinOp object that will evaluate
     * 
     * @param val1 first expression
     * @param operator given operator
     * @param val2 second expression
     */
    public BinOp(Expression val1, String operator, Expression val2)
    {
        this(val1, val2, operator);
    }

    /**
     * Evaluates a BinOp
     *
     * @param env the environment which will be the source of variables
     * @return the evaluated result
     */
    public Object eval(Environment env)
    {
        if(operator.equals("+"))
        {
            return (int) val1.eval(env) + (int) val2.eval(env);
        }
        else if(operator.equals("-"))
        {
            return (int) val1.eval(env) - (int) val2.eval(env);
        }
        else if(operator.equals("/"))
        {
            return (int) val1.eval(env) / (int) val2.eval(env);
        }
        else if(operator.equals("*"))
        {
            return (((int) val1.eval(env)) * (int) (val2.eval(env)));
        }
        else if((operator.equals("%") || operator.equals("MOD")))
        {
            return (int) val1.eval(env) % (int) val2.eval(env);
        }
        else if(operator.equals(">"))
        {
            return (int) val1.eval(env) > (int) val2.eval(env);
        }
        else if(operator.equals("<"))
        {
            return (int) val1.eval(env) < (int) val2.eval(env);
        }
        else if(operator.equals(">="))
        {
            return (int) val1.eval(env) >= (int) val2.eval(env);
        }
        else if(operator.equals("<="))
        {
            return (int) val1.eval(env) <= (int) val2.eval(env);
        }
        else if(operator.equals("<>"))
        {
            return (int) val1.eval(env) != (int) val2.eval(env);
        }
        else if(operator.equals("=="))
        {
            return (int) val1.eval(env) == (int) val2.eval(env);
        }
        else if(operator.equals("&&"))
        {
            return (boolean) val1.eval(env) && (boolean) val2.eval(env);
        }
        else if(operator.equals("||"))
        {
            return (boolean) val1.eval(env) || (boolean) val2.eval(env);
        }
        else
            throw new IllegalArgumentException("Operator '" + operator + "' " +
                "not recognized!");
    }

    public void compile(Emitter e)
    {
        val1.compile(e);
        e.emitPush("v0");
        val2.compile(e);
        e.emitPop("t0");
        if(operator.equals("+"))
        {
            e.emit("addu $v0, $t0, $v0");
        }
        else if(operator.equals("*"))
        {
            e.emit("mul $v0, $t0, $v0");
        }
        else if(operator.equals("/"))
        {
            e.emit("div $v0, $t0, $v0");
        }
        else if(operator.equals("-"))
        {
            e.emit("subu $v0, $t0, $v0");
        }
        else
        {
            throw new IllegalArgumentException("Operator '" + operator + "' " +
                    "not recognized!");
        }
    }
}
