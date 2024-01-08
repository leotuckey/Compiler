import java.io.*;
import java.util.regex.Pattern;
import java.nio.file.Files;
import java.io.InputStream;
import java.io.FileInputStream;
import scanner.ScanErrorException;
import scanner.Scanner;
/**
 * Tests nextToken method on text file. As long as it's not at
 * end of file, prints the current lexeme.
 * 
 * @author  Leo Tuckey
 *
 * @version February 1, 2022
 */
public class ScannerTest 
{
    /* ScannerTest methods: */

    /**
     * Tests nextToken method on text file. As long as it's not at
     * end of file, prints the current lexeme.
     * 
     * @param args arguments from the command line
     */
    public static void main(String[] args) throws IOException
    {
        //File test=new File("ScannerTest.txt");
        //FileInputStream file=new FileInputStream(test);
        File testTwo=new File("ScannerTestAdvanced.txt");
        FileInputStream fileTwo=new FileInputStream(testTwo);
        Scanner testFile = new Scanner(fileTwo);
        try
        {
            while(testFile.hasNext())
            {
                System.out.println(testFile.nextToken());
            }
        }
        catch(ScanErrorException e)
        {
            System.out.println(e);
        }
    }

}


