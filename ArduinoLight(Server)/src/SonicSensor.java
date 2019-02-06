

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.Session;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
                    sendGet("ON");
                    System.out.println("LED=ON");
                    isOn = true;
                }
            } else {
                if (isOn) {
                    sendGet("OFF");
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

    private void sendGet(String stat) throws Exception {

        String lightUrl = "";

        if (stat.equals("ON")) {
            lightUrl = "http://192.168.9.7/LED=ON";
        } else if (stat.equals("OFF")) {
            lightUrl = "http://192.168.9.7/LED=OFF";
        }

        URL obj = new URL(lightUrl);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("GET");

        System.out.println(con.getResponseCode());
//        System.out.println("Response Code : " + responseCode);
//
//        BufferedReader in = new BufferedReader(
//                new InputStreamReader(con.getInputStream()));
//        String inputLine;
//        StringBuffer response = new StringBuffer();
//
//        while ((inputLine = in.readLine()) != null) {
//            response.append(inputLine);
//        }
//        in.close();

    }

    private static void broadcast(String msg) {
        Set<Session> userSessions = ServerEndPoint.getUserSessions();
        for (Session session : userSessions) {
            session.getAsyncRemote().sendText(msg);
        }
    }
}
