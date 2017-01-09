package com.artpi.games.a7zamachow;

/**
 * Created by augue on 19/12/2016.
 */

public class Tile {

    private final Integer _unselected;
    private final Integer _selected;
    private final TileType _type;
    private Integer _current;

    public Tile(Integer unselected, Integer selected, TileType type) {
        _unselected = unselected;
        _selected = selected;
        _type = type;
        _current = unselected;
    }

    public void select() {
        _current = _selected;
    }

    public void unselect() {
        _current = _unselected;
    }

    public Integer getImage() {
        return _current;
    }

    public TileType getType(){return _type;}

    @Override
    public boolean equals(Object other){
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof Tile))return false;
        Tile otherMyClass = (Tile)other;
        if(_type == otherMyClass._type)
            return true;
        return false;
    }
}
