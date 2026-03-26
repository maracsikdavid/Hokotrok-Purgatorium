package core;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * A szkeleton program központi naplózó és tesztelő osztálya. 
 * Felelős az objektumok azonosításáért, a metódushívások fa-struktúrájú 
 * megjelenítéséért a konzolon, valamint a Tesztelővel való interakcióért.
 */
public class Skeleton {
    private static int callDepth = 0;
    private static Scanner scanner = new Scanner(System.in);
    private static Map<Object, String> objectRegistry = new HashMap<>();

    /**
     * Beregisztrál egy objektumot egy egyedi névvel, amely a naplózás során fog megjelenni.
     *
     * @param obj  a regisztrálandó objektum
     * @param name az objektum megjelenítendő neve (pl. "c1", "sk")
     */
    public static void registerObject(Object obj, String name) {
        objectRegistry.put(obj, name);
    }

    /**
     * Visszaadja az objektum regisztrált nevét. Ha az objektum nincs regisztrálva, 
     * alapértelmezettként az osztályának rövid nevét adja vissza.
     *
     * @param obj a keresett objektum
     * @return    az objektum regisztrált vagy alapértelmezett neve
     */
    public static String getObjectName(Object obj) {
        return objectRegistry.getOrDefault(obj, obj.getClass().getSimpleName());
    }

    /**
     * Segédmetódus, amely a hívási mélységnek (callDepth) megfelelő számú 
     * behúzást ír ki a konzolra a vizuális fa-struktúra kialakításához.
     */
    private static void printIndent() {
        for (int i = 0; i < callDepth; i++) {
            System.out.print("│   ");
        }
    }

    /**
     * Naplózza egy metódushívás kezdetét, majd megnöveli a hívási mélységet.
     *
     * @param caller     a hívást kezdeményező objektum (ha null, akkor "Tester")
     * @param target     a meghívott (cél) objektum
     * @param methodName a meghívott metódus neve
     */
    public static void printCall(Object caller, Object target, String methodName) {
        printIndent();
        String callerName = (caller == null) ? "Tester" : getObjectName(caller);
        System.out.println("├── [" + callerName + "] -> [" + getObjectName(target) + "]." + methodName + "()");
        callDepth++;
    }

    /**
     * Naplózza egy metódushívás végét és visszatérési értékét, majd csökkenti a hívási mélységet.
     *
     * @param target      a meghívott objektum, ami visszatér
     * @param methodName  a metódus neve
     * @param returnValue a visszatérési érték szöveges formában (üres string esetén nem írja ki)
     */
    public static void printReturn(Object target, String methodName, String returnValue) {
        callDepth--;
        printIndent();
        System.out.println("└── [" + getObjectName(target) + "]." + methodName + "() returned" + (returnValue.isEmpty() ? "" : ": " + returnValue));
    }

    /**
     * Naplózza egy visszatérési érték nélküli (void) metódus végét.
     *
     * @param target     a meghívott objektum
     * @param methodName a metódus neve
     */
    public static void printReturn(Object target, String methodName) {
        printReturn(target, methodName, "");
    }

    /**
     * Konzolos kérdést tesz fel a Tesztelőnek, és addig vár, amíg egy érvényes 
     * egész számot nem kap válaszul (bolondbiztos bemenetkezelés).
     *
     * @param question a Tesztelőnek feltett kérdés szövege
     * @return         a Tesztelő által megadott egész szám (pl. 1 vagy 0)
     */
    public static int getIntFromUser(String question) {
        printIndent();
        System.out.print("<?> " + question + " ");
        while (!scanner.hasNextInt()) {
            scanner.next();
        }
        return scanner.nextInt();
    }
}