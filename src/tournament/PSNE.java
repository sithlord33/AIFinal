package tournament;

import games.MatrixGame;
import games.MixedStrategy;
import games.OutcomeIterator;

public class PSNE extends Player {
    protected final String newName = "PSNE";

    public PSNE() {
        super();
        playerName = newName;
    }

    public void initialize() {
        // System.out.println("PSNE"+getParameters().getDescription());
    }

    protected MixedStrategy solveGame(MatrixGame mg, int playerNumber) {
        MixedStrategy ms = new MixedStrategy(mg.getNumActions(playerNumber));

        boolean[][] playerMatrix = new boolean[mg.getNumActions(playerNumber)][mg.getNumActions(playerNumber)];
        boolean[][] opponentMatrix = new boolean[mg.getNumActions(playerNumber)][mg.getNumActions(playerNumber)];

        for (int i = 1; i < mg.getNumActions(playerNumber); i++) {
            double p1CurrentMax = Double.MIN_VALUE;
            double p2CurrentMax = Double.MIN_VALUE;
            for (int j = 1; j < mg.getNumActions(playerNumber); j++) {
                int[] p1Actions = {i, j};
                int[] p2Actions = {j, i};
                double[] payoffs = mg.getPayoffs(p1Actions);
                if (p1CurrentMax < payoffs[playerNumber])
                    p1CurrentMax = payoffs[playerNumber];
                payoffs = mg.getPayoffs(p2Actions);
                if (p2CurrentMax < payoffs[getOpponentNumber()-1])
                    p2CurrentMax = payoffs[getOpponentNumber()-1];
            }

            for (int j = 1; j < mg.getNumActions(playerNumber); j++) {
                int[] p1Actions = {i, j};
                int[] p2Actions = {j, i};
                double[] payoffs = mg.getPayoffs(p1Actions);
                if (p1CurrentMax == payoffs[playerNumber])
                    playerMatrix[i][j] = true;
                else playerMatrix[i][j] = false;
                payoffs = mg.getPayoffs(p2Actions);
                if(p2CurrentMax == payoffs[getOpponentNumber()-1])
                    opponentMatrix[j][i] = true;
                else opponentMatrix[j][i] = false;
            }
        }
        double max = Double.MIN_VALUE;
        int[] coordinates = {0,0};

        for (int i = 1; i < mg.getNumActions(playerNumber); i++) {
            for (int j = 1; j < mg.getNumActions(playerNumber); j++) {
                boolean player = playerMatrix[i][j];
                boolean opponent = opponentMatrix[i][j];
                if(player && opponent){
                    int[] actions = {i, j};
                    double[] payoffs = mg.getPayoffs(actions);
                    if(payoffs[playerNumber] > max){
                        max = payoffs[playerNumber];
                        coordinates[0]= i;
                        coordinates[1] = j;
                    }
                }
            }
        }
        for(int i = 0; i <= mg.getNumActions(playerNumber);i++) {
            ms.setProb(i, 0.0);
        }
        if(coordinates[0] != 0) {
            ms.setProb(coordinates[playerNumber] + 1, 1.0);
            return ms;
        }
        else {
            return new MixedStrategy(mg.getNumActions(playerNumber));
        }
    }
}
