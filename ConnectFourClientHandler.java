
/*
 * @author Amrutha Varshini Mandalreddy
 */
import java.io.*;

/**
 * Create a Client Handler acts as a referee between 2 players/clients
 *
 */

public class ConnectFourClientHandler extends Thread implements ConnectFourProtocol {
    Player[] players;

    public ConnectFourClientHandler(Player player1, Player player2) {
        players = new Player[2];
        players[0] = player1;
        players[1] = player2;
    }
    /**
     * Sends a string to both the players
     *
     * @param Message  The string to be sent to both clients/players
     */

    public void informClients(String Message) {
        players[0].writeMessage(Message);
        players[1].writeMessage(Message);
    }

    /**
     * The run method portrays how each thread will be executed
     *
     */

    public void run() {
        ConnectFour f = new ConnectFour();
        String fromClient;
        try {
            Player currentPlayer = players[0];
            Player nextPlayer = players[1];
            while (true) {
                currentPlayer.writeMessage(ConnectFourProtocol.MAKE_MOVE);
                int col_number;
                if ((fromClient = currentPlayer.readNextLine()) != null) {
                    col_number = Integer.parseInt(fromClient);
                    f.makeMove(col_number);
                    informClients("MOVE_MADE " + col_number);
                    informClients(f.toString().replace("\n", "#"));
                }
                if (f.hasWonGame()) {
                    currentPlayer.writeMessage(ConnectFourProtocol.GAME_WON);
                    nextPlayer.writeMessage(GAME_LOST);
                    break;
                } else if (f.hasTiedGame()) {
                    currentPlayer.writeMessage(ConnectFourProtocol.GAME_TIED);
                    nextPlayer.writeMessage(ConnectFourProtocol.GAME_TIED);
                    break;
                }
                Player temp = currentPlayer;
                currentPlayer = nextPlayer;
                nextPlayer = temp;
            }

        } catch (IOException | ConnectFourException e) {
            informClients(ERROR);
        }

    }

}
