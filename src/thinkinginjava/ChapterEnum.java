/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thinkinginjava;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import thinkinginjava.ChapterIO.OSExecute;

/**
 *
 * @author MikeX
 */
public interface ChapterEnum {
    static void mian(String[] args){
        int caseID = 2;
        switch(caseID){
            case 0:
                Reflection.main(args);               
                break;
            case 1:
                RandomTest.main(args);
                break;
            case 2:
                SecurityCategory.main(args);
                break;
            default:
                break;
        }
    }
    
    enum Explore { HERE, THERE }
    
    public class Reflection{
        public static Set<String> analyze(Class<?> enumClass){
            System.out.println("-------- Analyzing " + enumClass + " ---------");
            System.out.println("Interface: ");
            for(Type t : enumClass.getGenericInterfaces())
                System.out.println(t);
            
            System.out.println("Base: " + enumClass.getSuperclass());
            System.out.println("Methods: ");
            Set<String> methods = new TreeSet();
            for(Method m : enumClass.getMethods())
                methods.add(m.getName());
            System.out.println(methods);
            return methods;
        }
        //---------------------------------------------------------------------
        public static void main(String[] args){
            Set<String> exploreMethods = analyze(Explore.class);
            Set<String> enumMetods = analyze(Enum.class);
            
            System.out.println("Explore.containsAll(Enum)? " + exploreMethods.containsAll(enumMetods));
            System.out.println("Explore.removeAll(Enum): ");
            exploreMethods.removeAll(enumMetods);
            System.out.println(exploreMethods);
            
            String path = "C:\\XM\\Tmp\\Java\\ThinkingInJava\\build\\classes\\thinkinginjava\\ChapterEnum$Reflection";
            String file = "thinkinginjava.ChapterEnum$Explore";
            OSExecute.command("javap " + file);
        }
    }
    //************************************************************************************
    public class Enums{
        private static Random rand = new Random(47);
        
        public static <T extends Enum<T>> T random(Class<T> ec){
            return random(ec.getEnumConstants());
        }
        
        public static <T> T random(T[] values){
            return values[(rand.nextInt(values.length))];
        }
    }
    //***********************************************************************
    enum Activity { SITTING, LYING, STANDING, HOPPING, RUNNING, DODGING, JUMPING, FALILING, FLYING }
    
    public class RandomTest{
        public static void main(String[] args){
            for(int i=0; i < 20; i++){
                System.out.print(Enums.random(Activity.class) + "  ");
                if(i % 9 == 0 && i!= 0)
                    System.out.println();
            }            
        }
    }
    //************************************************************************
    enum SecurityCategory{
        STOCK(Security.Stock.class), BOND(Security.Bond.class);
        
        Security[] values;
        SecurityCategory(Class<? extends Security> kind){
            values = kind.getEnumConstants();
        }
        
        interface Security{
            enum Stock implements Security { SHORT, LONG, MARGIN }
            enum Bond implements Security { MUNICIPAL, JUNK }
        }
        
        public Security randomSelection(){
            return Enums.random(values);
        }
        
        public static void main(String[] args){
            for(int i=0; i < 10; i ++){
                SecurityCategory category = Enums.random(SecurityCategory.class);
                System.out.println(category + ": " + category.randomSelection());               
            }
            
            final String dir = System.getProperty("user.dir");
            System.out.println("current dir = " + dir);
        }
    }    
}
