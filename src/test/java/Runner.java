import fr.unice.polytech.qgl.qbc.Explorer;
import org.junit.Test;

import java.io.File;

import static eu.ace_design.island.runner.Runner.run;

public class Runner {

    //Run standard, avec log
    public static void main(String[] args) throws Exception{
        week05();
    }


    //Tests d'integration, il faut utiliser l'option -DactivateIntegrationTests dans la commande Maven pour les lancer
    @Test
    public void integrationTest(){
        try {
            week46();
            week47();
            week48();
            week49();
            week50();
            week51();
            week52();
            week53();
            week01();
            week02();
            week03();
            week05();
            week06();
            week07();
            week08();
            week09();
            week10();
            week11();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*

    Maps : https://github.com/mosser/QGL-15-16/tree/master/championships
    Parameters : https://github.com/mosser/QGL-15-16/tree/master/arena/src/main/scala
    Seeds : https://github.com/mosser/QGL-15-16/blob/master/arena/src/main/scala/library/Islands.scala

    */

    public static void week46() throws Exception {
        run(Explorer.class)
                .exploring(new File("src/main/resources/map46.json"))
                .withSeed(0L)
                .startingAt(1, 1, "EAST")
                .backBefore(7000)
                .withCrew(15)
                .collecting(1000, "WOOD")
                .collecting(300, "QUARTZ")
                .collecting(10, "FLOWER")
                .storingInto("./outputs")
                .fire();
    }

    public static void week47() throws Exception {
        run(Explorer.class)
                .exploring(new File("src/main/resources/map47.json"))
                .withSeed(0x7C86C8F0AE471824L)
                .startingAt(1, 1, "EAST")
                .backBefore(7000)
                .withCrew(15)
                .collecting(1000, "WOOD")
                .collecting(300, "QUARTZ")
                .collecting(10, "FLOWER")
                .storingInto("./outputs")
                .fire();
    }

    public static void week48() throws Exception {
        run(Explorer.class)
                .exploring(new File("src/main/resources/map48.json"))
                .withSeed(0x7099D003D471C6D9L)
                .startingAt(152, 159, "NORTH")
                .backBefore(10000)
                .withCrew(3)
                .collecting(2000, "WOOD")
                .collecting(30, "GLASS")
                .collecting(1000, "FUR")
                .storingInto("./outputs")
                .fire();
    }

    public static void week49() throws Exception {
        run(Explorer.class)
                .exploring(new File("src/main/resources/map49.json"))
                .withSeed(0x19ABF6AA7B22F38BL)
                .startingAt(152, 159, "NORTH")
                .backBefore(10000)
                .withCrew(3)
                .collecting(5000, "WOOD")
                .collecting(300, "GLASS")
                .collecting(1000, "SUGAR_CANE")
                .storingInto("./outputs")
                .fire();
    }
    public static void week50() throws Exception {
        run(Explorer.class)
                .exploring(new File("src/main/resources/map50.json"))
                .withSeed(0xBE3EF65BEF2BD459L)
                .startingAt(1, 1, "EAST")
                .backBefore(10000)
                .withCrew(3)
                .collecting(1000, "WOOD")
                .collecting(5000, "FUR")
                .collecting(300, "GLASS")
                .storingInto("./outputs")
                .fire();
    }
    public static void week51() throws Exception {
        run(Explorer.class)
                .exploring(new File("src/main/resources/map51.json"))
                .withSeed(0x9B4937D7A7783C0EL)
                .startingAt(1, 1, "SOUTH")
                .backBefore(10000)
                .withCrew(25)
                .collecting(3000, "WOOD")
                .collecting(3000, "FUR")
                .collecting(100, "GLASS")
                .storingInto("./outputs")
                .fire();
    }
    public static void week52() throws Exception {
        run(Explorer.class)
                .exploring(new File("src/main/resources/map52.json"))
                .withSeed(0x6C3E51CEEFA93D5FL)
                .startingAt(1, 1, "EAST")
                .backBefore(10000)
                .withCrew(25)
                .collecting(4000, "WOOD")
                .collecting(100, "GLASS")
                .collecting(1000, "FUR")
                .storingInto("./outputs")
                .fire();
    }
    public static void week53() throws Exception {
        run(Explorer.class)
                .exploring(new File("src/main/resources/map53.json"))
                .withSeed(0x68C81A2529ACAFF3L)
                .startingAt(1, 1, "SOUTH")
                .backBefore(10000)
                .withCrew(25)
                .collecting(5000, "WOOD")
                .collecting(100, "GLASS")
                .collecting(80, "FLOWER")
                .storingInto("./outputs")
                .fire();
    }
    public static void week01() throws Exception {
        run(Explorer.class)
                .exploring(new File("src/main/resources/map01.json"))
                .withSeed(0x8E80C1B12F0B7C40L)
                .startingAt(1, 1, "EAST")
                .backBefore(15000)
                .withCrew(4)
                .collecting(3000, "WOOD")
                .collecting(800, "QUARTZ")
                .collecting(1000, "PLANK")
                .collecting(80, "FLOWER")
                .storingInto("./outputs")
                .fire();
    }
    public static void week02() throws Exception {
        run(Explorer.class)
                .exploring(new File("src/main/resources/map02.json"))
                .withSeed(0x71CE0F48848F322DL)
                .startingAt(1, 1, "EAST")
                .backBefore(15000)
                .withCrew(4)
                .collecting(5000, "WOOD")
                .collecting(200, "QUARTZ")
                .collecting(40, "FLOWER")
                .collecting(50, "GLASS")
                .storingInto("./outputs")
                .fire();
    }
    public static void week03() throws Exception {
        run(Explorer.class)
                .exploring(new File("src/main/resources/map03.json"))
                .withSeed(0xC0ECF96E85B08EFEL)
                .startingAt(1, 1, "SOUTH")
                .backBefore(20000)
                .withCrew(15)
                .collecting(5000, "WOOD")
                .collecting(200, "QUARTZ")
                .collecting(40, "FLOWER")
                .collecting(50, "GLASS")
                .storingInto("./outputs")
                .fire();
    }
    //public static void week04() throws Exception {} idem week52
    public static void week05() throws Exception {
        run(Explorer.class)
                .exploring(new File("src/main/resources/map05.json"))
                .withSeed(0x3EAC7E4FF2A31F33L)
                .startingAt(1, 1, "SOUTH")
                .backBefore(20000)
                .withCrew(25)
                .collecting(7000, "WOOD")
                .collecting(2000, "FUR")
                .collecting(5000, "PLANK")
                .collecting(100, "GLASS")
                .storingInto("./outputs")
                .fire();
    }
    public static void week06() throws Exception {
        run(Explorer.class)
                .exploring(new File("src/main/resources/map06.json"))
                .withSeed(0x1DB7EB420E31D98AL)
                .startingAt(1, 159, "NORTH")
                .backBefore(20000)
                .withCrew(25)
                .collecting(15000, "WOOD")
                .collecting(100, "GLASS")
                .storingInto("./outputs")
                .fire();
    }
    public static void week07() throws Exception {
        run(Explorer.class)
                .exploring(new File("src/main/resources/map07.json"))
                .withSeed(0xEC4F4F3A4630034BL)
                .startingAt(1, 159, "NORTH")
                .backBefore(20000)
                .withCrew(25)
                .collecting(15000, "WOOD")
                .collecting(100, "GLASS")
                .collecting(30, "LEATHER")
                .storingInto("./outputs")
                .fire();
    }
    public static void week08() throws Exception {
        run(Explorer.class)
                .exploring(new File("src/main/resources/map08.json"))
                .withSeed(0xAE6942D415A29E7FL)
                .startingAt(1, 159, "NORTH")
                .backBefore(20000)
                .withCrew(15)
                .collecting(10000, "WOOD")
                .collecting(300, "LEATHER")
                .collecting(50, "GLASS")
                .storingInto("./outputs")
                .fire();
    }

    public static void week09() throws Exception {
        run(Explorer.class)
                .exploring(new File("src/main/resources/map09.json"))
                .withSeed(0xDE577ABFB68A7E38L)
                .startingAt(1, 1, "EAST")
                .backBefore(20000)
                .withCrew(15)
                .collecting(10000, "WOOD")
                .collecting(50, "GLASS")
                .collecting(300, "LEATHER")
                .storingInto("./outputs")
                .fire();
    }

    public static void week10() throws Exception {
        run(Explorer.class)
                .exploring(new File("src/main/resources/map10.json"))
                .withSeed(0xDE05C9B2CE32BF74L)
                .startingAt(158, 158, "NORTH")
                .backBefore(20000)
                .withCrew(15)
                .collecting(10000, "WOOD")
                .collecting(500, "QUARTZ")
                .collecting(300, "LEATHER")
                .collecting(50, "RUM")
                .storingInto("./outputs")
                .fire();
    }
    public static void week11() throws Exception {
        run(Explorer.class)
                .exploring(new File("src/main/resources/map11.json"))
                .withSeed(0xC70825E4D144AA25L)
                .startingAt(1, 1, "SOUTH")
                .backBefore(20000)
                .withCrew(15)
                .collecting(10000, "WOOD")
                .collecting(100, "QUARTZ")
                .collecting(300, "LEATHER")
                .collecting(50, "ORE")
                .storingInto("./outputs")
                .fire();
    }
}