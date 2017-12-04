package com.bignerdranch.nyethack

//enum class Race {
//    DWARF,
//    ORC,
//    HUMAN,
//    ELF,
//    GNOME
//
//    fun setRace(playerRace: Race) =
//            Race(playerRace)
//}

class Player(val name: String, val raceCode: Char, override var healthPoints: Int,
             private var isBlessed: Boolean = false, private var isImmortal: Boolean = false) : Fightable {
    override val dice = 3
    override val sides = 7

//    constructor(name: String) : this(name, raceCode = 'H', healthPoints = 100) {
//        if (name.toLowerCase() == "madrigal") healthPoints += 20
//    }
    override fun attack(opponent: Fightable): Int {
        val damageDealt = if (isBlessed) {
            damageRoll / 3
        } else {
            damageRoll }
        opponent.healthPoints -= damageDealt
        return damageDealt
    }

    private val race: String
    var currentPosition = Coordinate(0, 0)

    init {
        check(healthPoints > 0, { "Invalid healthPoints: $healthPoints" })
        check(name.isNotEmpty(), { "Invalid name: $name" })
        race = lookupRace()
    }

    fun formatHealthStatus() = when (healthPoints) {
        100 -> "is in excellent condition"
        in 90..100 -> "has a few scratches"
        in 75..90 -> if (isBlessed) {
            "has some small wounds and bruises, but seems to be healing quite quickly!"
        } else {
            "has some small wounds and bruises."
        }
        in 15..75 -> "looks pretty hurt."
        else -> "is in awful condition!"
    }

    fun auraColor() = if (isBlessed && healthPoints > 50 || isImmortal)
        "green" else "none"

    private fun lookupRace() = when (raceCode) {
        'D' -> "DWARF"
        'G' -> "GNOME"
        'O' -> "ORC"
        'H' -> "HUMAN"
        else -> "UNKNOWN"
    }

    private fun blessedStatus() = if (isBlessed) "YES" else "NO"

    fun status() = "(race: ${lookupRace()})" +
            "(aura: ${auraColor()})" +
            "(blessed: ${blessedStatus()}) $name ${formatHealthStatus()}"

//    fun look() {
//        println("$name looks around")
//    }

}