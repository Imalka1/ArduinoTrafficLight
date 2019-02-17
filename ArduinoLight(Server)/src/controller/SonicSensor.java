package controller;

import components.Sensor;
import mac_ip.NodemcuTable;
import socket_controller.ServerEndPoint;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.Session;
import java.io.*;
import java.util.Set;

@WebServlet(urlPatterns = "/sonicDistance")
public class SonicSensor extends HttpServlet {

    private Sensor sensor;
    private int distanceConst = NodemcuTable.getDistance();
    //    private static int[] countSensor = {0, 0, 0};
    private LightSensorController lightSensorController = new LightSensorController();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getParameter("wifiStatus") != null) {
            Sensor sensor = NodemcuTable.getSensorViaMac(req.getParameter("mac"));
            sensor.setError("Not found(OK)");
            String message = "{\"sensor\":\"" + sensor.getName().substring(6) + "\",\"segment\":\"" + sensor.getSegment().substring(3) + "\",\"errorFound\":\"" + sensor.getError() + "\"}";
            System.out.println(message);
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        Thread.sleep(NodemcuTable.getDelayTime());
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }).start();
            broadcast(message);
        } else {
            String distance = req.getParameter("distance");
//            String mac = req.getParameter("mac");
//        System.out.println(mac);
            Sensor sensor = NodemcuTable.getSensorViaMac(req.getParameter("mac"));
            switch (sensor.getName()) {
                case "sensor1":
                    switchLights(NodemcuTable.getIpOfLight("light0"), NodemcuTable.getIpOfLight("light1"), distance, 0);
                    break;
                case "sensor2":
                    switchLights(NodemcuTable.getIpOfLight("light1"), NodemcuTable.getIpOfLight("light2"), distance, 1);
                    break;
                case "sensor3":
                    switchLights(NodemcuTable.getIpOfLight("light2"), NodemcuTable.getIpOfLight("light3"), distance, 2);
                    break;
            }
        }
    }

    private void switchLights(String ip1, String ip2, String distance, int position) {
        try {
            if (Integer.parseInt(distance) < distanceConst) {
                if (ip1 != null) {
                    sensor = NodemcuTable.getSensors().get(position - 1);
                    if (sensor.getCount() > 0) {
                        sensor.setCount(sensor.getCount() - 1);
                    }
                    if (sensor.getCount() == 0) {
                        lightSensorController.sendGetToLights(ip1, 0);
                        System.out.println("LED=OFF");
                    }
                }
                if (ip2 != null) {
                    sensor = NodemcuTable.getSensors().get(position);
                    sensor.setCount(sensor.getCount() + 1);
                    lightSensorController.sendGetToLights(ip2, 1);
                    System.out.println("LED=ON");
                }
                String message = NodemcuTable.getSensors().get(position).getName().substring(6);
                if (Integer.parseInt(message) == 1) {
                    message = "{\"sensor\":\"" + message + "\",\"segment\":\"" + sensor.getSegment().substring(3) + "\",\"curCount\":\"" + sensor.getCount() + "\",\"preCount\":\"0\",\"errorFound\":\"Not found\"}";
//                    System.out.println(message);
                } else {
                    message = "{\"sensor\":\"" + message + "\",\"segment\":\"" + sensor.getSegment().substring(3) + "\",\"curCount\":\"" + sensor.getCount() + "\",\"preCount\":\"" + NodemcuTable.getSensors().get(position - 1).getCount() + "\",\"errorFound\":\"Not found\"}";
//                    System.out.println(message);
//                    message += "&" + sensor[0].substring(3) + "&" + countSensor[Integer.parseInt(message) - 1] + "&" + countSensor[Integer.parseInt(message) - 2];
                }
                System.out.println(message);
                broadcast(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void broadcast(String msg) {
        Set<Session> userSessions = ServerEndPoint.getUserSessions();
        for (Session session : userSessions) {
            session.getAsyncRemote().sendText(msg);
        }
    }

    public static String getVehicleCount() {
        String message = "{\"sensorData\":[";
        for (int i = 0; i < NodemcuTable.getSegmentSensorsCount().length; i++) {
//            message += "\"segment\":";
            for (int j = 0; j < NodemcuTable.getSegmentSensorsCount()[0]; j++) {
                if ((j + 1) == 1) {
                    message += "{\"sensor\":\"" + (j + 1) + "\",\"segment\":\"" + (i + 1) + "\",\"curCount\":\"" + NodemcuTable.getSensors().get(j).getCount() + "\",\"preCount\":\"0\",\"errorFound\":\"" + NodemcuTable.getSensors().get(j).getError() + "\"}";
                    if (j != NodemcuTable.getSegmentSensorsCount()[0] - 1) {
                        message += ",";
                    }
                } else {
                    message += "{\"sensor\":\"" + (j + 1) + "\",\"segment\":\"" + (i + 1) + "\",\"curCount\":\"" + NodemcuTable.getSensors().get(j).getCount() + "\",\"preCount\":\"" + NodemcuTable.getSensors().get(j - 1).getCount() + "\",\"errorFound\":\"" + NodemcuTable.getSensors().get(j).getError() + "\"}";
                    if (j != NodemcuTable.getSegmentSensorsCount()[0] - 1) {
                        message += ",";
                    }
                }
            }
        }
        message += "]}";
//        System.out.println(message);
        return message;
    }
}
