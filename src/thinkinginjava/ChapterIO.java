/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thinkinginjava;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;
import java.util.regex.Pattern;

//import Utils.*;

/**
 *
 * @author MikeX
 */
public interface ChapterIO {
    static void test(String[] args) throws IOException, FileNotFoundException, ClassNotFoundException, Exception{
        int caseID = 22;
        switch(caseID){
            case 0:
                TextFile.test(args);
                break;
            case 1:
                Redirecting.test(args);
                break;
            case 2:
                OSExecuteDemo.main(args);
                break;
            case 20:
                Worm.test();
                break;
            case 21:
                MyWorld.test(args);
                break;
            case 22:
                StoreCADState.test();
                RecoverCADState.test();
                break;
            default:
                break;
        }
    }
    //*********************************************************
    public class TextFile extends ArrayList<String>{
        public static String read(String fileName){
            StringBuilder sb = new StringBuilder();
            try{
                BufferedReader in = new BufferedReader(new FileReader(
                    new File(fileName).getAbsoluteFile()));
                try{
                    String s;
                    while((s = in.readLine()) != null){
                        sb.append(s);
                        sb.append("\n");
                    }
                }finally{
                    in.close();
                }
            }catch(IOException e){
                throw new RuntimeException(e);
            }
            return sb.toString();
        }
        //----------------------------------------------------------------
        public static void write(String fileName, String text){
            try{
                PrintWriter out = new PrintWriter(new File(fileName).getAbsoluteFile());
                try{
                    //out.println(text);  //without changing lines
                    String[] strArray = text.split("\n");
                    for(int j=0; j < strArray.length; j++)
                        out.println(strArray[j]);
                }finally{
                    out.close();
                }
            }catch(IOException e){
                throw new RuntimeException(e);
            }
        }
        //-----------------------------------------------------------------
        public TextFile(String fileName, String splitter){
            super(Arrays.asList(read(fileName).split(splitter)));
            
            if(get(0).equals(""))
                remove(0);
        }
        //--------------------------------------------------------------
        public TextFile(String fileName){
            this(fileName, "\n");
        }
        //-------------------------------------------------------------
        public void write(String fileName){
            try{
                PrintWriter out = new PrintWriter(new File(fileName).getAbsoluteFile());
                
                try{
                    for(String item : this)
                        out.println(item);
                }finally{
                    out.close();
                }
            }catch(IOException e){
                throw new RuntimeException(e);
            }
        }
        //----------------------------------------------------------------
        public static void test(String[] args){
            String file = read("TextFile.java");
            write("test.txt", file);
            
            TextFile text = new TextFile("test.txt");
            text.write("test2.txt");
            
            TreeSet<String> words = new TreeSet<String>(new TextFile("TextFile.java", "\\W+"));
            
            System.out.println(words.headSet("B"));
        }
    }    
    //***********************************************************************************
    public class Redirecting{
        public static void test(String[] args) throws FileNotFoundException, IOException{
            PrintStream console = System.out;
            BufferedInputStream in = new BufferedInputStream(new FileInputStream("test.txt"));
            
            PrintStream out = new PrintStream(new BufferedOutputStream(
                new FileOutputStream("test.out")));
            
            System.setIn(in);
            System.setOut(out);
            System.setErr(out);
            
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String s;
            while((s = br.readLine()) != null)
                System.out.println(s);
            
            out.close();
            System.setOut(console);
        }
    }
    //******************************************************************************
    public class OSExecuteException extends RuntimeException{
        public OSExecuteException(String why){ super(why); }
    }
    //***********************************************************
    public class OSExecute{
        public static void command(String command){
            boolean err = false;
            try{
                Process process = new ProcessBuilder(command.split(" ")).start();
                
                BufferedReader results = new BufferedReader(new InputStreamReader(process.getInputStream()));
                
                String s;
                while((s = results.readLine()) != null){
                    System.out.println(s);
                }
                
                BufferedReader errors = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                while((s = errors.readLine()) != null){
                    System.out.println(s);
                    err = true;
                }
            } catch(Exception e){
                if(!command.startsWith("CMD /C"))
                    command("CMD /C" + command);
                else
                    throw new RuntimeException(e);
            }
            
            if(err)
                throw new OSExecuteException("Errors executing " + command);
        }
    }
    //****************************************************************************
    public class OSExecuteDemo{
        public static void main(String[] args){
            OSExecute.command("javap ChapterIO$OSExecuteDemo");
        }
    }
    //*****************************************************************************
    //Object serialization
    class Data implements Serializable{
        private int n;
        public Data(int n) { this.n = n; }
        @Override
        public String toString() { return Integer.toString(n); }
    }
    //***********************************************************
    public class Worm implements Serializable{
        private static Random rand = new Random(47);
        private Data[] d = {
            new Data(rand.nextInt(10)),
            new Data(rand.nextInt(10)),
            new Data(rand.nextInt(10)),
        };
        
        private Worm next;
        private char c;
        
        public Worm(int i, char x){
            System.out.println("Worm constructor: " + i);
            c = x;
            if(--i > 0)
                next = new Worm(i, (char)(x + 1));
        }
        
        public Worm(){
            System.out.println("Worm constructor");
        }
        
        @Override
        public String toString(){
            StringBuilder result = new StringBuilder(":");
            result.append(c);
            result.append("(");
            for(Data dat : d)
                result.append(dat);
            
            result.append("); ");
            
            if(next != null)
                result.append(next);
            
            return result.toString();
        }
        
        public static void test() throws FileNotFoundException, IOException, ClassNotFoundException{
            Worm w = new Worm(6, 'a');
            System.out.println("w = " + w);
            
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("worm.out"));
            out.writeObject("Worm stroage\n");
            out.writeObject(w);
            out.close();
            
            ObjectInputStream in = new ObjectInputStream(new FileInputStream("worm.out"));
            String s = (String)in.readObject();
            Worm w2 = (Worm)in.readObject();
            System.out.println(s + "w2 = " + w2);
            
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            ObjectOutputStream out2 = new ObjectOutputStream(bout);
            out2.writeObject("Worm storage\n");
            out2.writeObject(w);
            out2.flush();
            
            FileChannel fc = new FileOutputStream("worm2.txt").getChannel();
            fc.write(ByteBuffer.wrap(bout.toByteArray()));
            fc.close();
            
            ObjectInputStream in2 = new ObjectInputStream( new ByteArrayInputStream(bout.toByteArray()));
            s = (String)in2.readObject();
            Worm w3 = (Worm)in2.readObject();
            System.out.println(s + "w3 = " + w3);
            
            fc = new FileInputStream("worm2.txt").getChannel();
            ByteBuffer buff = ByteBuffer.allocate(3000);
            fc.read(buff);
            buff.flip();
            //while(buff.hasRemaining())
            //    System.out.print((char)buff.get());
            
            //System.out.println();
            
            System.out.println(buff.array().toString());
            
            ObjectInputStream in4 = new ObjectInputStream(new FileInputStream("worm2.txt"));
            s = (String)in4.readObject();
            Object w4 = in4.readObject();
            System.out.println(s + "w4 = " + w4);
            System.out.println(w4.getClass());
        }
    }
    //********************************************************************
    //Using persistence
    class House implements Serializable {}
    //****************************************
    class Animal implements Serializable{
        private String name;
        private House preferredHouse;
        //------------------------------
        Animal(String nm, House h){
            name = nm;
            preferredHouse = h;
        }
        //--------------------------------

        /**
         *
         * @return
         */
        @Override
        public String toString(){
            return name + "[" + super.toString() + "], " + preferredHouse + "\n";
        }
    } 
    //****************************************************************************
    public class MyWorld {
        public static void test(String[] args) throws IOException, ClassNotFoundException{
            House house = new House();
            List<Animal> animals = new ArrayList<Animal>();
            animals.add(new Animal("Bosco the dog", house));
            animals.add(new Animal("Ralph the hamster", house));
            animals.add(new Animal("Molly the cat", house));
            System.out.println("animals: " + animals);
            
            ByteArrayOutputStream buf1 = new ByteArrayOutputStream();
            ObjectOutputStream o1 = new ObjectOutputStream(buf1);
            o1.writeObject(animals);
            o1.writeObject(animals);
            
            ByteArrayOutputStream buf2 = new ByteArrayOutputStream();
            ObjectOutputStream o2 = new ObjectOutputStream(buf2);
            o2.writeObject(animals);
            
            ObjectInputStream in1 = new ObjectInputStream(new ByteArrayInputStream(buf1.toByteArray()));
            
            ObjectInputStream in2 = new ObjectInputStream(new ByteArrayInputStream(buf2.toByteArray()));
            
            List
                    animals1 = (List)in1.readObject(),
                    animals2 = (List)in1.readObject(),
                    animals3 = (List)in2.readObject();
            
            System.out.println("animals1: " + animals1);
            System.out.println("animals2: " + animals2);
            System.out.println("animals3: " + animals3);
        }
    }
    //****************************************************************************
    abstract class Shape implements Serializable{
        public static final int RED = 1, BLUE = 2, GREEN = 3;
        private int xPos, yPos, dimension;
        private static Random rand = new Random(47);
        private static int counter = 0;
        
        public abstract void setColor(int newColor);
        public abstract int getColor();
        
        public Shape(int xVal, int yVal, int dim){
            xPos = xVal;
            yPos = yVal;
            dimension = dim;
        }
        //----------------------------------------------
        @Override
        public String toString() {
            return getClass() + "color[" + getColor() + "] xPos[" + xPos + "] yPos[" + 
                    yPos + "] dim[" + dimension + "]\n";
        }
        //-------------------------------------------------
        public static Shape randomFactory(){
            int xVal = rand.nextInt(100);
            int yVal = rand.nextInt(100);
            int dim = rand.nextInt(100);
            
            switch(counter++ % 3){
                default:
                case 0: return new Circle(xVal, yVal, dim);
                case 1: return new Square(xVal, yVal, dim);
                case 2: return new Line(xVal, yVal, dim);
            }
        }
    }
    //********************************************************
    class Circle extends Shape{
        private static int color = RED;

        public Circle(int xVal, int yVal, int dim){
            super(xVal, yVal, dim);
        }
        //------------------
        @Override
        public void setColor(int newColor) { color = newColor; }
        @Override
        public int getColor() { return color; }
    }
    //****************************************************************
    class Square extends Shape{
        private static int color;
        
        public Square(int xVal, int yVal, int dim){
            super(xVal, yVal, dim);
            color = RED;
        }
        //-----------------------------------------------
        @Override
        public void setColor(int newColor) { color = newColor; }
        @Override
        public int getColor() { return color; }
    }
    //***************************************************************
    class Line extends Shape{
        private static int color = RED;
        
        public static void serializeStaticState(ObjectOutputStream os) throws IOException{
            os.writeInt(color);
        }
        public static void deserializeStaticState(ObjectInputStream os) throws IOException{
            color = os.readInt();
        }
        
        public Line(int xVal, int yVal, int dim){
            super(xVal, yVal, dim);
        }
        
        @Override
        public void setColor(int newColor) { color = newColor; }
        @Override
        public int getColor() { return color; }
    }
    //****************************************************************
    public class StoreCADState{
        public static void test() throws FileNotFoundException, IOException{
            List<Class<? extends Shape>> shapeTypes = new ArrayList<Class<? extends Shape>>();
            
            shapeTypes.add(Circle.class);
            shapeTypes.add(Square.class);
            shapeTypes.add(Line.class);
            
            List<Shape> shapes = new ArrayList<Shape>();
            
            for(int i=0; i < 10; i++)
                shapes.add(Shape.randomFactory());
            
            for(int i=0; i < 10; i++)
                ((Shape)shapes.get(i)).setColor(Shape.GREEN);
            
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("CADState.out"));
            out.writeObject(shapeTypes);
            Line.serializeStaticState(out);
            out.writeObject(shapes);
            
            System.out.println(shapes);
            out.close();
        }
    }
    //***************************************************************
    public class RecoverCADState{
        public static void test() throws Exception{
            ObjectInputStream in = new ObjectInputStream(new FileInputStream("CADState.out"));
            
            List<Class<? extends Shape>> shapeTypes = (List<Class<? extends Shape>>)in.readObject();
            
            Line.deserializeStaticState(in);
            
            List<Shape> shapes = (List<Shape>)in.readObject();
            
            System.out.println(shapes);
        }
    }
    //*******************************************************************************
    
    
}
