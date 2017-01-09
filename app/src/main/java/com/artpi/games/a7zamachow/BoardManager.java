package com.artpi.games.a7zamachow;

import android.util.Log;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by Ikebusia on 2016-11-25.
 */

public class BoardManager implements IBoardManager {

    private static final int BoardSize = 8;
    private static final int NumOfSymbols = 5;
//    private Integer[][] _board;
/*
    private Integer[] _symbols = { R.drawable.bomb,
            R.drawable.brightness, R.drawable.danger,
            R.drawable.favourites, R.drawable.flower,
            R.drawable.hint
    };
*/

    private Tile[][] _board;
   /* private Tile[] _symbols = { new Tile(R.drawable.bomb, R.drawable.bomb, TileType.Bomb),
            new Tile(R.drawable.brightness, R.drawable.brightness_selected, TileType.Brightness),
            new Tile(R.drawable.danger, R.drawable.danger_selected, TileType.Danger),
            new Tile(R.drawable.favourites, R.drawable.favourites_selected, TileType.Favourites),
            new Tile(R.drawable.flower, R.drawable.flower_selected, TileType.Flower),
            new Tile(R.drawable.hint, R.drawable.hint_selected, TileType.Hint)
    };*/

    private TileType[] _symbols = { TileType.Bomb,
            TileType.Brightness,
            TileType.Danger,
            TileType.Favourites,
            TileType.Flower,
            TileType.Hint
    };

    private Tile tileFactory(TileType type){

        switch (type){
            case Bomb:
                return new Tile(R.drawable.bomb_small, R.drawable.bomb_small, TileType.Bomb);
            case Brightness:
                return new Tile(R.drawable.brightness, R.drawable.brightness_selected, TileType.Brightness);
            case Danger:
                return new Tile(R.drawable.danger, R.drawable.danger_selected, TileType.Danger);
            case Favourites:
                return new Tile(R.drawable.favourites, R.drawable.favourites_selected, TileType.Favourites);
            case Flower:
                return new Tile(R.drawable.flower, R.drawable.flower_selected, TileType.Flower);
            case Hint:
                return new Tile(R.drawable.hint, R.drawable.hint_selected, TileType.Hint);
            default:
                return null;
        }

    }

    private int bombColumn;

    public static IBoardManager Create()
    {
        return new BoardManager();
    }

    private BoardManager()
    {
        _board = new Tile[BoardSize][BoardSize];
        Random random = new Random();
        bombColumn = random.nextInt(BoardSize);
        for (int row = 0; row < BoardSize; row++)
        {
            for (int col = 0; col < BoardSize; col++) {
                if (row == 0 && col == bombColumn) {
                    _board[row][col] = tileFactory(_symbols[0]);
                } else {
                    _board[row][col] = tileFactory(_symbols[random.nextInt(NumOfSymbols) + 1]);

                }
            }
        }
    }

    @Override
    public int getLength() {
        return BoardSize * BoardSize;
    }

    @Override
    public int getImageAt(int position) {
        return _board[getRowFor(position)][getColumnFor(position)].getImage();
    }

    public boolean isColumnTheSame(int position1, int position2){
        return getColumnFor(position1) == getColumnFor(position2);
    }

    public boolean isRowTheSame(int position1, int position2){
        return getRowFor(position1) == getRowFor(position2);
    }

    private int getColumnFor(int position) {
        return position % BoardSize;
    }

    private int getRowFor(int position) {
        return (int)Math.floor(position / BoardSize);
    }

    public void setSelected(int position){
        _board[getRowFor(position)][getColumnFor(position)].select();
    }

    public void setUnselected(int position){
        _board[getRowFor(position)][getColumnFor(position)].unselect();
    }

    public void setUnselected(ArrayDeque<Integer> list){
        for (int element :list) {
            _board[getRowFor(element)][getColumnFor(element)].unselect();
        }
    }

    @Override
    public void removeAndRefillFields(ArrayDeque<Integer> touchList) {

        refillBlanks(touchList);

    }

    private void refillBlanks(ArrayDeque<Integer> list){

        Integer[] tmpArray = list.toArray(new Integer[0]);
        Arrays.sort(tmpArray);
        for (Integer element:tmpArray) {
            int row = getRowFor(element);
            int col = getColumnFor(element);
            refillField(row,col);
        }

    }

    private void refillField(int i, int y) {
        int currentRow = i;
        while (currentRow > 0)
        {
            _board[currentRow][y] = _board[currentRow - 1][y];
            currentRow--;
        }
        Random random = new Random();
        _board[0][y] = tileFactory(_symbols[random.nextInt(NumOfSymbols) + 1]);
    }

    @Override
    public boolean isFinished() {
        for(int col = 0; col < BoardSize; col++){
            if(_board[BoardSize-1][col].getType() == _symbols[0]){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean arePointsVerticallyOrHorizontallyAligned(int currTouch, Integer last) {
        return isColumnTheSame(currTouch, last) || isRowTheSame(currTouch, last);
    }

    @Override
    public boolean isImageTheSame(int currTouch, int firstTouch) {
        return _board[getRowFor(currTouch)][getColumnFor(currTouch)].equals(_board[getRowFor(firstTouch)][getColumnFor(firstTouch)]);
    }

}
