package com.tehvilla.apps.tehvilla.helpers;

import android.app.Activity;
import android.widget.Toast;

import com.pixplicity.easyprefs.library.Prefs;
import com.tehvilla.apps.tehvilla.models.Login;
import com.tehvilla.apps.tehvilla.models.Member;
import com.tehvilla.apps.tehvilla.models.Verifikasi;

import java.util.List;

public class Helpers {

    public static void setIntro(boolean intro){
        Prefs.putBoolean("SudahIntro", intro);
    }

    public static boolean getIntro(){
        return Prefs.getBoolean("SudahIntro", false);
    }

    public static void delIntro(){
        Prefs.putBoolean("SudahIntro", false);
    }

    public static void setActiveMember(List<Member> member){
        Prefs.putString("Memberkode", member.get(0).getKode());
        Prefs.putString("Membernama", member.get(0).getNamaLengkap());
        Prefs.putString("Memberjenkel", member.get(0).getJenisKelamin());
        Prefs.putString("Membertanggallahir", member.get(0).getTanggalLahir());
        Prefs.putString("Memberalamat", member.get(0).getAlamat());
        Prefs.putString("Memberprovinsi", member.get(0).getProvinsi());
        Prefs.putString("Membertelepon", member.get(0).getTelepon());
        Prefs.putString("Memberkota", member.get(0).getKota());
        Prefs.putString("Memberkodepos", member.get(0).getKodePos());
        Prefs.putString("Memberphoto", member.get(0).getUrlGambar());
        Prefs.putString("Memberemail", member.get(0).getEmail());
        Prefs.putString("Memberlasttoken", member.get(0).getLastToken());
        Prefs.putBoolean("IsLoggedIn", true);
    }

    public static void resetActiveMember(){
        Prefs.putString("Memberkode", "");
        Prefs.putString("Membernama", "");
        Prefs.putString("Memberjenkel", "");
        Prefs.putString("Membertanggallahir", "");
        Prefs.putString("Memberalamat", "");
        Prefs.putString("Memberprovinsi", "");
        Prefs.putString("Membertelepon", "");
        Prefs.putString("Memberkota", "");
        Prefs.putString("Memberkodepos", "");
        Prefs.putString("Memberphoto", "");
        Prefs.putString("Memberemail", "");
        Prefs.putString("Memberlasttoken", "");
        Prefs.putBoolean("IsLoggedIn", false);
    }
}
