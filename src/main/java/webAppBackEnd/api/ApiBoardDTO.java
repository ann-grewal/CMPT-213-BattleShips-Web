package webAppBackEnd.api;

import webAppBackEnd.model.Cell;
import webAppBackEnd.model.Game;
import webAppBackEnd.model.GameBoard;

/**
 * DTO class for the REST API to define object structures required by the front-end.
 * HINT: Create static factory methods (or constructors) which help create this object
 * from the data stored in the model, or required by the model.
 */

public class ApiBoardDTO {
    public int boardWidth;
    public int boardHeight;

    // celState[row]col] = {"fog", "hit", "fort", "miss", "field"}
    public String[][] cellStates;

    public ApiBoardDTO(int boardWidth, int boardHeight, String[][] cellStates) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.cellStates = cellStates;
    }

    public static ApiBoardDTO ApiBoardDefault(Game game) {
        GameBoard board = game.board;
        return new ApiBoardDTO(board.NUMBER_COLS, board.NUMBER_ROWS,
                board.stringCellStates(game.isGameOver(), game.isCheating()));
    }

}

