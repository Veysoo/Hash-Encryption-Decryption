package com.company;

import javax.crypto.*;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Main {





    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {


        String dosya_path = "C:\\Users\\Veysel\\Desktop\\albil_staj.txt";
        String dosya_path_hash = "C:\\Users\\Veysel\\Desktop\\abil_staj_hash.txt";


        //Hash kodu üretme

        MessageDigest digest = MessageDigest.getInstance("SHA-256"); //hash için SHA-256 kullandım
        FileInputStream fis = new FileInputStream(dosya_path);
        byte[] buffer = new byte[8192];
        int bytesRead;

        while ((bytesRead = fis.read(buffer)) != -1) {
            digest.update(buffer, 0, bytesRead);
        }

        fis.close();

        byte[] encodedHash = digest.digest();

        // Byte dizisini hex formata dönüştürmek için Kullandım

        StringBuilder hexString = new StringBuilder();
        for (byte b : encodedHash) {
            String hex = String.format("%02x", b);
            hexString.append(hex);
        }

        String hashValue = hexString.toString();
        System.out.println("Hash Değeri: " + hashValue);

        //Dosyaya Hash kodunu yazma

        File file = new File(dosya_path_hash);
        if(!file.exists()){
            System.out.println("DOSYA YOK");
        }

        FileWriter writer1 = new FileWriter(file,true);
        BufferedWriter writer = new BufferedWriter(writer1);
        String yazdir = "Hash Değeri : " + hashValue;
        writer.write(yazdir);
        writer.close();

        //albil_staj_txt dosyasının şifreleme işlemi

        String hedef_dosya_path = "C:\\Users\\Veysel\\Desktop\\abil_staj_hash.txt";
        String sifrelenmis_dosya_path = "C:\\Users\\Veysel\\Desktop\\abil_staj_hash.enc";

        SecretKey secretKey = secretKey_Olusturma();
        sifrelemeIslemi(hedef_dosya_path,sifrelenmis_dosya_path,secretKey);

        //albil_staj içeriğini değiştirme

        File file_1 = new File(dosya_path);
        if (!file_1.exists()){
            System.out.println("Dosya bulunamadı");
        }

        FileWriter writer2 = new FileWriter(file_1);
        BufferedWriter writer3 = new BufferedWriter(writer2);
        String yazdir1 = "değiştirildi";
        writer3.write(yazdir1);
        writer3.close();


        //değiştirilmiş dosyanın hashi. Yukarıda yaptığım hash işleminin aynısı sadece değişken isimlerine 1 veya 2 dedim

        MessageDigest digest1 = MessageDigest.getInstance("SHA-256"); //hash için SHA-256 kullandım
        FileInputStream fis1 = new FileInputStream(dosya_path);
        byte[] buffer2 = new byte[8192];
        int bytesRead2;

        while ((bytesRead2 = fis1.read(buffer2)) != -1) {
            digest.update(buffer2, 0, bytesRead2);
        }

        fis1.close();

        byte[] encodedHash1 = digest1.digest();

        // Byte dizisini hex formata dönüştürmek için Kullandım

        StringBuilder hexString1 = new StringBuilder();
        for (byte b : encodedHash1) {
            String hex1 = String.format("%02x", b);
            hexString1.append(hex1);
        }

        String hashValue_yeni = hexString1.toString(); //hashValue_yeni değişkenin tutuyorum yeni dosyaya yazdırmadım
        System.out.println("Yeni Hash Değeri: " + hashValue_yeni);

        //Decryption işlemi

        String cözümlenmis_dosya_path = "C:\\Users\\Veysel\\Desktop\\abil_staj_hash_1.txt";

        sifreCozmeIslemi(sifrelenmis_dosya_path,cözümlenmis_dosya_path,secretKey);


        //Hash Karşılaştırma işlemi

        if(hashValue.equals(hashValue_yeni)){
            System.out.println("Dosya Değiştirilmemiş");
        }else{
            System.out.println("Dosya Değiştirilmiş");
        }


    }


    public static SecretKey secretKey_Olusturma() throws NoSuchAlgorithmException {

        SecretKey secretKey = null;
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128); // 128-bit AES anahtarı
        secretKey = keyGenerator.generateKey();

        return secretKey;

    }

    public static void sifrelemeIslemi(String inputFile, String outputFile, SecretKey secretKey) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, IOException {

        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        FileInputStream fis = new FileInputStream(inputFile);
        FileOutputStream fos = new FileOutputStream(outputFile);
        CipherOutputStream cos = new CipherOutputStream(fos, cipher);

        byte[] buffer = new byte[8192];
        int bytesRead;

        while ((bytesRead = fis.read(buffer)) != -1) {
            cos.write(buffer, 0, bytesRead);
        }

        fis.close();
        cos.close();

    }


    public static void sifreCozmeIslemi(String inputFile, String outputFile, SecretKey secretKey) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, IOException {

        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        FileInputStream fis = new FileInputStream(inputFile);
        CipherInputStream cis = new CipherInputStream(fis, cipher);
        FileOutputStream fos = new FileOutputStream(outputFile);

        byte[] buffer = new byte[8192];
        int bytesRead;

        while ((bytesRead = cis.read(buffer)) != -1) {
            fos.write(buffer, 0, bytesRead);
        }

        cis.close();
        fos.close();

    }


}
