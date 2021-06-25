import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


/**
 * The player class represent each client that shall be playing the game.
 *
 */

class Player {
    private PrintWriter printWriter;
    private BufferedReader bufferedReader;

    public Player(Socket socket) throws IOException {
        this.printWriter = new PrintWriter(socket.getOutputStream(), true);
        this.bufferedReader = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
    }
    /**
     * Sends a string to the client handler
     *
     * @param message  The string to be sent
     */

    public void writeMessage(String message) {
        this.printWriter.println(message);
    }
    /**
     * Reads the next line
     *
     * @return  String  The string that is read
     */

    public String readNextLine() throws IOException {
        return this.bufferedReader.readLine();
    }
}

/**
 * Server port listening to all clients
 */
public class ConnectFourServer implements ConnectFourProtocol {
    static ArrayList<Player> players = new ArrayList<>();

    /**
     *The main method
     * @param args
     */
    public static void main(String[] args) throws IOException {

        if (args.length != 1) {
            System.err.println("Usage: java ConnectFourServer <port number>");
            System.exit(1);
        }

        int portNumber = Integer.parseInt(args[0]);
        System.out.println("Connecting to Port:" + portNumber);
        try {
            ServerSocket ss = new ServerSocket(portNumber);
            System.out.println("Listening on Port:" + portNumber);
            while (true) {

                Socket client = ss.accept();
                Player player = new Player(client);
                player.writeMessage(CONNECT);
                players.add(player);
                int playersSize = players.size();
                if (playersSize % 2 == 0) {
                    ConnectFourClientHandler t = new ConnectFourClientHandler(players.get(playersSize - 2), players.get(playersSize - 1));
                    t.start();
                }
            }
        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                    + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }
}
