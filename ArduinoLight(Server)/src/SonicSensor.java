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

    private int distanceConst = MacIpTable.getDistance();
    private static int[] countSensor = {0, 0, 0};
    private LightSensorController lightSensorController = new LightSensorController();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String distance = req.getParameter("distance");
        String mac = req.getParameter("mac");
//        System.out.println(mac);
        String[] sensor = MacIpTable.getSensorViaMac(mac);
        switch (sensor[1]) {
            case "sensor1":
                switchLights(MacIpTable.getIpOfLight("light0"), MacIpTable.getIpOfLight("light1"), countSensor, 0, 0, distance, sensor);
                break;
            case "sensor2":
                switchLights(MacIpTable.getIpOfLight("light1"), MacIpTable.getIpOfLight("light2"), countSensor, 0, 1, distance, sensor);
                break;
            case "sensor3":
                switchLights(MacIpTable.getIpOfLight("light2"), MacIpTable.getIpOfLight("light3"), countSensor, 1, 2, distance, sensor);
                break;
        }
    }

    private void switchLights(String ip1, String ip2, int[] countSensor, int pos11, int pos21, String distance, String[] sensor) {
        try {
            if (Integer.parseInt(distance) < distanceConst) {
                if (ip1 != null) {
                    if (countSensor[pos11] > 0) {
                        countSensor[pos11]--;
                    }
                    if (countSensor[pos11] == 0) {
                        lightSensorController.sendGetToLights(ip1, 0);
                        System.out.println("LED=OFF");
                    }
                }
                if (ip2 != null) {
                    countSensor[pos21]++;
                    lightSensorController.sendGetToLights(ip2, 1);
                    System.out.println("LED=ON");
                }
                String message = sensor[1].substring(6);
                if (Integer.parseInt(message) == 1) {
                    message = "{\"sensor\":\"" + message + "\",\"segment\":\"" + sensor[0].substring(3) + "\",\"curCount\":\"" + countSensor[Integer.parseInt(message) - 1] + "\",\"preCount\":\"0\",\"error\":\"Not found\"}";
                    System.out.println(message);
                } else {
                    message = "{\"sensor\":\"" + message + "\",\"segment\":\"" + sensor[0].substring(3) + "\",\"curCount\":\"" + countSensor[Integer.parseInt(message) - 1] + "\",\"preCount\":\"" + countSensor[Integer.parseInt(message) - 2] + "\",\"error\":\"Not found\"}";
                    System.out.println(message);
//                    message += "&" + sensor[0].substring(3) + "&" + countSensor[Integer.parseInt(message) - 1] + "&" + countSensor[Integer.parseInt(message) - 2];
                }
//                System.out.println(message);
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

    public static String getMessage() {
        String message = "{\"sensorData\":[";
        for (int i = 0; i < MacIpTable.getSegmentSensorsCount().length; i++) {
//            message += "\"segment\":";
            for (int j = 0; j < MacIpTable.getSegmentSensorsCount()[0]-1; j++) {
                if ((j + 1) == 1) {
                    message += "{\"sensor\":\"" + (j + 1) + "\",\"segment\":\"" + (i + 1) + "\",\"curCount\":\"" + countSensor[j] + "\",\"preCount\":\"0\",\"error\":\"Not found\"}";
                    if (j != MacIpTable.getSegmentSensorsCount()[0] - 2) {
                        message += ",";
                    }
//                    message += (j + 1) + "&" + (i + 1) + "&" + countSensor[j] + "&0";
                } else {
                    message += "{\"sensor\":\"" + (j + 1) + "\",\"segment\":\"" + (i + 1) + "\",\"curCount\":\"" + countSensor[j] + "\",\"preCount\":\"" + countSensor[j - 1] + "\",\"error\":\"Not found\"}";
                    if (j != MacIpTable.getSegmentSensorsCount()[0] - 2) {
                        message += ",";
                    }
//                    message += (j + 1) + "&" + (i + 1) + "&" + countSensor[j] + "&" + countSensor[j - 1];
                }
            }
            message += "]}";
        }
        System.out.println(message);
        return message;
    }
}
