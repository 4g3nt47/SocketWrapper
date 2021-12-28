import java.io.*;
import com.umarabdul.socketwrapper.SocketWrapper;


public class App{

  public static void main(String[] args){

    // A simple demo of using the library to communicate with a service.
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