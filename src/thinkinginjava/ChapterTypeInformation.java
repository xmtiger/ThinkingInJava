/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thinkinginjava;

/**
 *
 * @author MikeX
 */
public interface ChapterTypeInformation {
    static void main(String[] args){
        int caseID = 0;
        switch(caseID){
            case 0:
                SweetShop.main(args);
                break;
            case 1:
                break;
            case 2:
                break;
            default:
                break;
        }
    }
    //****************************************************
    class Gum{
        static { System.out.println("Loading Gum"); }
        Gum() { System.out.println("Inside Constructor"); }
    }
    //******************************************************
    class SweetShop{
        public static void main(String[] args){
            new Gum();
            try{
                String fullName = SweetShop.class.getName();
                String simpleName = SweetShop.class.getSimpleName();
                int index = fullName.indexOf(simpleName);
                String path = fullName.substring(0, index);
                System.out.println("fullName:" + fullName);
                System.out.println("simapleName: " + simpleName);
                System.out.println("path: " + path);
                
                String cName = SweetShop.class.getCanonicalName();
                System.out.println("canonical Name: " + cName);
                
                Class.forName(path + "Gum");
                
                Class<? extends Number> gNum = int.class;
                System.out.println(gNum.toString());
                
            }catch(ClassNotFoundException e){
                System.out.println("Could not find Gum");
            }
        }
    }
}
