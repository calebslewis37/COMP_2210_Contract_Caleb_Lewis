public class Stats {
    public static void main(String[] args) {
        Risk game = new Risk();
        int player1Wins = 0;
        int player2Wins = 0;
        int totalGames = 10;
        for(int i = 0; i < totalGames; i++) {
            int winner = game.playGame();
            if(winner == 1) {
                player1Wins++;
            } else if(winner == 2) {
                player2Wins++;
            }
            game.closeWindow();
        }
        System.out.println("After " + totalGames + " games:");
        System.out.println("Player 1 wins: " + player1Wins + " (" + (player1Wins * 100.0 / totalGames) + "%)");
        System.out.println("Player 2 wins: " + player2Wins + " (" + (player2Wins * 100.0 / totalGames) + "%)");
    }
}
