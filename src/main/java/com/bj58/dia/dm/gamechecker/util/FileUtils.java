package com.bj58.dia.dm.gamechecker.util;

import java.io.*;

public class FileUtils {
    /**
     * 创建文件
     * 目录不存在时递归 创建目录
     * 创建成功 返回true  否则返回false
     * @param pathname
     * @return
     */
    public static boolean createFile(String pathname){
        File file=new File(pathname);
        if(file.exists()){
            return false;
        }

        File parentFile = file.getParentFile();
        if(!parentFile.exists()){
            if(!parentFile.mkdirs()){
                return false;
            }
        }

        try {
            return file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取文件内容
     * @param pathname
     * @return
     */
    public static String fileGetContent(String pathname){
        File file=new File(pathname);

        FileInputStream fileInputStream=null;
        InputStreamReader inputStreamReader=null;
        try {
            StringBuilder stringBuilder=new StringBuilder();
            fileInputStream=new FileInputStream(file);
            inputStreamReader = new InputStreamReader(fileInputStream,"utf8");
            BufferedReader bufferedReader=new BufferedReader(inputStreamReader);
            String line=null;
            while((line=bufferedReader.readLine()) != null){
                stringBuilder.append(line+NEW_LINE);
            }
            return stringBuilder.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            if(null!=fileInputStream){
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            if(null!=inputStreamReader){
                try {
                    inputStreamReader.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }


    private static String NEW_LINE = System.getProperty("line.separator");

    public static String fileGetContent(InputStream inputStream){
        try {
            StringBuilder stringBuilder=new StringBuilder();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream,"utf8");
            BufferedReader bufferedReader=new BufferedReader(inputStreamReader);

            String line=null;
            while((line=bufferedReader.readLine()) != null){
                stringBuilder.append(line+NEW_LINE);
            }
            return stringBuilder.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 写入文件内容
     * @param pathname
     * @param data
     */
    public static void filePutContent(String pathname,String data){
        filePutContent(pathname,data,false);
    }

    /**
     * 写入文件内容 文件不存在时创建（递归创建）
     * @param pathname
     * @param data
     * @param append
     */
    public static void filePutContent(String pathname,String data,boolean append){
        FileOutputStream fileOutputStream=null;
        OutputStreamWriter outputStreamWriter=null;
        try {
            File file=new File(pathname);
            if(!file.exists()){
                createFile(pathname);
            }

            if(null==data){
                data="";
            }

            fileOutputStream=new FileOutputStream(file,append);
            outputStreamWriter = new OutputStreamWriter(fileOutputStream,"utf8");
            outputStreamWriter.write(data);
            outputStreamWriter.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            if(outputStreamWriter!=null){
                try {
                    outputStreamWriter.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }


            if(null!=fileOutputStream){
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }


    /**
     * 删除文件 成功返回true 失败返回false
     * @param pathname
     * @return
     */
    public static boolean deleteFile(String pathname){
        File file=new File(pathname);
        return file.delete();
    }
}
