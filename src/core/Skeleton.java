package core;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Skeleton {
    private static int callDepth = 0;
    private static Scanner scanner = new Scanner(System.in);
    private static Map<Object, String> objectRegistry = new HashMap<>();

    public static void registerObject(Object obj, String name) {
        objectRegistry.put(obj, name);
    }

    public static String getObjectName(Object obj) {
        return objectRegistry.getOrDefault(obj, obj.getClass().getSimpleName());
    }

    private static void printIndent() {
        for (int i = 0; i < callDepth; i++) {
            System.out.print("│   ");
        }
    }

    public static void printCall(Object caller, Object target, String methodName) {
        printIndent();
        String callerName = (caller == null) ? "Tester" : getObjectName(caller);
        System.out.println("├── [" + callerName + "] -> [" + getObjectName(target) + "]." + methodName + "()");
        callDepth++;
    }

    public static void printReturn(Object target, String methodName, String returnValue) {
        callDepth--;
        printIndent();
        System.out.println("└── [" + getObjectName(target) + "]." + methodName + "() returned" + (returnValue.isEmpty() ? "" : ": " + returnValue));
    }

    public static void printReturn(Object target, String methodName) {
        printReturn(target, methodName, "");
    }

    public static int getIntFromUser(String question) {
        printIndent();
        System.out.print("<?> " + question + " ");
        while (!scanner.hasNextInt()) {
            scanner.next();
        }
        return scanner.nextInt();
    }
}