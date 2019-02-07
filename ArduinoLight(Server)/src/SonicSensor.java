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

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String txt = request.getReader().readLine();
        try {
            if (Integer.parseInt(txt.trim()) < 30) {
                if (!isOn) {
                    sendPost("ON");
                    System.out.println("LED=ON");
                    isOn = true;
                }
            } else {
                if (isOn) {
                    sendPost("OFF");
                    System.out.println("LED=OFF");
                    isOn = false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        count++;
        txt = txt + "&" + count + "&" + isOn;
        broadcast(txt);
    }

    private void sendPost(String stat) throws Exception {
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
}
