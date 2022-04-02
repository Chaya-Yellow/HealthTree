package com.jks.Spo2MonitorEx.util;

/**
 * Created by apple on 16/7/16.
 */
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.app.Activity;

public class MD5 {
    private Activity activity;
    private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

    public MD5(Activity activity) {
        super();
        this.activity = activity;
    }

//	public String toHexString(byte[] b) { // String to byte
//		StringBuilder sb = new StringBuilder(b.length * 2);
//		for (int i = 0; i < b.length; i++) {
//			sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
//			sb.append(HEX_DIGITS[b[i] & 0x0f]);
//		}
//		return sb.toString();
//	}

    public MD5() {
        super();
    }

    public String getMD5(String str) {
        StringBuffer hexString = new StringBuffer();
        if (str != null && str.trim().length() != 0) {
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update(str.getBytes());
                byte[] hash = md.digest();
                for (int i = 0; i < hash.length; i++) {
                    if ((0xff & hash[i]) < 0x10) {
                        hexString.append("0"
                                + Integer.toHexString((0xFF & hash[i])));
                    } else {
                        hexString.append(Integer.toHexString(0xFF & hash[i]));
                    }
                }
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        return hexString.toString();
    }

//	public String md5(String s) {
//		try {
//			// Create MD5 Hash
//			MessageDigest digest = java.security.MessageDigest
//					.getInstance("MD5");
//			digest.update(s.getBytes());
//			byte messageDigest[] = digest.digest();
//
//			return toHexString(messageDigest);
//		} catch (NoSuchAlgorithmException e) {
//			e.printStackTrace();
//		}
//
//		return "";
//	}
//
//	public boolean checkPassword(String password, String md5PwdStr) {
//		String s = md5(password);
//		return s.equals(md5PwdStr);
//	}

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

}
