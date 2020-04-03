package com.zettix.scumlord;

import java.util.SortedSet;

public class TileAction {

    public TileAction(TileEffectType effectType, TileEffectTime effectTime, TileAreaEffect tileAreaEffect,
                      PlayerStatChange change) {
        this.effectType = effectType;
        this.effectTime = effectTime;
        this.tileAreaEffect = tileAreaEffect;
        this.name = "Default Action Name";
        this.colors = null;
        this.tileTags = null;
        this.change = change;
    }

    public void setFilterName(String name) {
        this.name = name;
    }

    public void setFilterColors(SortedSet<SlumColors> colors) {
        this.colors = colors;
    }

    public void setFilterTags(SortedSet<TileTag> tileTags) {
        this.tileTags = tileTags;
    }

    public PlayerStatChange getChange() {
        return change;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        switch (effectTime) {
            case INSTANT:
                break;
            case RETROACTIVE:
                sb.append(" when ");
                break;
            case AFTER_NEW:
                sb.append(" after new ");
                break;
            default:
                sb.append(" UNKNOWN EFFECT TIME");
                break;
        }
        switch (effectType) {
            case ANY:
                break;
            case RED_LINE:
                sb.append(" passing a red line ");
                break;
            case TILE_COLOR:
                for (SlumColors c : colors) {
                    sb.append(c);
                    sb.append(" ");
                }
                break;
            case TILE_TAG:
                for (TileTag t : tileTags) {
                    sb.append(t);
                    sb.append(" ");
                }
                break;
            case TILE_NAME:
                sb.append(name);
                sb.append(" ");
                break;
            default:
                sb.append(" UNKNOWN EFFECT TYPE ");
        }

        switch (tileAreaEffect) {
            case ANY:
                break;
            case ADJACENT:
                sb.append(" is adjacent ");
                break;
            case GAME_GLOBAL:
                sb.append(" of all tiles ");
                break;
            case PLAYER_GLOBAL:
                sb.append(" of player tiles ");
                break;
            default:
                sb.append(" UNKNOWN EFFECT AREA ");
        }
        sb.append(change);
        return sb.toString();
    }

    private final TileEffectType effectType;
    private final TileEffectTime effectTime;
    private final TileAreaEffect tileAreaEffect;
    private String name;
    private SortedSet<SlumColors> colors;
    private SortedSet<TileTag> tileTags;
    private PlayerStatChange change;
}
