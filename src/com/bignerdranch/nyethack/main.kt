package com.bignerdranch.nyethack

fun main(args: Array<String>) {
    val madrigal = Player(name = "Madrigal", raceCode = 'D', healthPoints = 100, isBlessed = true)
    log(madrigal)
    val gameInstance = Game(madrigal)
    log(gameInstance)
    gameInstance.play()
}

fun log(any: Any) {
    if (any is Player) {
        println("player name: ${(any as Player).name}, currentHealth: ${(any as Player).healthPoints}")
    } else {
        println("logging: ${any.toString()}")
    }
}