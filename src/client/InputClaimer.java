package client;

import java.util.ArrayList;
import java.util.Scanner;

public class InputClaimer {
    private static boolean scriptFlag = false;
    private static String scriptFile;
    private static ArrayList<String> scriptList = new ArrayList<>();
    private static int scriptCounter = 0;
    private static int commandCounter = 0;

    public static void setScriptFlag(boolean flag){scriptFlag = flag;}
    public static ArrayList<String> getScriptList(){return scriptList;}
    public static void incScriptCounter(){scriptCounter += 1;}

    public static String input() {
        if(!scriptFlag) {
            Scanner in = new Scanner(System.in);
            return (in.nextLine());
        } else {
            if(scriptCounter!=0){
                commandCounter += 1;
                scriptCounter -= 1;
                return (scriptList.get(commandCounter-1));
            } else {
                scriptFlag = false;
                scriptList.clear();
                scriptCounter = 0;
                commandCounter = 0;
                Scanner in = new Scanner(System.in);
                return (in.nextLine());
            }
        }
    }
}
