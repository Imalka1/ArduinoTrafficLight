import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.Session;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Set;

@WebServlet(urlPatterns = "/sonicDistance")
public class SonicSensor extends HttpServlet {

    private boolean isOn;
    private int count;
    private int distanceConst = 30;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String position = req.getParameter("position");
//        System.out.println(req.getParameter("mac"));
        if (position.equals("getDistance")) {
            resp.setContentType("text/plain");
            PrintWriter out = resp.getWriter();
            out.println(distanceConst + "");
        } else {
            try {
                if (Integer.parseInt(position) == 1) {
                    sendGetToLights("ON");
                    System.out.println("LED=ON");
                } else if (Integer.parseInt(position) == 0) {
                    sendGetToLights("OFF");
                    System.out.println("LED=OFF");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            count++;
            position = position + "&" + count + "&" + isOn;
            broadcast(position);
        }
    }

    private void sendGetToLights(String stat) throws Exception {
        String lightUrl = "";
        if (stat.equals("ON")) {
            lightUrl = "http://192.168.9.7/led_on";
        } else if (stat.equals("OFF")) {
            lightUrl = "http://192.168.9.7/led_off";
        }
        URL obj = new URL(lightUrl);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        try {
            System.out.println(con.getResponseCode());
        } catch (Exception ex) {
            System.out.println("Something went wrong");
        }
    }

    private static void broadcast(String msg) {
        Set<Session> userSessions = ServerEndPoint.getUserSessions();
        for (Session session : userSessions) {
            session.getAsyncRemote().sendText(msg);
        }
    }

    private String getPlaceViaMac(String mac) {
        switch (mac) {
            case "80:7D:3A:7F:F4:27":
                return "Galle1";
            case "80:7D:3A:7F:F3:77":
                return "Galle2";
            default:
                return "";
        }
    }
}
