/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thinkinginjava;

import java.util.Scanner;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author MikeX
 */
public interface ChapterStrings {
    static void main(String[] args){
        int caseID = 1;
        switch(caseID){
            case 0:
                Resetting.main(args);
                break;
            case 1:
                ThreatAnalyzer.main(args);
                break;
            case 2:
                break;
            default:
                break;
        }
    }
    //*************************************
    public class Resetting{
        public static void main(String[] args){
            Matcher m = Pattern.compile("[wrb][aiu][tgx]").matcher("fix the rug with bags");
            while(m.find())
                System.out.println(m.group() + " ");
            
        }
    }
    //**********************************************************************************
    public class ThreatAnalyzer{
        static String threatData = ".27.82.161@02/10/2005\n" +
                "205.56.234.40@02/11/2005\n" + "[next log]";
        
        public static void main(String[] args){
            Scanner scanner = new Scanner(threatData);
            String pattern = "(\\d*+[.]\\d*+[.]\\d*+[.]\\d+)@" + "(\\d{2}/\\d{2}/\\d{4})";
            
            while(scanner.hasNext(pattern)){
                scanner.next(pattern);
                MatchResult match = scanner.match();
                String ip = match.group(1);
                String date = match.group(2);
                System.out.format("Threat on %s from %s\n", date, ip);
            }
        }
    }
}
