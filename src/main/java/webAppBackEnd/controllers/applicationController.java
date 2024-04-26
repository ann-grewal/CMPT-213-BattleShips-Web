package webAppBackEnd.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import webAppBackEnd.api.ApiBoardDTO;
import webAppBackEnd.api.ApiGameDTO;
import webAppBackEnd.api.ApiLocationDTO;
import webAppBackEnd.model.Coordinate;
import webAppBackEnd.model.Game;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class applicationController {
    private final int NUM_ENEMIES = 5;
    private final List<Game> games = new ArrayList<>();

    @GetMapping("/api/about")
    @ResponseStatus(HttpStatus.OK)
    public String getName() {
        return "Anureet Grewal";
    }

    @GetMapping("/api/games")
    @ResponseStatus(HttpStatus.OK)
    public List<ApiGameDTO> allGames() {
        List<ApiGameDTO> apiGames = new ArrayList<>();
        for (int i = 0; i < games.size(); i++) {
            apiGames.add(ApiGameDTO.ApiGameDefault(i + 1, games.get(i)));
        }
        return apiGames;
    }

    @PostMapping("/api/games")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiGameDTO createNewGame() {
        Game newGame = new Game(games.size() + 1, NUM_ENEMIES);
        games.add(newGame);
        return new ApiGameDTO(games.size(), false, false, 0, NUM_ENEMIES, newGame.getLatestEnemyDamages());
    }

    @GetMapping("/api/games/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ApiGameDTO getGameById(@PathVariable("id") int gameId) {
        if (games.size() < gameId) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return ApiGameDTO.ApiGameDefault(gameId, games.get(gameId - 1));
    }


    @GetMapping("/api/games/{id}/board")
    @ResponseStatus(HttpStatus.OK)
    public ApiBoardDTO getGameBoardById(@PathVariable("id") int gameId) {
        if (games.size() < gameId) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return ApiBoardDTO.ApiBoardDefault(games.get(gameId - 1));
    }

    @PostMapping("/api/games/{id}/moves")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void moveGameById(@RequestBody ApiLocationDTO apiCoordinates, @PathVariable("id") int gameId) {
        if (games.size() < gameId) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        try {
            Coordinate.checkRowAndCol(apiCoordinates.row, apiCoordinates.col);
        } catch (InvalidParameterException error) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        Coordinate selectedCell = new Coordinate(apiCoordinates.row, apiCoordinates.col);
        Game selectedGame = games.get(gameId - 1);
        selectedGame.recordPlayerShot(selectedCell);
        selectedGame.fireEnemyShots();
    }

    @PostMapping("/api/games/{id}/cheatstate")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void cheatGameById(@RequestBody String message, @PathVariable("id") int gameId) {
        if (games.size() < gameId) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        if (!message.equals("SHOW_ALL")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        Game selectedGame = games.get(gameId - 1);
        selectedGame.startCheating();
    }
}
