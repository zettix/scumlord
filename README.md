## Scumlord: Hexagonal Tile Game

## Tiles have the following properties:

### Data:
1.   A name.  All tiles have a name, silly.
2.   A series.  When the tile comes out in the game.  A, B, C, D, E....
3.   A color/main category.
4.   tags/sub categories.
5.   A cost
6.   Placement requirements.  A tile cannot be placed unless requirements are met.
7.   A list of tile names the player has.
8.   A list and count of tags the player has.
9.   A list and count of colors the player has.
### Effects:
1.   Immediate effects.  These happen when the tile is placed and otherwise have no effect after placement.
2.   Ongoing effects.  These happen also when the tile is placed, but also may continue after placement.

### Requirements and Effects:
The ongoing effect is a requirement and an immediate effect together.

A requirement is a logical operation on game, player, or tile data.
Therefore a kind of grammar is created:
1.   [color/tag/name] [after new?] neighbor.  Example: For every GREEN neighbor.
2.   [color/tag/name] [after new?] for each in game. Example: For every Restaurant tag.
3.   [color/tag/name] [after new?] for each in player tiles: Example: For every Blue tag the player has.
4.   By some player / game state: Passing a score interval.

The result of the requirement is the effect, immediate effects have a trivial true requirement,
placement requirements allow placement, and ongoing effect requirements result in some change.

### Changes:
Changes to players stats involve the following:
.   Income Rate
.   Funds
.   Reputation
.   Score



## Players have the following properties:

### Data:
1.   A name.  All players have names.
2.   A game board.  The game board has:
   1.   Statistics for the player: Income, Funds, Reputation, Score
   2.   A hexagonal game board with starter tiles.

### Operations:
A player must perform the following operations:
1.   Purchase or convert a tile.
2.   Place the tile.
3.   (Optional) Register/Quit.

## Game. The game will have the following properties:
1.   A list of players.
2.   The current player index, and player order.
3.   Game logic to process effects, events, and game end.


## Progress on code:

Tile representation more or less done.  Next steps:
1.  Getting all available places around tiles done.
### TODO/In Progress
1.  Render tiles in Java Swing.  Long TODO.
2.  Block out game board so some cells are not available.
