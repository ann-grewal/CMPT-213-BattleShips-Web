package webAppBackEnd.model;

/**
 * Represent the state of a game-board cell.
 * An immutable class.
 */

public class Cell {
    private final boolean hasBeenShot;
    private final int enemyNumberAtCell;

    public Cell(boolean isShot, int enemyNumberAtCell) {
        this.hasBeenShot = isShot;
        this.enemyNumberAtCell = enemyNumberAtCell;
    }

    public boolean hasFort() {
        return enemyNumberAtCell != 0;
    }

    public boolean hasBeenShot() {
        return hasBeenShot;
    }

    public boolean isHidden() {
        return !hasBeenShot;
    }

    // Create new instance based on current state (Immutable)
    public Cell makeHasBeenShot() {
        return new Cell(true, enemyNumberAtCell);
    }

    public Cell makeContainEnemy(int enemyNumber) {
        return new Cell(hasBeenShot, enemyNumber);
    }

    public int getFortNumberAtCell() {
        return enemyNumberAtCell;
    }

    public String stringState(boolean gameOver, boolean cheating) {
        if (gameOver || (cheating && isHidden())) {
            if (hasFort()) {
                return "fort";
            } else {
                return "feild";
            }
        } else {
            if (isHidden()) {
                return "fog";
            } else if (hasFort()) {
                return "hit";
            } else {
                return "miss";
            }
        }
    }
}
