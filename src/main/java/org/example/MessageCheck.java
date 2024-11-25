package org.example;

public class MessageCheck {
    public static int checkMessage(String message, MySQL mySQL) {
        int type ;
        String[] words = message.split(" ");
        try {
            type = Integer.parseInt(words[0]);
        }catch(Exception e) {
            return -1;
        }
        switch (type) {
            case 0:
                return mySQL.checkType0And1(words[1],words[2],0);
            case 1:
                return mySQL.checkType0And1(words[1],words[2],1);
            case 2:
                return mySQL.checkType2(words[1],Integer.parseInt(words[2]));
            case 3:
                return mySQL.checkType3(words[1],words[2]);
            case 4:
                return mySQL.checkType4(words[1],words[2]);
            case 5:
                return mySQL.checkType5(words[1],words[2],words[3]);
            default:
                return -1;
        }
    }

}
