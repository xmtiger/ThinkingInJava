/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thinkinginjava;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author MikeX
 */
public interface ChapterGenerics {
    //--------------------------------------------------------------
    //testing method
    public static void test(String[] args){
        int caseId = 7;
        switch(caseId){
            case 0:
                TupleTest.test();
                break;
            case 1:
                ArrayMaker.test();
                break;
            case 2:
                ErasedTypeTest.main(args);
                break;
            case 3:
                ArrayOfGeneric.main(args);
                break;
            case 4:
                GenericArray2.main(args);
                break;
            case 5:
                GenericArrayWithTypeToken.main(args);
                break;
            case 6:
                GenericAndCovariance.main(args);
                break;
            case 7:
                GenericReading.main(args);
                break;
            default:
                break;
        }
    }
    //****************************************************************
    public class TwoTuple<A,B>{
        public final A first;
        public final B second;
        public TwoTuple(A a, B b){
            first = a; second =b;
        }
        @Override
        public String toString(){
            return "(" + first + ", " + second + ")";
        }
    }
    //*******************************************************************
    public class ThreeTuple<A,B,C> extends TwoTuple<A,B>{
        public final C third;
        public ThreeTuple(A a, B b, C c){
            super(a, b);
            third = c;
        }
        @Override
        public String toString(){
            return "(" + first + ", " + second + ", " + third + ")";
        }
    }
    //******************************************************************
    public class FourTuple<A, B, C, D> extends ThreeTuple<A, B, C> {
        public final D fourth;
        public FourTuple(A a, B b, C c, D d){
            super(a, b, c);
            fourth = d;
        }
        @Override
        public String toString(){
            return "(" + first + ", " + second + ", " + third + ", " + fourth + ")";
        }
    }
    //***********************************************************************************
    public class FiveTuple<A, B, C, D, E> extends FourTuple{
        public final E fifth;
        public FiveTuple(A a, B b, C c, D d, E e){
            super(a,b,c,d);
            fifth = e;
        }
        @Override
        public String toString(){
            return "(" + first + ", " + second + ", " + third + ", " + fourth 
                    + ", " + fifth + ")";
        }
    }
    //*************************************************************************
    class Amphibian{}
    //****************
    class Vehicle {}
    //******************************************************************
    public class Tuple{
        public static <A,B> TwoTuple tuple(A a, B b){
            return new TwoTuple(a,b);
        }
        //-----------------------------------------------------
        public static <A,B,C> ThreeTuple tuple(A a, B b, C c){
            return new ThreeTuple(a, b, c);
        }
        //---------------------------------------------------------
        public static <A,B,C,D> FourTuple tuple(A a, B b, C c, D d){
            return new FourTuple(a, b, c, d);
        }
        //-------------------------------------------------------------
        public static <A,B,C,D,E> FiveTuple tuple(A a, B b, C c, D d, E e){
            return new FiveTuple(a, b, c, d, e);
        }
    }
    //************************************************************************
    public class TupleTest{
        //----------------------------------
        static TwoTuple f(){
            return Tuple.tuple("hi", 47);
        }
        //-----------------------------------------
        static TwoTuple f2(){ return Tuple.tuple("hi", 47);}
        //------------------------------------------------------
        static ThreeTuple<Amphibian, String, Integer> g(){
            return Tuple.tuple(new Amphibian(), "hi", 47);
        }
        //---------------------------------------------------------
        static FourTuple<Vehicle, Amphibian, String, Integer> h(){
            return Tuple.tuple(new Vehicle(), new Amphibian(), "hi", 47);
        }
        //-----------------------------------------------------------------
        static FiveTuple k(){
            return Tuple.tuple(new Vehicle(), new Amphibian(), "hi", 47, 11.1);
        }
        //----------------------------------------------------------------------
        static void test(){
            TwoTuple ttsi = f();
            System.out.println(ttsi);
            System.out.println(f2());
            System.out.println(g());
            System.out.println(h());
            System.out.println(k());
        }
    }
    //***********************************************************************
    class ArrayMaker<T> {
        private Class<T> kind;
        //-------------------------------------
        public ArrayMaker(Class<T> kind){
            this.kind = kind;
        }
        //----------------------------------
        T[] create(int size){
            return (T[])Array.newInstance(kind, size);
        }
        //------------------------------------------------
        public static void test(){
            ArrayMaker<String> stringMaker = new ArrayMaker(String.class);
            String[] stringArray = stringMaker.create(9);
            System.out.println(Arrays.toString(stringArray));
            
            String[] strA = (String[])Array.newInstance(String.class, 3);
            System.out.println(Arrays.toString(strA));
        }
    }
    //*************************************************************************
    public class ErasedTypeTest{
        public static void main(String[] args){
            Class c1 = new ArrayList<String>().getClass();
            System.out.println(c1);
            
            List<String> list = new ArrayList();
            list.add("test1");
            System.out.println(list.getClass());
            System.out.println(list.get(0).getClass());
            System.out.println(list.getClass().getTypeParameters());
            System.out.println(Arrays.toString(list.getClass().getTypeParameters()));
            
            Type1<T1> t1 = new Type1(new T1());
            System.out.println(t1.getClass());
            System.out.println(t1.getT1().getClass());
            
            Type1<String> t2 = new Type1(new String());
            System.out.println(t2.getClass());
            System.out.println(t2.getT1().getClass());
        }
        //****************************************************************************
        public static class T1 {
            public void test1(){
                System.out.println("T1.test1()");
            }
        }
        //***********************************************
        public static class Type1<T>{
            private T t1 ;
            
            public Type1(){
                //t1 = new T();
            }
            
            public Type1(T in){
                t1 = in;
                if(t1 instanceof T1)
                    ((T1)t1).test1();
                System.out.println("inside Type1(T in):");
                System.out.println(t1.getClass());
            }
            public T getT1(){
                return t1;
            }
        }
    }
    //***********************************************************
    public class ListOfGenerics<T>{
        private List<T> array = new ArrayList();
        public void add(T item) { array.add(item); }
        public T get(int index) { return array.get(index); }
    }
    //***************************************************************
    public class Generic<T> {}
    //******************************
    public class ArrayOfGenericReference {
        static Generic<Integer>[] gia;
    }
    //**********************************************
    public class ArrayOfGeneric {
        static final int SIZE = 100;
        static Generic<Integer>[] gia;
        
        public static void main(String[] args){
            gia = new Generic[SIZE];
            System.out.println(gia.getClass().getSimpleName());
            gia[0] = new Generic<Integer>();
            System.out.println(gia[0].getClass());
        }
    }
    //******************************************
    public class GenericArray2<T>{
        private Object[] array;
        public GenericArray2(int sz){
            array = new Object[sz];
        }
        //------------------------------
        public void put(int index, T item){
            array[index] = item;
        }
        //---------------------------------------
        public T get(int index) { return (T)array[index]; }
        
        public T[] rep(){ return (T[])array; }
        
        public static void main(String[] args){
            GenericArray2<Integer> gai = new GenericArray2<Integer>(10);
            for(int i=0; i < 10; i++)
                gai.put(i, i);
            
            for(int i=0; i < 10; i++)
                System.out.print(gai.get(i) + " ");
            
            System.out.println();
            
            try{
                Integer[] ia = gai.rep();
            }catch(Exception e){
                System.out.println(e);
            }
        }
    }
    //********************************************************************
    public class GenericArrayWithTypeToken<T> {
        private T[] array;
        public GenericArrayWithTypeToken(Class<T> type, int sz){
            array = (T[])Array.newInstance(type, sz);
        }
        //---------------------------------------------------------
        public void put(int index, T item){
            array[index] = item;
        }
        //--------------------------------------------
        public T[] rep() { return array; }
        //---------------------------------------
        public static void main(String[] args) {
            GenericArrayWithTypeToken<Integer> gai = new GenericArrayWithTypeToken(Integer.class, 10);
            Integer[] ia = gai.rep();
            
            for(int i=0; i<ia.length; i++)
                System.out.print(ia[i] + ", ");
        }
    }
    //******************************************************************************
    class Fruit{
        void test1() { System.out.println("Fruit.test1()" ); }
    }
    class Apple extends Fruit{
        void test2() { System.out.println("Apple.test2()"); }
    }
    
    //*************************************
    class GenericAndCovariance{
        public static void main(String[] args){
            List<? super Fruit> flist = new ArrayList();
            
            flist.add(new Apple());
            flist.add(new Fruit());
            
            Iterator<? super Fruit> it = flist.iterator();
            while(it.hasNext()){
                Object f = it.next();
                if(f instanceof Apple){
                    Apple f1 = (Apple) f;
                    f1.test2();
                }  
                
                System.out.println(f.getClass());
            }          
        }
    }
    //*******************************************************
    public class Holder<T> {
        private T value;
        public Holder() {}
        public Holder(T val) { value = val; }
        
        public T get() { return value; }
        public void set(T val) { value = val; }
        
        @Override
        public boolean equals(Object obj) { return value.equals(obj); }
        //----------------------------------------------------------------
        public static void main(String[] args){
            Holder<Apple> apple = new Holder(new Apple());
            Apple d = apple.get();
            apple.set(d);
            
            //Holder<? extends Fruit> fruit = apple;
            
        }
    }
    //*********************************************************************
    public class GenericReading{
        static <T> T readExact(List<T> list){
            return list.get(0);
        }
        //--------------------------------------
        static List<Apple> apples = Arrays.asList(new Apple());
        static List<Fruit> fruit = Arrays.asList(new Fruit());
        
        static void f1(){
            System.out.println("f1()");
            Apple a = readExact(apples);
            System.out.println(a.getClass());
            Fruit f = readExact(fruit);
            System.out.println(f.getClass());
            f = readExact(apples);
            System.out.println(f.getClass());
        }
        
        static class Reader<T> {
            T readExact(List<T> list) { return list.get(0); }
        }
        
        static void f2(){
            System.out.println("f2()");
            Reader<Fruit> fruitReader = new Reader<Fruit>();
            Fruit f = fruitReader.readExact(fruit);
            System.out.println(f.getClass());
        }
        
        static class CovariantReader<T> {
            T readCovariant(List<? extends T> list){
                return list.get(0);
            }
        }
        
        static void f3(){
            System.out.println("f3()");
            CovariantReader<Fruit> fruitReader = new CovariantReader<Fruit>();
            Fruit f = fruitReader.readCovariant(fruit);
            System.out.println(f.getClass());
            Fruit a = fruitReader.readCovariant(apples);
            System.out.println(a.getClass());
        }
        
        public static void main(String[] args){
            f1(); f2(); f3();
        }
    }
}
