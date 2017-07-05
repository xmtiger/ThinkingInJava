/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thinkinginjava;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author MikeX
 */
public class ThinkingInJava {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, FileNotFoundException, ClassNotFoundException, Exception {
        // TODO code application logic here
        int caseId = 730;
        switch(caseId){
            case 0:
                ChapterGenerics.test(args);
                break;
            case 1:
                ChapterContainersInDepth.test();
                break;
            case 672:
                ChapterIO.test(args);
                break;
            case 765:
                ChapterAnnotation.main(args);
                break;
            case 338:
                ChapterException.main(args);
                break;
            case 383:
                ChapterStrings.main(args);
                break;
            case 396:
                ChapterTypeInformation.main(args);
                break;
            case 730:
                ChapterEnum.mian(args);
                break;
            default:
                break;
        }
    }
    
}
