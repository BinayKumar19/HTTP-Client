/**
 * @author Binay
 * To Test Redirection Scenario please input in this format
 */

import java.io.*;

import org.apache.commons.httpclient.*;
import org.apache.commons.logging.*;

import java.net.*;

public class Client {

    public static void main(String[] args) {

        String sMethod = args[0];
        String sMethodOption = null;
        String sURL = null;
        //"http://httpbin.org/get?course=networking&assignment=1";
        String sFormat = null;
        String sFilePath = null;
        //"E:\\MS\\Fall 2017\\Computer networking\\Lab\\http_demo_file.txt";
        String sInlineData = null;
        String sOutputFileName = null;
        //"httpc_file";
        String sHostName = null;
        Boolean bPrintAll = false;

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-v":
                    bPrintAll = true;
                    break;
                case "-h":
                    if (sFormat == null)
                        sFormat = args[i + 1];
                    else
                        sFormat = sFormat + " " + args[i + 1];
                    break;
                case "-d":
                    sInlineData = args[i + 1];
                    break;
                case "-f":
                    sFilePath = args[i + 1];
                    break;
                case "-o":
                    sOutputFileName = args[i + 1];
                    break;
                case "-L":
                    sMethod = "get";
                    break;
                default:
                    break;
            }
            if (args[i].contains("http:") ||
                    args[i].contains("www.") ||
                    args[i].contains("https:"))
                sURL = args[i];
        }

        if (sFormat == null)
            sFormat = "Content-Type:application/json";

        if (sURL != null && sURL.contains("http://"))
            sHostName = (sURL.substring(0, sURL.replace("http://", "").indexOf("/") + 7)).replace("http://", "www.");
        else if (sURL != null && sURL.contains("www."))
            sHostName = sURL.substring(0, sURL.indexOf("/"));

        if (args.length == 2 && (args[1].equals("get") || args[1].equals("post")))
            sMethodOption = args[1];

        switch (sMethod) {
            case "help":
                helpInformationPrint(args.length, sMethodOption);
                break;
            case "get":
                getMethod(sURL, sHostName, sFormat, bPrintAll, sOutputFileName);
                break;
            case "post":
                postMethod(sURL, sHostName, sFormat, sInlineData, sFilePath, bPrintAll, sOutputFileName);
                break;
            default:
                System.out.println("Invalid Input");
                break;
        }
    }

    public static void helpInformationPrint(int iargsLength, String sMethodOption) {
        if (iargsLength == 1) {
            System.out.println("httpc is a curl-like application but supports HTTP protocol only.\n"
                    + "Usage:httpc command [arguments]\nThe commands are:\n"
                    + " get    executes a HTTP GET request and prints the response.\n"
                    + " post   executes a HTTP POST request and prints the response.\n"
                    + " help   prints this screen.\n\n"
                    + "Use \"httpc help [command]\" for more information about a command.");

        } else if (sMethodOption.equals("get")) {
            System.out.println("usage: httpc get [-v] [-h key:value] URL\n"
                    + "Get executes a HTTP GET request for a given URL.\n"
                    + " -v           Prints the detail of the response such as protocol, status, and headers.\n"
                    + " -h key:value Associates headers to HTTP Request with the format 'key:value'.");

        } else if (sMethodOption.equals("post")) {
            System.out.println("usage: httpc post [-v] [-h key:value] [-d inline-data] [-f file] URL\n"
                    + "Post executes a HTTP POST request for a given URL with inline data or from file.\n"
                    + " -v  Prints the detail of the response such as protocol, status, and headers.\n"
                    + " -h  key:value Associates headers to HTTP Request with the format 'key:value'.\n"
                    + " -d  string Associates an inline data to the body HTTP POST request.\n"
                    + " -f  file Associates the content of a file to the body HTTP POST request.\n\n"
                    + "Either [-d] or [-f] can be used but not both.");
        } else {
            System.out.println("Invalid Option");
        }
    }

    public static void getMethod(String sURL, String sHostName, String sFormat, Boolean bPrintAll, String sOutputFileName) {
        String sTextFromServer, sUserAgent = "Concordia-HTTP/1.0";

        try {
            InetAddress addr = InetAddress.getByName(sHostName);
            Socket s = new Socket(addr, 80);

            BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));

            wr.write("GET " + sURL + " HTTP/1.0\r\n");
            wr.write("Host:" + sHostName + "\r\n");
            wr.write(sFormat + "\r\n");
            wr.write("User-Agent:" + sUserAgent + "\r\n");
            wr.write("\r\n");
            wr.write("");
            wr.flush();

            //IF Server response has to be written in output file
            BufferedWriter output = null;
            if (sOutputFileName != null) {
                FileWriter file = new FileWriter(sOutputFileName + ".txt", true);
                output = new BufferedWriter(file);
                output.append("Output of Get Request" + System.lineSeparator());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));

            while ((sTextFromServer = br.readLine()) != null) {
                if (sTextFromServer.contains("302")) {
                    while ((sTextFromServer = br.readLine()) != null) {
                        if (sTextFromServer.contains("Location")) {
                            sURL = sTextFromServer.substring(10);
                            getMethod(sURL, sHostName, sFormat, bPrintAll, sOutputFileName);
                            break;
                        }
                    }
                    break;
                }
                if (sTextFromServer.startsWith("{")) {
                    bPrintAll = true;
                }

                if (bPrintAll == true) {
                    if (sOutputFileName == null)
                        System.out.println(sTextFromServer);
                    else
                        output.append(sTextFromServer + System.lineSeparator());
                }
            }

            if (sOutputFileName != null)
                output.close();

            br.close();
            wr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void postMethod(String sURL, String sHostName, String sFormat, String sInlineData, String sFilePath, Boolean bPrintAll, String sOutputFileName) {
        String sTextFromServer, sCurrentLine, sUserAgent = "Concordia-HTTP/1.0";

        if (sFilePath != null) {
            BufferedReader br = null;
            FileReader fr = null;
            try {
                fr = new FileReader(sFilePath);
                br = new BufferedReader(fr);

                while ((sCurrentLine = br.readLine()) != null) {
                    if (sInlineData == null)
                        sInlineData = sCurrentLine;
                    else sInlineData = sInlineData + sCurrentLine;
                }
                br.close();
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            InetAddress addr = InetAddress.getByName(sHostName);
            Socket s = new Socket(addr, 80);

            BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));

            wr.write("POST " + sURL + " HTTP/1.0\r\n");
            wr.write("Host:" + sHostName + "\r\n");
            if (sInlineData != null) {
                wr.write("Content-Length:" + sInlineData.length() + "\r\n");
            }
            wr.write(sFormat + "\r\n");
            wr.write("User-Agent:" + sUserAgent + "\r\n");
            wr.write("\r\n");

            // Send parameters
            if (sInlineData != null) {
                wr.write(sInlineData);
            }
            wr.write("");
            wr.flush();

            //IF Server response has to be written in output file
            BufferedWriter output = null;
            if (sOutputFileName != null) {
                FileWriter file = new FileWriter(sOutputFileName + ".txt", true);
                output = new BufferedWriter(file);
                output.append("Output of Post Request" + System.lineSeparator());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            while ((sTextFromServer = br.readLine()) != null) {
                if (sTextFromServer.startsWith("{")) {
                    bPrintAll = true;
                }

                if (bPrintAll == true) {
                    if (sOutputFileName == null)
                        System.out.println(sTextFromServer);
                    else
                        output.append(sTextFromServer + System.lineSeparator());
                }
            }
            if (sOutputFileName != null)
                output.close();

            br.close();
            wr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}