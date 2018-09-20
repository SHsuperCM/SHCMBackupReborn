package SHCM.SHsuperCM.forge.shcmbackupreborn.common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Reference {
    public static final String PATH_ROOT_BACKUPS = "\\backups";
    public static final String PATH_WORLDPROFILE = PATH_ROOT_BACKUPS + "\\worldprofile.dat";
    public static MessageDigest messageDigest;{ try { messageDigest = MessageDigest.getInstance("SHA-256"); } catch (NoSuchAlgorithmException e){}}
}
