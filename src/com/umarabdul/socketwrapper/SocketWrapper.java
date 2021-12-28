package com.umarabdul.socketwrapper;

import java.io.*;
import java.net.*;
import javax.net.ssl.*;


/**
* A simple wrapper for Java sockets for easier operations.
* @author Umar Abdul
* @version 1.0
* @since 2020
*/

public class SocketWrapper{

  private Socket sock = null;
  private DataInputStream reader;
  private DataOutputStream writer;

  /**
  * Wrap an existing socket.
  * @param sock Connected socket object to wrap.
  * @throws IOException on initialization error.
  */
  public SocketWrapper(Socket sock) throws IOException{

    this.sock = sock;
    reader = new DataInputStream(sock.getInputStream());
    writer = new DataOutputStream(sock.getOutputStream());
  }

  /**
  * Create a socket and wrap.
  * @param host Remote host.
  * @param port Remote port.
  * @param ssl Use SSL.
  * @throws IOException on initialization error.
  */
  public SocketWrapper(String host, int port, boolean ssl) throws IOException{

    if (ssl){
      SSLSocketFactory factory = (SSLSocketFactory)SSLSocketFactory.getDefault();
      sock = (Socket)factory.createSocket(host, port);
      ((SSLSocket)sock).startHandshake();
    }else{
      sock = new Socket(host, port);
    }
    reader = new DataInputStream(sock.getInputStream());
    writer = new DataOutputStream(sock.getOutputStream());
  }

  /**
  * Set socket read timeout.
  * @param timeout Read timeout in milliseconds.
  */
  public void setTimeout(int timeout){

    try{
      sock.setSoTimeout(timeout);
    }catch(Exception e){}
  }

  /**
  * Close the wrapped socket.
  */
  public void close(){

    try{
      sock.close();
    }catch(Exception e){}
  }

  /**
  * Obtain the wrapped socket object.
  * @return Wrapped socket object.
  */
  public Socket getSocket(){
    return sock;
  }

  /**
  * Obtain the instance of {@code DataInputStream} connected to the socket.
  * @return An instance of DataInputStream for reading data from the socket.
  */
  public DataInputStream getReader(){
    return reader;
  }

  /**
  * Obtain the instance of {@code DataOutputStream} connected to the socket.
  * @return An instance of DataOutputStream for writing data to the socket. 
  */
  public DataOutputStream getWriter(){
    return writer;
  }

  /**
  * Obtain the IP of the remote host the wrapped socket is connected to.
  * @return IP address of remote host.
  */
  public String getRhost(){

    String data[] = sock.getRemoteSocketAddress().toString().split("/");
    String rhost = data[data.length-1];
    return rhost.split(":")[0];
  }

  /**
  * Obtain the remote port number the wrapped socket is connected to.
  * @return Port number of remote host.
  */
  public int getRport(){

    String data[] = sock.getRemoteSocketAddress().toString().split(":");
    return Integer.valueOf(data[data.length-1]);
  }

  /**
  * Get local IP of wrapped socket.
  * @return Local IP of wrapped socket.
  */
  public String getLhost(){

    String data[] = sock.getLocalSocketAddress().toString().split("/");
    return data[data.length-1].split(":")[0];
  }

  /**
  * Get local port of wrapped socket.
  * @return Local port of wrapped socket.
  */
  public int getLport(){

    String data[] = sock.getLocalSocketAddress().toString().split(":");
    return Integer.valueOf(data[data.length-1]);
  }
  
  /**
  * Read data from the wrapped socket.
  * @param len Maximum number of bytes to read.
  * @return A string containing data received, empty string on timeout, {@code null} on IO error.
  */
  public String read(int len){

    byte[] data = new byte[len];
    int rlen = 0;
    try{
      rlen = reader.read(data, 0, len);
      if (rlen == -1)
        return null;
      if (rlen == 0)
        return "";
      if (rlen == len)
        return new String(data);
      byte[] newData = new byte[rlen];
      for (int i = 0; i < rlen; i++)
        newData[i] = data[i];
      return new String(newData);
    }catch(SocketTimeoutException e1){
      return "";
    }catch(IOException e2){
      return null;
    }
  }

  /**
  * Read data from the wrapped socket.
  * @param len Maximum number of bytes to read.
  * @return A byte array containing data received, empty array on timeout, {@code null} on IO error.
  */
  public byte[] readBytes(int len){

    byte[] data = new byte[len];
    try{
      int rlen = reader.read(data, 0, len);
      if (rlen == -1)
        return null;
      if (rlen == len)
        return data;
      byte[] newData = new byte[rlen];
      if (rlen == 0)
        return newData;
      for (int i = 0; i < rlen; i++)
        newData[i] = data[i];
      return newData;
    }catch(SocketTimeoutException e1){
      return new byte[0];
    }catch(IOException e2){
      return null;
    }
  }

  /**
  * Write data to the wrapped socket object.
  * @param data Data to write to socket.
  * @return {@code true} on success.
  */
  public boolean write(String data){
    
    return writeBytes(data.getBytes(), 0, data.length());
  }

  /**
  * Write data to the wrapped socket object.
  * @param data Data to write to socket.
  * @param off Array offset of data.
  * @param len Number of bytes to send.
  * @return {@code true} on success.
  */
  public boolean writeBytes(byte[] data, int off, int len){

    try{
      writer.write(data, off, len);
      writer.flush();
      return true;
    }catch(IOException e){
      return false;
    }
  }

}
