package com.zettix.scumlord.tile;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.zettix.scumlord.PlayerStatChange;

import java.io.InputStream;
import java.io.InputStreamReader;
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
            TileSeries series = TileSeries.fromString(jsonTile.get("series").getAsString());
            TileTag tag = null;
            if (jsonTile.has("tag")) {
                tag = TileTag.fromString(jsonTile.get("tag").getAsString());
            }
            TileAction requirement = null;
            if (jsonTile.has("requirements")) {
                JsonObject jRequrements = jsonTile.get("requirements").getAsJsonObject();
                TileAreaEffect tileAreaEffect = TileAreaEffect.fromString(jRequrements.get("area").getAsString());
                TileEffectTime tileEffectTime = TileEffectTime.fromString(jRequrements.get("time").getAsString());
                TileEffectType tileEffectType = TileEffectType.fromString(jRequrements.get("type").getAsString());
                requirement = new TileAction(tileEffectType, tileEffectTime, tileAreaEffect, null);
                switch (tileEffectType) {
                    case COLOR:
                        SortedSet<SlumColors> actionColors = new TreeSet<>();
                        JsonArray jColors = jRequrements.get("colors").getAsJsonArray();
                        for (JsonElement jColorElement : jColors) {
                            String stringColor = jColorElement.getAsString();
                            actionColors.add(SlumColors.fromString(stringColor));
                        }
                        requirement.setFilterColors(actionColors);
                        break;
                    case TAG:
                        SortedSet<TileTag> actionTags = new TreeSet<>();
                        JsonArray jTags = jRequrements.get("tags").getAsJsonArray();
                        for (JsonElement jTagElement : jTags) {
                            String stringColor = jTagElement.getAsString();
                            actionTags.add(TileTag.fromString(stringColor));
                        }
                        requirement.setFilterTags(actionTags);
                        break;
                    default:
                        throw new UnsupportedOperationException("Unknown requirement:" + tileEffectType);
                }
            }
            int cost  = jsonTile.get("cost").getAsInt();
            int count = jsonTile.get("count").getAsInt();
            SlumColors color = SlumColors.fromString(jsonTile.get("color").getAsString());
            JsonArray actions = jsonTile.getAsJsonArray("actions");
            for (JsonElement k : actions) {
                JsonObject action = k.getAsJsonObject();
                TileAreaEffect tileAreaEffect = TileAreaEffect.ANY;
                if (action.has("area")) {
                    tileAreaEffect = TileAreaEffect.fromString(action.get("area").getAsString());
                }
                TileEffectType tileEffectType = TileEffectType.ANY;
                if (action.has("type")) {
                    tileEffectType = TileEffectType.fromString(action.get("type").getAsString());
                }
                TileEffectTime tileEffectTime = TileEffectTime.fromString(action.get("time").getAsString());
                //PlayerStatChange change = new PlayerStatChange();
                PlayerStatChange change = null;
                if (action.has("change")) {
                    change = new PlayerStatChange();
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
                    case RED_LINE:
                        break;
                }
                tileActions.add(tileAction);
            }
            Tile tile = new TileImpl().setName(name).setSeries(series).setColor(color).setCost(cost)
                    .setTileActions(tileActions).setCount(count);
            if (tag != null) {
                tile.setTileTag(tag);
            }
            if (requirement != null) {
                tile.setRequirement(requirement);
            }
            result.add(tile);
        }
        return result;
    }
}
