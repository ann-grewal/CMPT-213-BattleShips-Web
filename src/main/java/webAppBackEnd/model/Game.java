package webAppBackEnd.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Manage the game Fort Defense game state.
 */

public class Game {
    public final GameBoard board = new GameBoard();
    private final ScoreTracker enemyScoreTracker = new ScoreTracker();
    private final List<Enemy> enemies = new ArrayList<>();
    private int id;
    private boolean cheating = false;
    private List<Integer> latestEnemyDamages;
    private boolean lastPlayerShotHit;

    public Game(int id, int numEnemies) {
        this.id = id;
        IntStream.range(1, numEnemies + 1)
                .forEach(enemyNum -> enemies.add(new Enemy(board, enemyNum)));
    }

    public boolean hasUserWon() {
        return enemies.stream().allMatch(Enemy::isFortDestroyed);
    }

    public boolean hasUserLost() {
        return enemyScoreTracker.hasWon();
    }

    public int getEnemyPoints() {
        return enemyScoreTracker.getScore();
    }

    public void recordPlayerShot(Coordinate cell) {
        board.recordUserShot(cell);
        lastPlayerShotHit = board.cellContainsEnemy(cell);
    }

    public boolean didLastPlayerShotHit() {
        return lastPlayerShotHit;
    }

    public Cell getCellState(Coordinate cell) {
        return board.getCellState(cell);
    }

    public void fireEnemyShots() {
        latestEnemyDamages = new ArrayList<>();
        enemies.stream()
                .mapToInt(Enemy::getShotDamage)
                .filter(dmg -> dmg > 0)
                .forEach(dmg ->
                {
                    enemyScoreTracker.addScore(dmg);
                    latestEnemyDamages.add(dmg);
                });
    }

    public int[] getLatestEnemyDamages() {
        if (latestEnemyDamages == null) {
            return new int[0];
        }

        return latestEnemyDamages.stream()
                .mapToInt(Integer::intValue)
                .toArray();
    }

    public long numActiveEnemies() {
        return enemies.stream()
                .filter(enemy -> !enemy.isFortDestroyed())
                .count();
    }

    public boolean isGameOver() {
        return hasUserLost() || hasUserWon();
    }

    public boolean isCheating() {
        return cheating;
    }

    public void startCheating() {
        cheating = true;
    }

    public int getId() {
        return id;
    }
}
