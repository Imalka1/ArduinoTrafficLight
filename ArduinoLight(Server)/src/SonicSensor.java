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

    //    private boolean isOn;
    private int distanceConst = MacIpTable.getDistance();
    private int[][] countSensor = {{1, 0}, {2, 0}, {3, 0}};
    private LightSensorController lightSensorController = new LightSensorController();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String distance = req.getParameter("distance");
        String mac = req.getParameter("mac");
        System.out.println(mac);
        switch (MacIpTable.getSensorViaMac(mac)) {
            case "sensor1":
                switchLights(MacIpTable.getIpOfLight("light0"), MacIpTable.getIpOfLight("light1"), countSensor, 0, 1, 0, 1, distance);
                break;
            case "sensor2":
                switchLights(MacIpTable.getIpOfLight("light1"), MacIpTable.getIpOfLight("light2"), countSensor, 0, 1, 1, 1, distance);
                break;
            case "sensor3":
                switchLights(MacIpTable.getIpOfLight("light2"), MacIpTable.getIpOfLight("light3"), countSensor, 1, 1, 2, 1, distance);
                break;
        }
        broadcast(distance);
    }

    private void switchLights(String ip1, String ip2, int[][] countSensor, int pos11, int pos12, int pos21, int pos22, String distance) {
        try {
            if (Integer.parseInt(distance) < distanceConst) {
                if (ip1 != null) {
                    if (countSensor[pos11][pos12] > 0) {
                        countSensor[pos11][pos12]--;
                    }
                    if (countSensor[pos11][pos12] == 0) {
                        lightSensorController.sendGetToLights(ip1, 0);
                        System.out.println("LED=OFF");
                    }
                }
                if (ip2 != null) {
                    countSensor[pos21][pos22]++;
                    lightSensorController.sendGetToLights(ip2, 1);
                    System.out.println("LED=ON");
                }
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
}
