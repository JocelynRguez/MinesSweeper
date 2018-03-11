package hu.ait.android.minessweeper.model;


import hu.ait.android.minessweeper.MainActivity;
import hu.ait.android.minessweeper.R;
import hu.ait.android.minessweeper.ui.MinesSweeperView;

public class MinesSweeperModel {


    private static MinesSweeperModel instance = null;
    //public MineField[][] gameField;
    public MineField[][] gameField =
            {{new MineField(), new MineField(), new MineField(),
                    new MineField(), new MineField(), new MineField(),
                    new MineField(), new MineField(), new MineField()},
                    {new MineField(), new MineField(), new MineField(),
                            new MineField(), new MineField(), new MineField(),
                            new MineField(), new MineField(), new MineField()},
                    {new MineField(), new MineField(), new MineField(),
                            new MineField(), new MineField(), new MineField(),
                            new MineField(), new MineField(), new MineField()},
                    {new MineField(), new MineField(), new MineField(),
                            new MineField(), new MineField(), new MineField(),
                            new MineField(), new MineField(), new MineField()},
                    {new MineField(), new MineField(), new MineField(),
                            new MineField(), new MineField(), new MineField(),
                            new MineField(), new MineField(), new MineField()},
                    {new MineField(), new MineField(), new MineField(),
                            new MineField(), new MineField(), new MineField(),
                            new MineField(), new MineField(), new MineField()},
                    {new MineField(), new MineField(), new MineField(),
                            new MineField(), new MineField(), new MineField(),
                            new MineField(), new MineField(), new MineField()},
                    {new MineField(), new MineField(), new MineField(),
                            new MineField(), new MineField(), new MineField(),
                            new MineField(), new MineField(), new MineField()},
                    {new MineField(), new MineField(), new MineField(),
                            new MineField(), new MineField(), new MineField(),
                            new MineField(), new MineField(), new MineField()}

            };


    private int gridLayout = 9;


    public static MinesSweeperModel getInstance() {
        if (instance == null) {
            instance = new MinesSweeperModel();
        }
        return instance;
    }

    private MinesSweeperModel() {

    }


    public void setBombs() {
        int x = (int) Math.floor(Math.random() * gridLayout);
        int y = (int) Math.floor(Math.random() * gridLayout);

        int populated = 0;

        while (populated < 10) {

            if (gameField[x][y].getType() != 1) {
                setNumbers(x, y);
            }
            gameField[x][y].setType(1);
            x = (int) Math.floor(Math.random() * gridLayout);
            y = (int) Math.floor(Math.random() * gridLayout);
            populated++;
        }
    }


    private void setNumbers(int x, int y) {
        //for(int i = 0; i < gridLayout; i++) {
        boolean left = x - 1 >= 0;
        boolean right = x + 1 <= 8;
        boolean down = y + 1 <= 8;
        boolean up = y - 1 >= 0;

        if (left) {
            gameField[x - 1][y].incrementMinesAround();
        }

        if (up) {
            gameField[x][y - 1].incrementMinesAround();
        }

        if (down) {
            gameField[x][y + 1].incrementMinesAround();
        }

        if (right) {
            gameField[x + 1][y].incrementMinesAround();
        }

        if (right && down) {
            gameField[x + 1][y + 1].incrementMinesAround();
        }

        if (right && up) {
            gameField[x + 1][y - 1].incrementMinesAround();
        }

        if (left && up) {
            gameField[x - 1][y - 1].incrementMinesAround();
        }

        if (left && down) {
            gameField[x - 1][y + 1].incrementMinesAround();
        }


//       // }
    }

    public boolean lose(int x, int y){
        if(gameField[x][y].getType() == MineField.BOMB){
            return true;
        }
        return  false;
    }

    public boolean win(){

        for(int i = 0; i < gridLayout; i++){
            for(int j = 0; j < gridLayout; j++){
                if(!gameField[i][j].wasClicked()){
                    return false;
                }
            }
        }

        return true;
    }




    public MineField getFieldContent(int x, int y) {
        return gameField[x][y];
    }



    public void resetGame() {
        for (int i = 0; i < gridLayout; i++) {
            for (int j = 0; j < gridLayout; j++) {
                gameField[i][j] = new MineField();
            }
        }

    }


}
