package controller;

import java.net.HttpURLConnection;
import java.net.URL;

public class LightSensorController {
    public void sendGetToLights(String ip, int status) throws Exception {
        String lightUrl = "";
        if (status == 1 && ip != null) {
            lightUrl = "http://" + ip + "/led_on";
        } else if (status == 0 && ip != null) {
            lightUrl = "http://" + ip + "/led_off";
        }

        URL obj = new URL(lightUrl);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        try {
            con.getResponseCode();
        } catch (Exception ex) {
            System.out.println("Something went wrong on lights");
        }
    }

    public void setProperties(String ip, int distance, int delayTime) throws Exception {
        String lightUrl = "http://" + ip + "/sensorBody?distance=" + distance + "&delayTime=" + delayTime;

        URL obj = new URL(lightUrl);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        try {
            con.getResponseCode();
        } catch (Exception ex) {
            System.out.println("Something went wrong on sensors");
        }
    }
}
