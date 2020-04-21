package com.zettix.scumlord;

import com.zettix.scumlord.images.RenderBoard;
import com.zettix.scumlord.images.RenderMarket;
import com.zettix.scumlord.tile.Tile;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        System.out.println("Welcome to Scum Lord. Enter player name:");
        Scanner scanner = new Scanner(System.in);
        String name = scanner.next();
       // String name = "foo";

        Game game = new Game();
        game.Load();
        Player player = new Player(name);
        System.out.println("Staring single player game with " + player.getName());
        game.AddPlayer(player);
        game.Setup();
        int turn = 1;

        RenderBoard renderBoard = new RenderBoard(player, game);
        RenderMarket renderMarket = new RenderMarket(game);

        renderBoard.Render("Turn:" + turn, 800, 800);
        renderMarket.Render("Market", 1800, 600);

        while (turn < 10) {
            System.out.println("Enter Buy action:");
            String action = scanner.next();
            Tile t = game.buyTile(player, action);
            System.out.println("Bought tile:" + t);
            System.out.println("Enter Placement:");
            String placement = scanner.next();
            game.PlaceTile(player, t, placement);
            turn++;

            renderBoard.Render("Turn:" + turn, 800, 800);
            renderMarket.Render("Market" + turn, 1800, 600);
        }

	// write your code here
    }
}
