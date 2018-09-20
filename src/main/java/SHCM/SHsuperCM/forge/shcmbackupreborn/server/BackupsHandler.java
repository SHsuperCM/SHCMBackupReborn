package SHCM.SHsuperCM.forge.shcmbackupreborn.server;

import java.util.Calendar;

public class BackupsHandler {
    public static String getBackupNameFor(String name, String comment) {
        Calendar c = Calendar.getInstance();
        return String.format("%tY.%tm.%td.%tH.%tM.%tS↨%s↨%s",c,c,c,c,c,c,name,comment);
    }
}
