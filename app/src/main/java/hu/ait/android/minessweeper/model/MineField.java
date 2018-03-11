package hu.ait.android.minessweeper.model;


import hu.ait.android.minessweeper.R;

public class MineField {

    public static final short SAFE = 0;
    public static final short BOMB = 1;



    private int type;
    private int minesAround;
    private boolean isFlagged;
    private boolean wasClicked;
    private boolean isRevealed;


    public MineField() {
        this.type = 0;
        this.minesAround = 0;
        this.isFlagged = false;
        this.wasClicked = false;
        this.isRevealed = false;

    }

    public MineField(int type, int minesAround, boolean isFlagged, boolean wasClicked, boolean isRevealed) {
        this.type = type;
        this.minesAround = minesAround;
        this.isFlagged = isFlagged;
        this.wasClicked = wasClicked;
        this.isRevealed = isRevealed;
    }


    public int getType() {
        return type;
    }

    public int getMinesAround() {
        return minesAround;
    }

    public boolean isFlagged() {
        return isFlagged;
    }

    public boolean wasClicked() {
        return wasClicked;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void incrementMinesAround() {
        this.minesAround++;
    }

    public void setWasClicked() {
        this.wasClicked = true;
    }

    public boolean isRevealed() {
        return isRevealed;
    }

    public void setRevealed() {
        isRevealed = true;
    }

    public void setFlagged() {
        isFlagged = true;
    }
}
