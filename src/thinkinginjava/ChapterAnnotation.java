/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thinkinginjava;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import thinkinginjava.ChapterIO.OSExecute;


/**
 *
 * @author MikeX
 */
public interface ChapterAnnotation {
    static void main(String[] args) throws Exception{
        int caseID = 1;
        switch(caseID){            
            case 0:
                UseCaseTracker.main(args);
                break;
            case 1:
                String[] curArgs = new String[] {"thinkinginjava.ChapterAnnotation$Member" };
                TableCreator.main(curArgs );
                break;
            case 2:
                break;
            default:
                break;
        }
    }
    //******************************************

    /**
     *
     */
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Test{}
    //******************************************************
    public class Testable{
        public void execute(){
            System.out.println("Executing..");
        }
        @Test void testExecute() { execute(); }
    }
    //**************************************************************
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface UseCase{
        public int id();
        public String description() default "no description";
    }
    //*****************************************************************
    public class PasswordUtils{
        @UseCase(id = 47, description = 
                "Passwords must contain at least one numeric")
        public boolean validatePassword(String password){
            return (password.matches("\\w*\\d\\w*"));
        }
        @UseCase(id = 48)
        public String encryptPassword(String password){
            return new StringBuilder(password).reverse().toString();
        }
        @UseCase(id = 49, description = "New passwords can't equal reviously used ones")
        public boolean checkForNewPassword(List<String> prevPasswords, String password){
            return !prevPasswords.contains(password);
        }
    }
    //*************************************************************************
    public class UseCaseTracker{
        public static void trackUseCases(List<Integer> useCases, Class<?> cl){
            for(Method m: cl.getDeclaredMethods()){
                UseCase uc = m.getAnnotation(UseCase.class);
                if(uc != null){
                    System.out.println("Found Use Case:" + uc.id() + " " + uc.description());
                    useCases.remove(new Integer(uc.id()));
                }
            }
            
            for(int i : useCases){
                System.out.println("Warning: Missing use case-" + i);
            }
        }
        
        public static void main(String[] args){
            List<Integer> useCases = new ArrayList<Integer>();
            Collections.addAll(useCases, 47, 48, 49, 50);
            trackUseCases(useCases, PasswordUtils.class);
        }
    }
    //*************************************************************
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface DBTable{
        public String name() default "";
    }
    //*****************************************
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Constraints{
        boolean primaryKey() default false;
        boolean allowNull() default true;
        boolean unique() default false;
    }
    //***************************************
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface SQLString{
        int value() default 0;
        String name() default "";
        Constraints constraints() default @Constraints;
    }
    //*****************************************************
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface SQLInteger{
        String name() default "";
        Constraints constraints() default @Constraints;
    }
    //****************************************************
    public @interface Uniqueness{
        Constraints constraints() default @Constraints(unique = true);
    }
    //******************************************************************
    @DBTable(name = "MEMBER")
    public class Member{
        @SQLString(value = 30) String firstName;
        @SQLString(50) String lastName;
        @SQLInteger Integer age;
        
        @SQLString(value = 30, constraints = @Constraints(primaryKey = true))
        String handle;
        
        static int memberCount;
        
        public String getHandle() { return handle; }
        public String getFirstName() { return firstName; }
        public String getLastName() { return lastName; }
        public Integer getAge() { return age; }
        @Override
        public String toString() { return handle; }        
    }
    //**************************************************************
    public class TableCreator{
        private static String getConstraints(Constraints con){
            String constraints = "";
            if(!con.allowNull())
                constraints += " Not NULL";
            
            if(con.primaryKey())
                constraints += " PRIMARY KEY";
            
            if(con.unique())
                constraints += " UNIQUE";
            
            return constraints;
        }
        //------------------------------------------------------------
        public static void main(String[] args) throws Exception{
            if(args.length < 1){
                System.out.println("arguments: annotated classes");
                System.exit(0);
            }
            
            for(String className : args){
                Class<?> cl = Class.forName(className);
                DBTable dbTable = cl.getAnnotation(DBTable.class);
                if(dbTable == null){
                    System.out.println("No DBTable annotations in class " + className);
                    continue;
                }
                
                String tableName = dbTable.name();
                
                if(tableName.length() < 1)
                    tableName = cl.getName().toUpperCase();
                
                List<String> columnDefs = new ArrayList<String>();
                for(Field field : cl.getDeclaredFields()){
                    String columnName = null;
                    Annotation[] anns = field.getDeclaredAnnotations();
                    if(anns.length < 1)
                        continue;
                    
                    if(anns[0] instanceof SQLInteger){
                        SQLInteger sInt = (SQLInteger) anns[0];
                        if(sInt.name().length() < 1)
                            columnName = field.getName().toUpperCase();
                        else
                            columnName = sInt.name();
                        
                        columnDefs.add(columnName + " INT" + getConstraints(sInt.constraints()));
                    }
                    
                    if(anns[0] instanceof SQLString){
                        SQLString sString = (SQLString) anns[0];
                        
                        if(sString.name().length() < 1)
                            columnName = field.getName().toUpperCase();
                        else
                            columnName = sString.name();
                        
                        columnDefs.add(columnName + " VARCHAR(" + sString.value() + ")" + getConstraints(sString.constraints()));
                    }
                    
                    StringBuilder createCommand = new StringBuilder("CREATE TABLE " + tableName + "(");
                    
                    for(String columnDef : columnDefs)
                        createCommand.append("\n      " + columnDef + ",");
                    
                    String tableCreate = createCommand.substring(0, createCommand.length() -1) + ");";
                    
                    System.out.println("Table Creation SQL for " + className + " is :\n" + tableCreate);
                }
            }
        }
    }
    //****************************************************************************************************
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface TestObjectCreate {}
    //********************************************
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface TestObjectCleanup {}
    //***************************************************
    @Target( {ElementType.FIELD, ElementType.METHOD } )
    @Retention(RetentionPolicy.RUNTIME)
    public @interface TestProperty {}
    //********************************************************
    public class AtUnit implements ProcessFiles.Strategy {
        static Class<?> testClass;
        static List<String> failedTests = new ArrayList<String> ();
        static long testsRun = 0;
        static long failures = 0;
        //---------------------------------------------------------------------
        public static void main(String[] args) throws Exception {
            ClassLoader.getSystemClassLoader().setDefaultAssertionStatus(true);
            new ProcessFiles(new AtUnit(), "class").start(args);
            if(failures == 0)
                System.out.println("OK (" + testsRun + " tests)");
            else{
                System.out.println("(" + testsRun + " tests)");
                System.out.println("\n>>> " + failures + " FAILURE" + (failures > 1 ? "S" : "") + " <<<");
            }
        }
        //--------------------------------------------------------------------------------
        public void process(File cFile){
            
        }
    }
    //********************************************************************************
    public class ClassNameFinder{
        public static String thisClass(byte[] classBytes) throws IOException{
            Map<Integer, Integer> offsetTable = new HashMap<Integer, Integer>();
            Map<Integer, String> classNameTable = new HashMap<Integer, String>();
            
            try{
                DataInputStream data = new DataInputStream(new ByteArrayInputStream(classBytes));
                
                int magic = data.readInt();
                int minorVersion = data.readShort();
                int majorVersion = data.readShort();
                
                int constant_pool_count = data.readShort();
                int[] constant_pool = new int[constant_pool_count];
                for(int i = 1; i < constant_pool_count; i++){
                    int tag = data.read();
                    int tableSize;
                    switch(tag){
                        case 1: //UTF
                            int length = data.readShort();
                            char[] bytes = new char[length];
                            for(int k=0; k < bytes.length; k++)
                                bytes[k] = (char)data.read();
                            
                            String className = new String(bytes);
                            classNameTable.put(i, className);
                            break;
                        case 5: //LONG
                        case 6: //DOUBLE
                            data.readLong();    //discard 8 bytes
                            i++;
                            break;
                        case 7: //Class
                            int offset = data.readShort();
                            offsetTable.put(i, offset);
                            break;
                        case 8:
                            data.readShort();
                            break;
                        case 3: //Integer
                        case 4: 
                        case 9:
                        case 10:
                        case 11:
                        case 12:
                            data.readInt(); //discard 4 bytes
                            break;
                        default:
                            throw new RuntimeException("Bad tag " + tag);
                    }
                }               
                
                short access_flages = data.readShort();
                int this_class = data.readShort();
                int super_class = data.readShort();
                
                return classNameTable.get(offsetTable.get(this_class)).replace('/', '.');
            }catch(Exception e){
                throw new RuntimeException(e);
            }
        }
        //----------------------------------------------------------------
        public static void main(String[] args) throws Exception{
            if(args.length > 0)
                for(String arg : args)
                    System.out.println(thisClass(BinaryFile.read(new File(arg))));
            else
                for(File klass : Directory.walk(".", ".*\\.class"))
                    System.out.println(thisClass(BinaryFile.read(klass)));
        }
    }
    //**********************************************************************************
    public class AtUnitExample1{
        public String methodOne(){
            return "This is methodOne";
        }
        //-----------------------------------
        public int methodTwo(){
            System.out.println("This is methodTwo");
            return 2;
        }
        //------------------------------------------------
        @Test boolean methodOneTest() {
            return methodOne().equals("This is methodOne");
        }
        //-----------------------------------------------
        @Test boolean m2() { return methodTwo() == 2; }
        //-----------------------------------------------
        @Test private boolean m3() { return true; }
        //------------------------------------------------
        @Test boolean failureTest() { return false; }
        //------------------------------------------------
        @Test boolean anotherDisappointment() { return false; }
        //-------------------------------------------------------
        public static void main(String[] args) throws Exception{
            OSExecute.command("java AtUnit AtUnitExample1");
        }
    }
    }
