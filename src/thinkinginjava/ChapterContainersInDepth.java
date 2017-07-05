/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thinkinginjava;

import java.util.AbstractList;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author MikeX
 */
public interface ChapterContainersInDepth {
    //--------------------------------------
    public static void test(){
        int caseID = 4;
        switch(caseID){
            case 0:
                FillingLists.test();
                break;
            case 1:
                CollectionDataTest.test();
                break;
            case 2:
                Countries.test();
                break;
            case 3:
                CountingMapData.test();
                break;
            case 4:
                LinkedHashMapDemo.test();
                break;
                
            default:
                break;
        }
    }
    //**************************************************
    public interface Generator<T> { T next(); }
    //***********************************************
    class StringAddress{
        private static int count = 0;
        private int id = ++count;
        private String s;
        public StringAddress(String s) { this.s = s; }
        @Override
        public String toString(){
            return super.toString() + ", id: " + id + "; "+ s;
        }
    }
    //************************************************************
    public class FillingLists{
        public static void test(){
            List<String> tmpList = Collections.nCopies(4, "Hello");
            List<StringAddress> list = new ArrayList(Collections.nCopies(4, new StringAddress("Hello")));
            System.out.println(list);            
            
            Collections.fill(list, new StringAddress("World"));
            System.out.println(list);
        }
    }
    //******************************************************************
    public class CollectionData<T> extends ArrayList<T> {
        public CollectionData(Generator<T> gen, int quantity){
            for(int i=0; i < quantity; i++){
                add(gen.next());
            }
        }
        //--------------------------------------------
        public static <T> CollectionData<T> list(Generator<T> gen, int quantity){
            return new CollectionData(gen, quantity);
        }
    }
    //********************************************************************
    class Government implements Generator<String>{
        String[] foundation = ("strange women lying in ponds " + 
                "disributing swords is no basis for a system of " +
                "government").split(" ");
        
        private int index;
        
        public String next() { return foundation[index++]; }             
    }
    //********************************************************
    class CollectionDataTest{
        public static void test(){
            CollectionData<String> tmpCollectionData = new CollectionData<>(new Government(), 15);
            Set<String> set = new LinkedHashSet<>(tmpCollectionData);
            System.out.println(set);
            
            set.addAll(CollectionData.list(new Government(), 15));
            System.out.println(set);
        }
    }
    //********************************************************************
    public class Countries{
        public static final String[][] DATA = {
            {"AERIA", "AGIERSA"} , {"ANGOLA", "Lnda"},
            {"Beig", "orto"},      
        };
        //***************
        private static class FlyweightMap extends AbstractMap<String, String> {
            //inner class
            private static class Entry implements Map.Entry<String, String> {
                int index;
                
                Entry(int index) { this.index = index; }
                
                @Override
                public boolean equals(Object o){
                    return DATA[index][0].equals(o);
                }

                @Override
                public String getKey() {
                    return DATA[index][0];
                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }

                @Override
                public String getValue() {
                    return DATA[index][0];
                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }

                @Override
                public String setValue(String v) {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
                
                @Override
                public int hashCode(){
                    return DATA[index][0].hashCode();
                }
            } 
            //*******************************************
            static class EntrySet extends AbstractSet<Map.Entry<String, String>>{
                private int size;
                EntrySet(int size){
                    if(size < 0)
                        this.size = 0;
                    else if(size > DATA.length)
                        this.size = DATA.length;
                    else
                        this.size = size;
                }
                
                @Override
                public int size() { return size; }
                //inner class
                private class Iter implements Iterator<Map.Entry<String, String>>{
                    private Entry entry = new Entry(-1);
                    
                    @Override
                    public boolean hasNext(){
                        return entry.index < size - 1;
                    }
                    
                    @Override
                    public Map.Entry<String, String> next(){
                        entry.index++;
                        return entry;
                    }
                    
                    @Override
                    public void remove(){
                        
                    }
                }
                
                @Override
                public Iterator<Map.Entry<String, String>> iterator() {
                    return new Iter();
                }
            }
            //
            private static Set<Map.Entry<String, String>> entries = new EntrySet(DATA.length);
            
            @Override
            public Set<Map.Entry<String, String>> entrySet() {
                return entries;
            }
        }
        //
        static Map<String, String> select(final int size){
            return new FlyweightMap() {
                @Override
                public Set<Map.Entry<String, String>> entrySet(){
                    return new EntrySet(size);
                }
            };
        }
        //
        static Map<String, String> map = new FlyweightMap();
        
        public static Map<String, String> capitals() {
            return map;
        }
        
        public static Map<String, String> capitals(int size){
            return select(size);
        }
        
        static List<String> names = new ArrayList<String>(map.keySet());
        
        public static List<String> names() { return names; }
        
        public static List<String> names(int size){
            return new ArrayList<String> (select(size).keySet());
        }
        
        public static void test(){
            System.out.println(capitals(10));
        }
    }
    //****************************************************************
    public class CountingMapData extends AbstractMap<Integer, String> {
        private int size;
        private static String[] chars = "A B C D E F G H I J K L M N O P Q R S T U V W X Y Z".split(" ");
        
        public CountingMapData(int size){
            if(size < 0) 
                this.size = 0;
            else
                this.size = size;
        }
        
        private static class Entry implements Map.Entry<Integer, String> {
            int index;
            Entry(int index){
                this.index = index;
            }
            
            @Override
            public boolean equals(Object o){
                Integer tmpIndex = Integer.valueOf(index);
                return tmpIndex.equals(o);
            }
            
            @Override
            public Integer getKey() { return index; }
            
            @Override
            public String getValue() {
                String tmpStr = chars[index % chars.length];
                String str = tmpStr + Integer.toString(index / chars.length);
                return str;
            }
            
            @Override
            public String setValue(String value){
                throw new UnsupportedOperationException();
            }
            
            @Override
            public int hashCode(){
                Integer tmpIndex = Integer.valueOf(index);
                Integer hCode = tmpIndex.hashCode();
                return hCode;
            }
        }// end of Entry
        //-----------------------------------
        @Override
        public Set<Map.Entry<Integer, String>> entrySet(){
            Set<Map.Entry<Integer, String>> entries = new LinkedHashSet<Map.Entry<Integer, String>>();
            
            for(int i=0; i < size; i++)
                entries.add(new Entry(i));
            
            return entries;
        }
        //----------------------------------------
        public static void test(){
            System.out.println(new CountingMapData(60));
        }
    }// end of class - CountingMapData
    //***************************************************************
    public class LinkedHashMapDemo{
        public static void test(){
            LinkedHashMap<Integer, String> linkedMap = new LinkedHashMap<Integer, String>(new CountingMapData(9));
            
            System.out.println(linkedMap);
            
            linkedMap = new LinkedHashMap<Integer, String>(16, 0.75f, true);
            
            linkedMap.putAll(new CountingMapData(9));
            
            System.out.println(linkedMap);
            
            for(int i=0; i < 6; i++)
                linkedMap.get(i);
            
            System.out.println(linkedMap);
            
            linkedMap.get(0);
            
            System.out.println(linkedMap);
        }
    }
    //**********************************************************************
    public class CountingIntegerList extends AbstractList<Integer>{
        private int size;
        public CountingIntegerList(int size){
            this.size = size < 0 ? 0 : size;
        }
        //-------------------
        public Integer get(int index){
            return Integer.valueOf(index);
        }
        //---------------------------------
        public int size() { return size; }
        //-----------------------------------
        public static void main(String[] args){
            System.out.println(new CountingIntegerList(30));
        }
    }
    //***********************************************************************
    public abstract class Test<C> {
        String name;
        public Test(String name){
            this.name = name;
        }
        abstract int test(C container, TestParam tp);
    }
    //*****************************************************
    public class TestParam{
        public final int size;
        public final int loops;
        //-------------------------------------------
        public TestParam(int size, int loops){
            this.size = size;
            this.loops = loops;
        }
        //-------------------------------------------------
        public static TestParam[] array(int... values){
            int size = values.length/2;
            TestParam[] result = new TestParam[size];
            int n=0;
            for(int i=0; i < size; i++)
               result[i] = new TestParam(values[n++], values[n++]);
            
            return result;
        }
        //-----------------------------------------------------------
        public static TestParam[] array(String[] values){
            int[] vals = new int[values.length];
            for(int i=0; i < vals.length; i++)
                vals[i] = Integer.decode(values[i]);
            
            return array(vals);
        }
    }// end of class TestParam
    //***********************************************************************
    public class Tester<C> {
        public static int fieldWidth = 8;
        public static TestParam[] defaultParams = TestParam.array(10, 5000, 100, 5000, 1000, 5000, 10000, 500);
        
        protected C container;
        protected C initialize(int size) { return container; }
        
        private String headline = "";
        private List<Test<C>> tests;
        
        private static String stringField(){
            return "%" + fieldWidth + "s";
        }
        
        private static String numberField(){
            return "%" + fieldWidth + "d";
        }
        
        private static int sizeWidth = 5;
        private static String sizeField = "%" + sizeWidth + "s";
        
        private TestParam[] paramList = defaultParams;
        
        public Tester(C container, List<Test<C>> tests){
            this.container = container;
            this.tests = tests;
            
            if(container != null)
                headline = container.getClass().getSimpleName();
        }
        
        public Tester(C container, List<Test<C>> tests, TestParam[] paramList){
            this(container, tests);
            this.paramList = paramList;
        }
        
        public void setHeadline(String newHeadline){
            headline = newHeadline;
        }       
        
        public void displayHeader(){
            int width = fieldWidth * tests.size() + sizeWidth;
            int dashLength = width - headline.length() + sizeWidth;
            
            StringBuilder head = new StringBuilder(width);
            
            for(int i=0; i < dashLength/2; i++)
                head.append('-');
            
            head.append(' ');
            head.append(headline);
            head.append(' ');
            
            for(int i=0; i < dashLength /2; i++)
                head.append('-');
            
            System.out.println(head);
            
            System.out.format(sizeField, "size");
            
            for(Test test : tests)
                System.out.format(stringField(), test.name);
            
            System.out.println();
        }
        //------------------------------------------------
        public void timedTest(){
            displayHeader();
            for(TestParam param : paramList){
                System.out.format(sizeField, param.size);
                
                for(Test<C> test : tests){
                    C kontainer = initialize(param.size);
                    long start = System.nanoTime();
                    
                    int reps = test.test(kontainer, param);
                    
                    long duration = System.nanoTime() - start;
                    
                    long timePerRep = duration / reps;
                    
                    System.out.format(numberField(), timePerRep);
                }
                System.out.println();
            }
        }
        //------------------------------------------------
        public static <C> void run(C cntnr, List<Test<C>> tests){
            new Tester<C>(cntnr, tests).timedTest();
        }
        
        public static <C> void run(C cntnr, List<Test<C>> tests, TestParam[] paramList){
            new Tester<C>(cntnr, tests, paramList).timedTest();
        }
    }
    //********************************************************************
    public class ListPrfomance{
        static Random rand = new Random();
        static int reps = 1000;
        static List<Test<List<Integer>>> tests = new ArrayList<Test<List<Integer>>>();
        
        static List<Test<LinkedList<Integer>>> qTests = new ArrayList<Test<LinkedList<Integer>>>();
        
        static{
            tests.add(new Test<List<Integer>>("add"){
                @Override
                int test(List<Integer> list, TestParam tp) {
                    int loops = tp.loops;
                    int listSize = tp.size;
                    for(int i=0; i < loops; i++){
                        list.clear();
                        for(int j=0; j < listSize; j++)
                            list.add(j);
                    }
                    return loops * listSize;
                }                
            });
            
            tests.add(new Test<List<Integer>>("get"){
                @Override
                int test(List<Integer> list, TestParam tp){
                    int loops = tp.loops * reps;
                    int listSize = list.size();
                    for(int i=0; i < loops; i++)
                        list.get(rand.nextInt(listSize));
                    
                    return loops;
                }
            });
            
            tests.add(new Test<List<Integer>>("set"){
                @Override
                int test(List<Integer> list, TestParam tp){
                    int loops = tp.loops * reps;
                    int listSize = list.size();
                    for(int i=0; i < loops; i++)
                        list.set(rand.nextInt(listSize), 47);
                    
                    return loops;
                }
            });
            
            tests.add(new Test<List<Integer>>("iteradd"){
                @Override
                int test(List<Integer> list, TestParam tp){
                    final int LOOPS = 1000000;
                    int half = list.size()/2;
                    ListIterator<Integer> it = list.listIterator(half);
                    for(int i=0; i < LOOPS; i++)
                        it.add(47);
                    return LOOPS;
                }
            });
            
            tests.add(new Test<List<Integer>>("insert"){
                @Override
                int test(List<Integer> list, TestParam tp){
                    int loops = tp.loops;
                                       
                    for(int i=0; i < loops; i++)
                        list.add(5, 47);
                    return loops;
                }
            });
            
            tests.add(new Test<List<Integer>>("remove"){
                @Override
                int test(List<Integer> list, TestParam tp){
                    int loops = tp.loops;
                    int size = tp.size;                   
                    for(int i=0; i < loops; i++){
                        list.clear();
                        list.addAll(new CountingIntegerList(size));
                        while(list.size() > 5)
                            list.remove(5);
                    }                        
                    return loops * size;
                }
            });
            //For qTests:
            qTests.add(new Test<LinkedList<Integer>>("addFirst"){
                @Override
                int test(LinkedList<Integer> list, TestParam tp){
                    int loops = tp.loops;
                    int size = tp.size;
                    for(int i=0; i < loops; i++){
                        list.clear();
                        for(int j=0; j < size; j++)
                            list.addFirst(47);
                    }
                    return loops * size;
                }
            });
            
            qTests.add(new Test<LinkedList<Integer>>("addLast"){
                @Override
                int test(LinkedList<Integer> list, TestParam tp){
                    int loops = tp.loops;
                    int size = tp.size;
                    for(int i=0; i < loops; i++){
                        list.clear();
                        for(int j=0; j < size; j++)
                            list.addLast(47);
                    }
                    return loops * size;
                }
            });
        }
    }
}
