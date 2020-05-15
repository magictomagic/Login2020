package com.example.login2020.controller;

import android.widget.Toast;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.example.login2020.R;
import com.example.login2020.ui.login.LoginActivity;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class conGetLinkKu {
    final private static String protocalName = "rtmp";
    final private static String IPaddress = "149.248.57.125";
    final private static int ffmpegPort = 1935;
    final private static String applicationName = "vod";

    final private static int defaultSSHport = 22;
    final private static String password = "B2$nBo7!]UkEL1@w";
    final private static String defaultUserName = "root";
    final public static String command = "ls /mnt/mp4s";

    public static int totalVideos;

    public static List<String> videoNameList = new ArrayList<String>();

    private static String protocol(){
        String pro = protocalName;
        return pro;
    }

    private static String address(){
        String adr = IPaddress;
        return adr;
    }

    private static String application(){
        String app = applicationName;
        return app;
    }

    public void convertStringToList(String s) {
        String str[] = s.split("\\s+");
        videoNameList = Arrays.asList(str);
    }

    public void getVideoNameList(){
        JSch jsch = new JSch();
        try {
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            Session session = jsch.getSession(defaultUserName, IPaddress, defaultSSHport);
            session.setPassword(password);
            session.setConfig(config);
            session.connect();
            System.out.println("Connected");

            // Create and connect channel.
            Channel channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand(command);
            channel.setInputStream(null);
            ((ChannelExec)channel).setErrStream(System.err);
            InputStream in=channel.getInputStream();
            channel.connect();

            byte[] tmp=new byte[1024];
            while(true){
                while(in.available()>0){
                    int i=in.read(tmp, 0, 1024);
                    if(i<0)break;
                    convertStringToList(new String(tmp, 0, i));
                }
                if(channel.isClosed()){
                    System.out.println("exit-status: "+channel.getExitStatus());
                    break;
                }
                try{Thread.sleep(1000);}catch(Exception ee){}
            }
            channel.disconnect();
            session.disconnect();

        }catch (JSchException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getVideoName(int index) {
        return videoNameList.get(index);
    }

    public static String jointLink(int index){
        return protocol() + "://" + address() + ":" + ffmpegPort + "/" + application() + "/" +
                getVideoName(index);
    }
}

