/**
 * A basic implementation of the Connect Four game.
 */
public class ConnectFour {
    /**
     * the number of rows
     */
    public final static int ROWS = 6;
    /**
     * the number of columns
     */
    public final static int COLS = 7;
    /**
     * how big a line one needs to win
     */
    public final static int WIN_LEN = 4;

    /**
     * Used to indicate a move that has been made on the board.
     */
    public enum Move {
        PLAYER_ONE('X'),
        PLAYER_TWO('O'),
        NONE('.');

        private char symbol;

        private Move(char symbol) {
            this.symbol = symbol;
        }

        public char getSymbol() {
            return symbol;
        }
    }

    /**
     * The number of rows in the board.
     */
    private int rows;

    /**
     * The number of columns in the board.
     */
    private int cols;

    /**
     * The board.
     */
    private Move[][] board;

    /**
     * Used to keep track of which player's turn it is; 0 for player 1, and 1
     * for player 2.
     */
    private int turn;

    /**
     * The last column a piece was placed.  Used for win checking.
     */
    private int lastCol;

    /**
     * The row the last piece was placed.  Used for win checking.
     */
    private int lastRow;

    /**
     * Creates a Connect Four game using a board with the standard number of
     * rows (6) and columns (7).
     */
    public ConnectFour() {
        this(ROWS, COLS);
    }

    /**
     * Creates a Connect Four game using a board with the specified number of
     * rows and columns. Assumes that player 1 is the first to move.
     *
     * @param rows The number of rows in the board.
     * @param cols The number of columns in the board.
     */
    public ConnectFour(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;

        board = new Move[cols][rows];
        for (int col = 0; col < cols; col++) {
            for (int row = 0; row < rows; row++) {
                board[col][row] = Move.NONE;
            }
        }

        turn = 0;
    }

    /**
     * Makes a move for the player whose turn it is. If the move is successful,
     * play automatically switches to the other player's turn.
     *
     * @param column The column in which the player is moving.
     * @throws ConnectFourException If the move is invalid for any reason.
     */
    public void makeMove(int column) throws ConnectFourException {

        if (turn == 0) {
            for (int row = rows - 1; row >= 0; row--) {
                if (board[column][row] == Move.NONE) {
                    board[column][row] = Move.PLAYER_ONE;
                    lastCol = column;
                    lastRow = row;
                    turn = 1;
                    break;
                }
            }
        } else if (turn == 1) {
            for (int row = rows - 1; row >= 0; row--) {
                if (board[column][row] == Move.NONE) {
                    board[column][row] = Move.PLAYER_TWO;
                    lastCol = column;
                    lastRow = row;
                    turn = 0;
                    break;
                }
            }
        }
    }

    /**
     * Look over the entire board for any N-in-a-row situations.
     * (By N we mean {@link #WIN_LEN}.)
     *
     * @return true if one of the players has an N-in-a-row situation.
     */
    public boolean hasWonGame() {
        // To twist turns updated by makeMove method
        if (turn == 0) {
            turn = 1;
        } else {
            turn = 0;
        }

        if (turn == 0) {
            turn = 1;
            if (checkHorizontal(Move.PLAYER_ONE) == true || checkVertical(Move.PLAYER_ONE) == true || checkDiagonal(Move.PLAYER_ONE) == true) {
                return true;
            } else {

                return false;
            }

        } else if (turn == 1) {
            turn = 0;
            if (checkHorizontal(Move.PLAYER_TWO) == true || checkVertical(Move.PLAYER_TWO) == true || checkDiagonal(Move.PLAYER_TWO) == true) {
                return true;
            } else {

                return false;
            }

        }
        return false;
    }

    private boolean checkDiagonal(Move m) {
        int count = 0;
        //check one diagonal
        boolean done = false;
        int x = 1;
        int y = -1;
        int currentCol;
        int currentRow;
        int minCol = lastCol;
        int minRow = lastRow;
        // Find one end of one diagonal
        while (true) {
            if (minCol - 1 < cols && minRow + 1 < rows && minCol - 1 >= 0 && minRow + 1 >= 0) {
                minCol = minCol - 1;
                minRow = minRow + 1;
            } else {
                break;
            }

        }
        currentCol = minCol;
        currentRow = minRow;
        //Traverse through the diagonal
        while (!done) {
            if (currentCol < cols && currentCol >= 0 && currentRow < rows && currentRow >= 0) {
                if (board[currentCol][currentRow] == m) {
                    count++;
                    if (count == WIN_LEN)
                        return true;
                } else {
                    //reset count
                    count = 0;
                }
                currentCol = currentCol + x;
                currentRow = currentRow + y;
            } else {
                done = true;
            }
        }
        minCol = lastCol;
        minRow = lastRow;
        // Find one end of another diagonal
        while (true) {
            if (minCol - 1 < cols && minRow - 1 < rows && minCol - 1 >= 0 && minRow - 1 >= 0) {
                minCol = minCol - 1;
                minRow = minRow - 1;
            } else {
                break;
            }

        }
        x = 1;
        y = 1;
        done = false;
        currentCol = minCol;
        currentRow = minRow;
        //Traverse through the diagonal
        while (!done) {
            if (currentCol < cols && currentCol >= 0 && currentRow < rows && currentRow >= 0) {
                if (board[currentCol][currentRow] == m) {
                    count++;
                    if (count == WIN_LEN)
                        return true;
                } else {
                    //reset count
                    count = 0;
                }
                currentCol = currentCol + x;
                currentRow = currentRow + y;
            } else {
                done = true;
            }
        }
        return false;
    }

    private boolean checkVertical(Move m) {

        int count = 0;
        for (int i = 0; i < rows; i++) {
            if (board[lastCol][i] == m) {
                count++;
                if (count == WIN_LEN)
                    return true;
            } else {
                count = 0;
            }
        }

        return false;

    }

    private Boolean checkHorizontal(Move m) {
        int count;
        count = 0;
        for (int j = 0; j < cols; j++) {
            if (board[j][lastRow] == m) {
                count++;
                if (count == WIN_LEN)
                    return true;
            } else {
                count = 0;
            }
        }
        return false;


        /*
        int count;
        //for(int i=0; i<rows; i++) {
            count=0;
            for (int j = 0; j < cols; j++) {
                if(board[j][lastRow] == m) {
                    count++;
                    if (count == WIN_LEN)
                        return true;
                }
            }
       // }
        return false;
*/

/*

            for(int i =0; i<rows; i++){
                for(int j=0; j< cols - 4; j++){
                    if(board[i][j]==m && board[i][j]==board[i][j+1] && board[i][j]==board[i][j+2] &&board[i][j]==board[i][j+3] ){
                        return true;
                    }
                }
            }
            return false;
*/
    }


    /**
     * Checks to see if the game is tied - no NONE moves left in board.  This
     * is called after hasGameWon.
     *
     * @return whether game is tied or not
     */
    public boolean hasTiedGame() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (board[j][i] != Move.NONE)
                    return false;
            }
        }
        return true;
    }

    /**
     * Returns a {@link String} representation of the board, suitable for
     * printing.
     *
     * @return A {@link String} representation of the board.
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                builder.append('[');
                builder.append(board[c][r].getSymbol());
                builder.append(']');
            }
            builder.append('\n');
        }
        return builder.toString();
    }
}
