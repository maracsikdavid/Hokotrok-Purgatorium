import cli.Parser;
import java.util.Scanner;

/**
 * A program belépési pontját tartalmazó osztály.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("Choose execution mode: [0] Test  [1] Game");
        Scanner scanner = new Scanner(System.in);
        int mode = scanner.nextInt();
        scanner.nextLine();
        
        Parser parser = new Parser(mode);
        
        System.out.println("INPUT:");
        while(scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if(line.equals("exit"))
                break;
            
            parser.parseLine(line); 
        }
    }
}