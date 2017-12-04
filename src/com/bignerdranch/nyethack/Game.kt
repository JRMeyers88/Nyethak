package com.bignerdranch.nyethack

import kotlin.system.exitProcess

class Game(val player: Player) {

    var currentRoom: Room = TownSquare()

    val listOfRooms = listOf(listOf(currentRoom, Room("Tavern"), Room("Back Room")),
            listOf(Room("Long Corridor"), HofPit()))

    private val inventory = Inventory<Gear>()
    private val boxOfTrinkets = Inventory<Trinket>()

    fun play() {
        println("Welcome, adventurer.")
        while (true) {
            println("room: ${currentRoom.description()}")
            currentRoom.load()
            println(player.status())
            print("enter your command: ")
            println(processGameCommand(GameInput(readLine())))
        }
    }

    private fun processGameCommand(input: GameInput) = when (input.command) {
        "fight" -> performFight()
        "move" -> performMove(input)
        "conjure" -> performConjuration(input)
        "equip" -> equipFirstPieceOfGear()
        else -> commandNotFound()
    }


    private fun commandNotFound(): String = "I'm not sure what you're trying to do"
    private fun performFight(): String =
            currentRoom.enemies.firstOrNull()?.let { enemy ->
                while (player.healthPoints > 0 && enemy.healthPoints > 0) {
                    val playerAttack = player.attack(enemy)
                    val enemyAttack = enemy.attack(player)
                    println("${enemy.name} did $enemyAttack damage!")
                    println("${player.name} did $playerAttack damage!")
                    if (player.healthPoints <= 0) {
                        println(">>>> You have been killed! <<<<")
                        exitProcess(0)
                    }
                    if (enemy.healthPoints <= 0) {
                        println(">>>> ${enemy.name} has been killed! <<<<")
                        currentRoom.enemies.remove(enemy)
                    }
                    Thread.sleep(1000)
                }
                "combat completed."
            } ?: "There's nothing to fight here!"
    private fun performMove(input: GameInput) =
            try {
                val direction = Direction.valueOf(input.argument.toUpperCase())
                val updatedPosition = direction.updateCoordinate(player.currentPosition)
                if (updatedPosition.isValid) {
                    val room = listOfRooms[updatedPosition.y][updatedPosition.x]
                    player.currentPosition = updatedPosition
                    currentRoom = room
                    "OK, you move $direction to the ${room.name}."
                } else {
                    "You run into a wall attempting to go $direction! Try another direction."
                }
            } catch (e: Exception) {
                val validDirections = Direction.values().joinToString(", ") { it.name.toLowerCase() }
                "Invalid direction: ${input.argument}. Valid directions are $validDirections"
    }
    private fun performConjuration(input: GameInput) = when (input.argument) {
        "weapon" -> inventory.add(Weapon("Duneblade", 35, 42))
        "armor" -> inventory.add(Armor("Bracers of Nyr", 400, 20))
        else -> commandNotFound()
    }

    private fun equipFirstPieceOfGear() =
            if (inventory.size() ==0) {
                "Inventory is empty"
            } else {
                val element = inventory.equip(0)
                "Equipped ${element.name}."
                if (element is Weapon) {
                    "Bonus: ${element.attackBonus}"
                } else if (element is Armor) {
                    "Bonus: ${element.defenseBonus}"
                } else {
                    "No Bonus!"
                }
            }

}

class GameInput(arg: String?) {
    private val input = arg ?: ""
    val command = input.split(" ")[0]
    val argument = input.split(" ").getOrElse(1){""}
}