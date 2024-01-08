package emitter;

import java.io.*;

public class Emitter
{
    private PrintWriter out;
    private final String fileName;

    //creates an emitter for writing to a new file with given name
    public Emitter(String outputFileName)
    {
        fileName = outputFileName;
        try
        {
            out = new PrintWriter(new FileWriter(outputFileName), true);
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    //prints one line of code to file (with non-labels indented)
    public void emit(String code)
    {
        if (!code.endsWith(":"))
            code = "\t" + code;
        out.println(code);
    }

    //closes the file.  should be called after all calls to emit.
    public void close()
    {
        out.close();
    }

    private int labelCount = 0;

    public String nextLabelID()
    {
        labelCount++;
        return "label" + labelCount;
    }

    public String getFileName()
    {
        return fileName;
    }

    public void emitPush(String reg)
    {
        emit("subu $sp $sp 4");
        emit("sw $"+reg+" ($sp)");
    }

    public void emitPop(String reg)
    {
        emit("lw $"+reg+" ($sp)");
        emit("addu $sp $sp 4");
    }
}
