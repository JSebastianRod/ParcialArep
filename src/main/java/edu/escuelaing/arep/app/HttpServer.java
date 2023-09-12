package edu.escuelaing.arep.app;

import java.net.*;
import java.io.*;

public class HttpServer {
   public static void main(String[] args) throws IOException {
      ServerSocket serverSocket = null;
      try {
         serverSocket = new ServerSocket(36000);
      } catch (IOException e) {
         System.err.println("Could not listen on port: 36000.");
         System.exit(1);
      }
      
      boolean running = true;
      while (running) {
         Socket clientSocket = null;
         try {
            System.out.println("Listo para recibir ...");
            clientSocket = serverSocket.accept();
         } catch (IOException e) {
            System.err.println("Accept failed.");
            System.exit(1);
         }
         PrintWriter out = new PrintWriter(
               clientSocket.getOutputStream(), true);
         BufferedReader in = new BufferedReader(
               new InputStreamReader(clientSocket.getInputStream()));
         String inputLine, outputLine, uriString, firstLine;
         uriString = "";
         firstLine = "";
         outputLine = "";
         while ((inputLine = in.readLine()) != null) {
            if (inputLine.contains("consulta?comando=")) {
               String[] ans = inputLine.split("comando=");
               uriString = (ans[1].split("HTTP")[0]).replace(" ", "");
            }
            System.out.println("Recibí: " + inputLine);
            
            if (!in.ready()) {
               break;
            }
         }

         if (!uriString.equals("")) {
            outputLine = "HTTP/1.1 200 OK\r\n"
                  + "Content-Type: text/html\r\n"
                  + "\r\n"
                  + "<!DOCTYPE html>\n"
                  + "<html>\n"
                  + "<head>\n"
                  + "<meta charset=\"UTF-8\">\n"
                  + "<title>Title of the document</title>\n"
                  + "</head>\n"
                  + "<body>\n"
                  + "<h1>Mi propio mensaje</h1>\n"
                  + "</body>\n"
                  + "</html>\n";
         } else {
            outputLine = "HTTP/1.1 200 OK\r\n"
                  + "Content-Type: text/html\r\n"
                  + "\r\n" + 
                  getResponse();
                  
         }
         out.println(outputLine);
         out.close();
         in.close();
         clientSocket.close();
      }
      serverSocket.close();
   }

   
   public static String getClass(String clase){
      String a;
      Class c = Class.forName("java.lang.String");
      a = c.toString();
      return a;
   }

   public static String getResponse() {
      String response = "<!DOCTYPE html>\n"
            + "<html>\n"
            + "    <head>\n"
            + "        <title>Reflective ChatGPT</title>\n"
            + "        <meta charset=\"UTF-8\">"
            + "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
            + "    </head>\n"
            + "    <body>\n"
            + "        <h1>Reflective ChatGPT</h1>\n"
            + "        <form action=\"/hello\">\n"
            + "            <label for=\"name\">Name:</label><br>\n"
            + "            <input type=\"text\" id=\"name\" name=\"name\" value=\"John\"><br><br>\n"
            + "            <input type=\"button\" value=\"Submit\" onclick=\"loadGetMsg()\">\n"
            + "        </form>\n "
            + "        <div id=\"getrespmsg\"></div>\n"
            + "        <script>\n"
            + "            function loadGetMsg() {\n"
            + "                let nameVar = document.getElementById(\"name\").value;\n"
            + "                const xhttp = new XMLHttpRequest();\n"
            + "                xhttp.onload = function() {\n"
            + "                    document.getElementById(\"getrespmsg\").innerHTML =\n"
            + "                    this.responseText;\n"
            + "                }\n"
            + "                xhttp.open(\"GET\", \"/hello?name=\"+nameVar);\n"
            + "                xhttp.send();\n"
            + "            }\n"
            + "        </script>\n"
            + "    </body>\n"
            + "</html>\n";
      return response;
   }
}