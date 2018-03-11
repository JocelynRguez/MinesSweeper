package hu.ait.android.minessweeper.ui;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import hu.ait.android.minessweeper.MainActivity;
import hu.ait.android.minessweeper.R;
import hu.ait.android.minessweeper.model.MineField;
import hu.ait.android.minessweeper.model.MinesSweeperModel;

public class MinesSweeperView extends View {

    //paints
    private Paint paintBackground;
    private Paint paintLine;
    private Paint paintFlag;
    private Paint paintNumbers;
    private Paint paintSafe;


    private Bitmap bitmap = null;
    private int gridLayout = 9;

    public static final short LOSS = -1;
    public static final short KEEP_PLAYING = 0;

    private boolean placingFlag = false;
    public int gameState = 0;

    public MinesSweeperView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        paintBackground = new Paint();
        paintBackground.setColor(Color.GRAY);
        paintBackground.setStyle(Paint.Style.FILL);

        paintLine = new Paint();
        paintLine.setColor(Color.WHITE);
        paintLine.setStyle(Paint.Style.STROKE);
        paintLine.setStrokeWidth(5);

        paintNumbers = new Paint();
        paintNumbers.setColor(Color.RED);
        paintNumbers.setStyle(Paint.Style.STROKE);
        paintNumbers.setTextSize(100);

        paintSafe = new Paint();
        paintSafe.setColor(Color.BLACK);
        paintSafe.setStyle(Paint.Style.STROKE);
        paintSafe.setTextSize(100);

        paintFlag = new Paint();
        paintFlag.setColor(Color.BLUE);
        paintFlag.setStyle(Paint.Style.STROKE);
        paintFlag.setTextSize(100);

        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bomb);
        //bitmap = Bitmap.createScaledBitmap(bitmap, getWidth(), getHeight(), false);

        MinesSweeperModel.getInstance().setBombs();


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(0, 0, getWidth(), getHeight(), paintBackground);


        if (gameState == LOSS) {
            drawAll(canvas);
        } else {
            drawSafe(canvas);
        }

        drawGameArea(canvas);


    }

    private void drawGameArea(Canvas canvas) {
        canvas.drawRect(0, 0, getWidth(), getHeight(), paintLine);

        for (int i = 1; i <= gridLayout; i++) {

            canvas.drawLine(0, i * getHeight() / gridLayout, getWidth(), i * getHeight() / gridLayout,
                    paintLine);

            canvas.drawLine(i * getWidth() / gridLayout, 0, i * getWidth() / gridLayout, getHeight(),
                    paintLine);
        }

    }

    public void removeAllZeros(Canvas canvas, int startX, int startY) {
        int distance = getWidth() / 9;
        if (startX < gridLayout && startX >= 0 && startY < gridLayout && startY >= 0) {
            if (!MinesSweeperModel.getInstance().getFieldContent(startX, startY).isRevealed()
                    && MinesSweeperModel.getInstance().getFieldContent(startX, startY).getMinesAround() == 0) {

                drawNumber(canvas, distance, startX, startY);

                MinesSweeperModel.getInstance().getFieldContent(startX, startY).setRevealed();
                MinesSweeperModel.getInstance().getFieldContent(startX, startY).setWasClicked();

                removeAllZeros(canvas, startX + 1, startY);
                removeAllZeros(canvas, startX - 1, startY);
                removeAllZeros(canvas, startX, startY + 1);
                removeAllZeros(canvas, startX, startY - 1);
                removeAllZeros(canvas, startX + 1, startY + 1);
                removeAllZeros(canvas, startX - 1, startY - 1);
                removeAllZeros(canvas, startX + 1, startY - 1);
                removeAllZeros(canvas, startX - 1, startY + 1);
            }

        }
    }

    private void drawSafe(Canvas canvas) {
        int distance = getWidth() / 9;

        for (int x = 0; x < gridLayout; x++) {
            for (int y = 0; y < gridLayout; y++) {
                drawMinesAround(canvas, distance, x, y);

            }
        }

    }

    private void drawMinesAround(Canvas canvas, int distance, int x, int y) {
        if (MinesSweeperModel.getInstance().getFieldContent(x, y).wasClicked()) {
            if (!MinesSweeperModel.getInstance().getFieldContent(x, y).isFlagged() && MinesSweeperModel.getInstance().getFieldContent(x, y).getMinesAround() >= 0) {
                removeAllZeros(canvas, x, y);
                drawNumber(canvas, distance, x, y);
            } else if (MinesSweeperModel.getInstance().getFieldContent(x, y).isFlagged()) {
                canvas.drawText("F",
                        x * distance, (y + 1) * distance, paintFlag);
            } else{
                drawNumber(canvas, distance, x, y);
            }


        }
    }

    private void drawNumber(Canvas canvas, int distance, int x, int y) {
        canvas.drawText("" + MinesSweeperModel.getInstance().getFieldContent(x, y).getMinesAround(),
                x * distance, (y + 1) * distance, paintSafe);
    }


    public void drawAll(Canvas canvas) {
        int distance = getWidth() / 9;

        for (int x = 0; x < gridLayout; x++) {
            for (int y = 0; y < gridLayout; y++) {
                if (MinesSweeperModel.getInstance().getFieldContent(x, y).getType() == MineField.BOMB) {
                    canvas.drawText("B", x * distance, (y + 1) * distance, paintNumbers);
                } else {
                    drawNumber(canvas, distance, x, y);
                }
            }
        }
    }


    public void newGame() {
        MinesSweeperModel.getInstance().resetGame();
        MinesSweeperModel.getInstance().setBombs();
        gameState = KEEP_PLAYING;

        if (placingFlag) {
            positionFlag();
        }

        placingFlag = false;

        invalidate();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        // return super.onTouchEvent(event);
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int tX = (int) (event.getX() / (getWidth() / gridLayout));
            int tY = (int) (event.getY() / (getWidth() / gridLayout));
            MinesSweeperModel.getInstance().getFieldContent(tX, tY).setWasClicked();

            if (placingFlag) {
                flaggedBomb(tX, tY);
            }
            gameState(tX, tY);

        }

        if(gameState != LOSS && MinesSweeperModel.getInstance().win()){
            ((MainActivity) getContext()).showMessage(getContext().getString(R.string.win));
        }



        return true;
    }

    private void gameState(int tX, int tY) {
        if (MinesSweeperModel.getInstance().lose(tX, tY) &&
                !MinesSweeperModel.getInstance().getFieldContent(tX,tY).isFlagged()) {
            gameState = LOSS;

            ((MainActivity) getContext()).showMessage(getContext().getString(R.string.end_game));
        }
        invalidate();
    }

    public void positionFlag() {
        if (!placingFlag) {
            paintBackground.setColor(Color.DKGRAY);
            placingFlag = true;
        } else {
            paintBackground.setColor(Color.GRAY);
            placingFlag = false;
        }

        invalidate();

    }

    public void flaggedBomb(int x, int y) {
        if (MinesSweeperModel.getInstance().getFieldContent(x, y).getType()
                != MineField.BOMB) {
            gameState = LOSS;
            ((MainActivity) getContext()).showMessage(getContext().getString(R.string.end_game));
        } else {
            MinesSweeperModel.getInstance().getFieldContent(x, y).setFlagged();
        }
    }


}
