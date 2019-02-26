# Text-adventure

## A simple text-based game, which merges elements of D&D, World of Warcraft and Heroes of Might and Magic.

## Features:
* Gameplay is based on user inputs
* Multiple classes
    * Class and equipment selection availability at the beginning
    * Classes and skills are inspired by D&D, WoW and own ideas
* One battle is available so far
* Battle is based on D&D d20 system
    * Turns based on Initiative, which comes from a Dexterity check.
    * Damage is based on weapons, skills and attributes
    * Multiple characters can fight on each side
    * Basic aggressive AI is available, using skill, when they are available
    * Target selection availability
* Skills are available to all characters, including enemies
    * Skills have a limiter per battle instead of a Mana/Stamina system
    * Certain skills have a lasting duration, others may have a delayed attributeEffect or a selectable target
    * Summon
    * Traps
* Battlefield is based on HoMM battlefield
    * Only those characters can be attacked, which are in reach of the weapon or the selected skill
    * Movement is based on speed
    * AI is able to determine the shortest route for an attack
    * Obstacles
* Certain attributes are present, but without the modifier availability so far

## Certain future plans chronologically:
* New classes
* More enemies and skills
* Magic
* Dexterity multiplier implementation to damage and initiative
* More attributes with multipliers