package com.zettix.scumlord;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.*;
import java.util.logging.Logger;

public class TileJsonReader {

    private static final Logger LOG = Logger.getLogger(TileJsonReader.class.getName());
    private JsonObject root;
    InputStream is;

    public TileJsonReader() {
        is = null;
    }

    public void setInputStream(InputStream inputStream) {
        is = inputStream;
    }

    //public void Load() {
    public List<Tile> Load() {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        if (is == null) {
            is = classloader.getResourceAsStream("tiles.json");
        }
        JsonParser parser = new JsonParser();
        InputStreamReader reader = new InputStreamReader(is);

        JsonElement parsed = parser.parse(reader);
        root = parsed.getAsJsonObject();
    //}

    ////public List<Tile> getTiles() {
        List<Tile> result = new ArrayList<>();
        JsonArray jsonTiles = root.get("tiles").getAsJsonArray();
        for (JsonElement j : jsonTiles) {
            List<TileAction> tileActions = new ArrayList<>();
            JsonObject jsonTile = j.getAsJsonObject();
            String name = jsonTile.get("name").getAsString();
            int cost  = jsonTile.get("cost").getAsInt();
            SlumColors color = SlumColors.fromString(jsonTile.get("color").getAsString());
            JsonArray actions = jsonTile.getAsJsonArray("actions");
            for (JsonElement k : actions) {
                JsonObject action = k.getAsJsonObject();
                TileAreaEffect tileAreaEffect = TileAreaEffect.fromString(action.get("area").getAsString());
                TileEffectTime tileEffectTime = TileEffectTime.fromString(action.get("time").getAsString());
                TileEffectType tileEffectType = TileEffectType.fromString(action.get("type").getAsString());
                PlayerStatChange change = new PlayerStatChange();
                JsonObject jChange = action.get("change").getAsJsonObject();
                if (jChange.has("reputation")) {
                    change.setReputationChange(jChange.get("reputation").getAsInt());
                }
                if (jChange.has("income")) {
                    change.setIncomeChange(jChange.get("income").getAsInt());
                }
                if (jChange.has("population")) {
                    change.setPopulationChange(jChange.get("population").getAsInt());
                }
                if (jChange.has("funds")) {
                    change.setFundsChange(jChange.get("funds").getAsInt());
                }
                TileAction tileAction = new TileAction(tileEffectType, tileEffectTime, tileAreaEffect, change);
                switch(tileEffectType) {
                    case ANY:
                        break;
                    case COLOR:
                        SortedSet<SlumColors> actionColors = new TreeSet<>();
                        JsonArray jColors = action.get("colors").getAsJsonArray();
                        for (JsonElement jColorElement : jColors) {
                            String stringColor = jColorElement.getAsString();
                            actionColors.add(SlumColors.fromString(stringColor));
                        }
                        tileAction.setFilterColors(actionColors);
                        break;
                    case TAG:
                        SortedSet<TileTag> actionTags = new TreeSet<>();
                        JsonArray jTags = action.get("tags").getAsJsonArray();
                        for (JsonElement jTagElement : jTags) {
                            String stringColor = jTagElement.getAsString();
                            actionTags.add(TileTag.fromString(stringColor));
                        }
                        tileAction.setFilterTags(actionTags);
                        break;
                    case NAME:
                        String stringName = action.get("name").getAsString();
                        tileAction.setFilterName(stringName);
                        break;
                    case RED_LINE:
                        break;
                }
                tileActions.add(tileAction);
            }
            Tile tile = new TileImpl().setName(name).setColor(color).setCost(cost).setTileActions(tileActions);
            result.add(tile);
        }
        return result;
    }
}
