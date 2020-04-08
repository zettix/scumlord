# Tiles JSON Grammar / Schema

1.  name: string, Mandatory, only case sensitive item.
2.  series: Start, A, B, or C.  In TileSeries.java. Mandatory.
3.  cost: Integer. Mandatory.
4.  color: Predefined, in SlumColors.java. Mandatory.
5.  actions: array of actions. Mandatory.
6.  tag:  Predefined, in TileTag.java. Optional.
7.  requriements: an action with no status change.  Optional.

## Actions
An action is a combinaiton of 3 requriements and a status change.

1. time: Instant, After_new, Ongoing; in TileEffectTime.java. Required.
2. type: Any, Color, Name, Tag, Red_line; in TileEffectType.java. Optional. Default is Any.
3. area: Any, Adjacent, Player_global, Game_global; in TileAreaEffect.java. Optional. Default is Any.
4. colors: An array of SlumColors if the action is based on tile colors. Optional
5. tags: An array of tags if the action is based on tile tags. Optional.
6. name: A string name if the action is based on a tile name. Optional.
7. chage: A statistic change, see below. Optional.

## Changes
A change alters a player's statistics, and is a map with the following items:
1.  income: Integer, default is 0. Optional.
2.  reputation: Integer, default is 0. Optional.
3.  funds: Integer, default is 0. Optional.
4.  score: Integer, default is 0. Optional.
