
/*
* @author Amrutha Varshini Mandalreddy
        */
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * Client class representing player
 */
public class ConnectFourClient implements ConnectFourProtocol{
    /**
     * The main method
     * @param args
     */

    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage: java ConnectFourClient <host name> <port number>");
            System.exit(1);
        }
        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);
        try (
                Socket s = new Socket(hostName, portNumber);
                PrintWriter out = new PrintWriter(s.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(s.getInputStream()));
                Scanner sc = new Scanner(System.in);
        ) {
            String fromServer;
            while ((fromServer = in.readLine()) != null) {
                if(fromServer.equals(MAKE_MOVE)){
                    System.out.println("Your turn! Enter a column: ");
                    String col = sc.nextLine();
                    int col_no=0;
                    boolean flag=true;
                    while (flag){
                        try {
                            col_no = Integer.parseInt(col);
                            if(col_no < 7 && col_no >=0){
                                out.println(col_no);
                                flag=false;
                            }
                            else{
                                System.out.println("Please enter a number from 0-6");
                                col = sc.nextLine();
                            }
                        }
                        catch (Exception e){
                            System.out.println("Please enter a number from 0-6");
                            col = sc.nextLine();
                        }
                    }
                }
                else if(fromServer.equals(GAME_LOST)){
                    System.out.println("You lost :(");
                    System.exit(0);
                }
                else if(fromServer.equals(ERROR)){
                    System.out.println("Something is wrong in the connection");
                    System.exit(0);
                }
                else if(fromServer.equals(GAME_TIED)){
                    System.out.println("No moves left. Game is Tied");
                    System.exit(0);
                }
                else if (fromServer.equals(GAME_WON)){
                    System.out.println("You win! Yay!");
                    System.exit(0);
                }
                else if(fromServer.equals(CONNECT)){
                    System.out.println("Connected!");
                }
                else if(fromServer.startsWith(MOVE_MADE)){
                    String[] inputs= fromServer.split(" ");
                    System.out.println("A move has been made in column "+ inputs[1]);
                    String nextMessage = in.readLine();
                    System.out.println(nextMessage.replace("#","\n"));
                }
            }
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                    hostName);
            System.exit(1);
        }


    }
}
