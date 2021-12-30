import java.io.*;
import com.umarabdul.socketwrapper.SocketWrapper;


public class App{

  public static void main(String[] args){

    // A demo of using the library to communicate with a service.
    // Start a TCP listener on port 5555, and run the code.
    try{
      SocketWrapper sock = new SocketWrapper("localhost", 5555, false);
      sock.write("What is your name? ");
      String name = sock.read(100);
      if (name != null)
        sock.write("Nice to meet you, " + name + "\n");
      sock.close();
    }catch(IOException e){}
  }
}
