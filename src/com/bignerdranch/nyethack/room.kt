package com.bignerdranch.nyethack

enum class Direction(private val coordinate: Coordinate) {
    NORTH(Coordinate(0, -1)),
    EAST(Coordinate(1, 0)),
    SOUTH(Coordinate(0, 1)),
    WEST(Coordinate(-1, 0));

    fun updateCoordinate(playerCoord: Coordinate) =
            Coordinate(playerCoord.x + coordinate.x, playerCoord.y + coordinate.y)
}

data class Coordinate(val x: Int, val y: Int) {
    val isValid = x >= 0 && y >= 0
}

open class Room(val name: String) {
    open val enemies: MutableList<Monster> = mutableListOf(Goblin())
    open fun description() = "$name \ncreatures:\n${enemiesDescription()}"
    private fun enemiesDescription(): String {
        return if (enemies.size > 0) {
            enemies.map { it.shortdesc }.joinToString ( "\n" )
        } else {
            "none."
        }
    }
    open fun load() = Unit
}

class TownSquare : Room("Town Square") {
    override fun load() {
        println("The villagers cheer when you enter!")
    }
}

class HofPit : Room(name = "The HofPit") {
    override val enemies: MutableList<Monster> = mutableListOf()
    override fun load() {
        println("You feel uneasy about this place...")
        val newMonster = monsterGenerator()
        println("A ${newMonster.name} materializes from the void!")
        enemies.add(newMonster)
    }
}