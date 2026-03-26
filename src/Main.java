import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

import tests.*;

public class Main {
    private static final Map<Integer, TestCaseDefinition> TEST_CASES = createTestCases();

    public static void main(String[] args) {
        try (Scanner menuScanner = new Scanner(System.in)) {
            int choice = -1;

            while (choice != 0) {
                printMenu();

                if (menuScanner.hasNextInt()) {
                    choice = menuScanner.nextInt();
                    runTestCase(choice);
                } else {
                    System.out.println("Please give me your number!");
                    menuScanner.next();
                }
            }
        }

        System.out.println("Exiting...");
    }

    private static void printMenu() {
        System.out.println("\n=== SKELETON MENU ===");
        for (Map.Entry<Integer, TestCaseDefinition> entry : TEST_CASES.entrySet()) {
            System.out.println(entry.getKey() + ". " + entry.getValue().name());
        }
        System.out.println("0. Exit");
        System.out.print("Choose a test case: ");
    }

    private static void runTestCase(int tcNumber) {
        if (tcNumber == 0) {
            return;
        }

        TestCaseDefinition testCase = TEST_CASES.get(tcNumber);
        if (testCase == null) {
            System.out.println("No such test case has been implemented!");
            return;
        }

        System.out.println("\n--- TEST CASE " + tcNumber + " START ---");
        testCase.testCase().run();
        System.out.println("--- TEST CASE END ---\n");
    }

    private record TestCaseDefinition(String name, TestCase testCase) {}

    private static Map<Integer, TestCaseDefinition> createTestCases() {
        Map<Integer, TestCaseDefinition> cases = new LinkedHashMap<>();

        cases.put(1, new TestCaseDefinition("TC_01: Car Move Normal", new TC_01_CarMoveNormal()));
        cases.put(2, new TestCaseDefinition("TC_02: Car Routing Intersection", new TC_02_CarRoutingIntersection()));
        cases.put(3, new TestCaseDefinition("TC_03: Vehicle Obstacle Right", new TC_03_VehicleObstacleRight()));
        cases.put(4, new TestCaseDefinition("TC_04: Vehicle Obstacle Left", new TC_04_VehicleObstacleLeft()));
        cases.put(5, new TestCaseDefinition("TC_05: Vehicle Obstacle Left Right", new TC_05_VehicleObstacleLeftRight()));
        cases.put(6, new TestCaseDefinition("TC_06: Snowfall On Clean", new TC_06_SnowfallOnClean()));
        cases.put(7, new TestCaseDefinition("TC_07: Thin Snow Trampled To Ice", new TC_07_ThinSnowTrampledToIce()));
        cases.put(8, new TestCaseDefinition("TC_08: Snowfall On Ice", new TC_08_SnowfallOnIce()));
        cases.put(9, new TestCaseDefinition("TC_09: Thin Snow To Thick Snow", new TC_09_ThinSnowToThickSnow()));
        cases.put(10, new TestCaseDefinition("TC_10: Car Collides With Bus On Ice", new TC_10_CarCollidesWithBusOnIce()));
        cases.put(11, new TestCaseDefinition("TC_11: Bus Collides With Bus On Ice", new TC_11_BusCollidesWithBusOnIce()));
        cases.put(12, new TestCaseDefinition("TC_12: Car Collides With Car On Ice", new TC_12_CarCollidesWithCarOnIce()));
        cases.put(13, new TestCaseDefinition("TC_13: Snowplow Immunity On Ice", new TC_13_SnowplowImmunityOnIce()));
        cases.put(14, new TestCaseDefinition("TC_14: SweeperPlow Clear", new TC_14_SweeperPlowClear()));
        cases.put(15, new TestCaseDefinition("TC_15: DumpPlow Clear", new TC_15_DumpPlowClear()));
        cases.put(16, new TestCaseDefinition("TC_16: Equip Plow Head", new TC_16_EquipPlowHead()));
        cases.put(17, new TestCaseDefinition("TC_17: DragonPlow Success", new TC_17_DragonPlowSuccess()));
        cases.put(18, new TestCaseDefinition("TC_18: DragonPlow Empty", new TC_18_DragonPlowEmpty()));
        cases.put(19, new TestCaseDefinition("TC_19: SaltPlow Success", new TC_19_SaltPlowSuccess()));
        cases.put(20, new TestCaseDefinition("TC_20: SaltPlow Empty", new TC_20_SaltPlowEmpty()));
        cases.put(21, new TestCaseDefinition("TC_21: Icebreaker Clear", new TC_21_IcebreakerClear()));
        cases.put(22, new TestCaseDefinition("TC_22: Cleaner Buy Success", new TC_22_CleanerBuySuccess()));
        cases.put(23, new TestCaseDefinition("TC_23: Cleaner Buy Fail", new TC_23_CleanerBuyFail()));
        cases.put(24, new TestCaseDefinition("TC_24: Bus Achieve Points", new TC_24_BusAchievePoints()));
        cases.put(25, new TestCaseDefinition("TC_25: Cleaner Achieve Coin", new TC_25_CleanerAchieveCoin()));
        cases.put(26, new TestCaseDefinition("TC_26: Cleaner Buy Plow", new TC_26_CleanerBuyPlow()));
        cases.put(27, new TestCaseDefinition("TC_27: Bus Move Normal", new TC_27_BusMoveNormal()));
        cases.put(28, new TestCaseDefinition("TC_28: Bus Routing Intersection", new TC_28_BusRoutingIntersection()));
        cases.put(29, new TestCaseDefinition("TC_29: Bus Obstacle Right", new TC_29_BusObstacleRight()));
        cases.put(30, new TestCaseDefinition("TC_30: Bus Obstacle Left", new TC_30_BusObstacleLeft()));
        cases.put(31, new TestCaseDefinition("TC_31: Bus Obstacle Left Right", new TC_31_BusObstacleLeftRight()));

        return cases;
    }
}