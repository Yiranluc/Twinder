import com.google.gson.Gson;
import java.io.BufferedReader;
import java.util.Locale;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "SwipeServlet", value = "/SwipeServlet")
public class SwipeServlet extends HttpServlet {

  private static final int SWIPE_URL_PARTS_LENGTH = 2;
  private static final String LEFT = "left";
  private static final String RIGHT = "right";
  private Gson gson;

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse res)
      throws IOException {
    gson = new Gson();
    String urlPath = req.getPathInfo();

    if (urlPath == null || urlPath.isEmpty()) {
      res.setStatus(HttpServletResponse.SC_NOT_FOUND);
      res.getWriter().write("missing parameters");
      res.getWriter().flush();
      return;
    }

    String[] urlParts = urlPath.split("/");
    Swipe swipe = turnRequestBodytoSwipe(req.getReader());
    Message message = new Message();
    res.setContentType("application/json");
    res.setCharacterEncoding("UTF-8");
    if (!isUrlValid(urlParts)) {
      res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      message.setMessage("Invalid inputs");
    }
    else if (!swipe.isSwipeRequestValid()) {
      res.setStatus(HttpServletResponse.SC_NOT_FOUND);
      message.setMessage("User not found");
    } else {
      res.setStatus(HttpServletResponse.SC_CREATED);
      message.setMessage("Write successful");
    }
    System.out.println("Message: " + gson.toJson(message));
    res.getWriter().write(gson.toJson(message));
    res.getWriter().flush();
  }

  private boolean isUrlValid(String[] urlParts) {
    // urlPath  = "/{leftorright}/"
    // urlParts = [, left or right]
    boolean isUrlValid = urlParts.length == SWIPE_URL_PARTS_LENGTH
        && (urlParts[1].toLowerCase(Locale.ROOT).equals(LEFT) ||
        urlParts[1].toLowerCase(Locale.ROOT).equals(RIGHT));
    return isUrlValid;
  }

  public Swipe turnRequestBodytoSwipe(BufferedReader buffIn) throws IOException {
    StringBuilder stringBuilder = new StringBuilder();
    String line;
    while( (line = buffIn.readLine()) != null) {
      stringBuilder.append(line);
    }
    return this.gson.fromJson(stringBuilder.toString(), Swipe.class);
  }
}
