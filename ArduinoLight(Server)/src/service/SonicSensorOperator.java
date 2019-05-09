package service;

import components.Sensor;
import controller.LightSensorController;
import mac_ip.NodemcuTable;
import socket_controller.ServerEndPoint;

import javax.websocket.Session;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.Future;

public class SonicSensorOperator {

    private Sensor sensor;
    private int distanceConst = NodemcuTable.getDistance();
    //    private NodemcuTable nodemcuTable = new NodemcuTable();
    private LightSensorController lightSensorController = new LightSensorController();

    public void switchLights(String ip1, String ip2, String distance, int position) {
        try {
            if (Integer.parseInt(distance) < distanceConst) {
                if (ip1 != null) {
                    sensor = NodemcuTable.getSensors().get(position - 1);
                    if (sensor.getCount() > 0) {
                        sensor.setCount(sensor.getCount() - 1);
                    }
                    if (sensor.getCount() == 0) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1000);
                                    lightSensorController.sendGetToLights(ip1, 0);
                                    System.out.println("LED=OFF");
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
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

    public static void broadcast(String msg) {
        System.out.println('A' + msg);
        Set<Session> userSessions = ServerEndPoint.getUserSessions();
        synchronized (userSessions) {
            for (Session session : userSessions) {
                System.out.println(session.isOpen());
                if (session.isOpen()) {
                    session.getAsyncRemote().sendText(msg);
//                        session.close();
                }
            }
        }
        System.out.println('B' + msg);
    }

    public String getVehicleCount() {
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
