# 7-Wonders
## Text File Format
* All inputs are separated by a "/"
* if there are multiple resources used to build a card, use a comma to separate them, with no spaces. ex. if one needs 2 stone and 1 wood to build a card, it would be:
> /stone,stone,wood/

coin is the resource for money

**Card.java**

*cardType*/*cardName*/*colour*/*previousChainCard*/*cost*/*numberOfPlayers*

**ResourceCard.java** Resource types: cloth is *cloth*, glass is *glass*, paper is *paper*

*cardType*/*cardName*/*colour*/*previousChainCard*/*cost*/*numberOfPlayers*/*resourcesGiven*

**CivicsCard.java**

*cardType*/*cardName*/*colour*/*previousChainCard*/*cost*/*numberOfPlayers*/*victoryPoints*

**MilitaryCard.java**

*cardType*/*cardName*/*colour*/*previousChainCard*/*cost*/*numberOfPlayers*/*militaryPoints*

**TechCard.java** Science types: compass is *compass*, gear is *gear*, and tablet is *tablet*

*cardType*/*cardName*/*colour*/*previousChainCard*/*cost*/*numberOfPlayers*/*scienceType*

**GuildCard.java**

*cardType*/*cardName*/*colour*/*previousChainCard*/*cost*/*numberOfPlayers*/*id*

## Possible To do
**graphics**
- [ ] Dynamic screen resolution, getActualPosition(positionOnScreen) = (int)((ScreenWidth/1920)(positionOnScreen))
- [ ] Selected card is lowered
- [ ] Shadows under cards and wonders
- [ ] Option to deselect resource

**general**

# Wonder Names
The Colossus of Rhodes

The Lighthouse of Alexandria

The Temple of Artemis in Ephesus

The Pyramids of Giza

The Statue of Zeus in Olympia

The Hanging Gardens of Babylon

The Mausoleum of Halicarnassus
