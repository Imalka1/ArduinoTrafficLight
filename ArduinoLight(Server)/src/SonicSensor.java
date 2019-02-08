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
    private int distanceConst = 20;
    private int[][] countSensor = {{1, 0}, {2, 0}, {3, 0}};
    private LightController lightController = new LightController();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String distance = req.getParameter("distance");
        String mac = req.getParameter("mac");
//        System.out.println(mac);
        if (distance.equals("getDistance")) {
            resp.setContentType("text/plain");
            PrintWriter out = resp.getWriter();
            out.println(distanceConst + "");
        } else {
            switch (getSensorViaMac(mac)) {
                case "sensor1":
                    switchLights(lightController.getIpViaLight("light1"), lightController.getIpViaLight("light0"), countSensor, 0, 1, 0, 1, distance);
                    break;
                case "sensor2":
                    switchLights(lightController.getIpViaLight("light2"), lightController.getIpViaLight("light1"), countSensor, 0, 1, 1, 1, distance);
                    break;
                case "sensor3":
                    switchLights(lightController.getIpViaLight("light3"), lightController.getIpViaLight("light2"), countSensor, 1, 1, 2, 1, distance);
                    break;
            }
            broadcast(distance);
        }
    }

    private void switchLights(String ip1, String ip2, int[][] countSensor, int pos11, int pos12, int pos21, int pos22, String distance) {
        try {
            if (Integer.parseInt(distance) < distanceConst && pos11 == pos21) { //Start Position
                countSensor[pos11][pos12]++;
                lightController.sendGetToLights(ip1, 1);
                System.out.println("LED=ON1");
            } else if (Integer.parseInt(distance) < distanceConst && pos11 != pos21) {
                if(countSensor[pos11][pos12]>0){
                    countSensor[pos11][pos12]--;
                }
                countSensor[pos21][pos22]++;
                lightController.sendGetToLights(ip1, 1);
                System.out.println("LED=ON");
//                sendGetToLights(ip2, 1);
                if (countSensor[pos11][pos12] == 0) {
                    lightController.sendGetToLights(ip1, 0);
//                    distance = distance + "&false";
                    System.out.println("LED=OFF");
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

    private String getSensorViaMac(String mac) {
        switch (mac) {
            case "80:7D:3A:7F:F4:27":
                return "sensor1";//Galle 1
            case "3C:71:BF:20:08:9D":
                return "sensor2";//Galle 2
            default:
                return "";
        }
    }
}
