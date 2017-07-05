/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thinkinginjava;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author MikeX
 */
public interface ChapterException {
    static void main(String[] args){
        int caseID = 1;
        switch(caseID){
            case 0:
                StormyInning.main(args);
                break;
            case 1:
                Human.main(args);
                break;
            case 2:
                break;
            default:
                break;
        }
    }
    //**************************************************
    class BaseballException extends Exception {}
    class Foul extends BaseballException {}
    class Strike extends BaseballException {}
    //**************************************************
    abstract class Inning{
        public Inning() throws BaseballException {}
        public void event() throws BaseballException{ }
        public abstract void atBat() throws Strike, Foul;
        public void walk() {}
    }
    //**********************************************************
    class StormException extends Exception {}
    class RainedOut extends StormException {}
    class PopFoul extends Foul {}
    //****************************************************
    interface Storm {
        public void event() throws RainedOut;
        public void rainHard() throws RainedOut;
    }
    //*****************************************************
    public class StormyInning extends Inning implements Storm {
        public StormyInning() throws RainedOut, BaseballException {}
        public StormyInning(String s) throws Foul, BaseballException {}
        
        //void walk() throws PopFoul {} 
        @Override
        public void rainHard() throws RainedOut {}
        @Override
        public void event() {}
        @Override
        public void atBat() throws PopFoul {}
        
        public static void main(String[] args){
            try{
                StormyInning si = new StormyInning();
                si.atBat();
                throw new RainedOut();
                
            } catch(PopFoul e) {
                System.out.println("Pop foul");
                //Logger.getLogger(ChapterException.class.getName()).log(Level.SEVERE, null, ex);
            } catch (RainedOut ex) {
                System.out.println("Rained Out");
                //throw ex;
            } 
            catch(BaseballException e){
                System.out.println("Generic baseball exception");
            } finally{
                System.out.println("all");
            }
            
            try{
                Inning i = new StormyInning();
                i.atBat();
                throw new Strike();
            } catch(Strike e){
                System.out.println("Strike");
            } catch(Foul e){
                System.out.println("Foul");
            }
            catch (RainedOut | BaseballException ex) {
                System.out.println("Rainedout | Baseball exceptions");
                //Logger.getLogger(ChapterException.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    //***********************************************************************************
    class Annoyance extends Exception {}
    class Sneeze extends Annoyance {}
    
    public class Human{
        public static void main(String[] args){
            try{
                throw new Sneeze();
            }catch(Sneeze s){
                System.out.println("Caught Sneeze");
            }catch(Annoyance a){
                System.out.println("Caught Annoyance");
            }
            
            try{
                throw new Sneeze();
            }catch(Annoyance a){
                System.out.println("Caught Annoyance");
            }            
        }
    }
}
