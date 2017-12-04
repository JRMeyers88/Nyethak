package com.bignerdranch.nyethack

import java.util.*

interface Fightable {
    var healthPoints: Int
    val dice: Int
    val sides: Int
    val damageRoll: Int
        get() = (0 until dice).map {
            Random().nextInt(sides + 1)
        }.sum()

    fun attack(opponent: Fightable): Int
}

abstract class Monster(val name: String, val shortdesc: String, override var healthPoints: Int) : Fightable {
    override fun attack( opponent: Fightable): Int {
        val damageDealt = damageRoll
        opponent.healthPoints -= damageDealt
        return damageDealt
    }
}

class Goblin(name: String = "goblin", shortdesc: String = "a nasty looking goblin",
             healthPoints: Int = 10) : Monster(name, shortdesc, healthPoints) {
    override val dice = 2
    override val sides = 4
}

val monsterGenerator: () -> Monster = {
    val RANDOM_HEALTH_MAX = 20
    val ALPHABET = ('a'..'z')
    val random = Random()
    val randomHealth = random.nextInt(RANDOM_HEALTH_MAX) + 1
    val name = (1..6)
            .map { ALPHABET.toList()[random.nextInt(ALPHABET.count())] }
            .joinToString("").capitalize()
    val shortDesc = "a $name stands before you, menacingly!"
    object : Monster(name, shortDesc, randomHealth) {
        override val dice = 2
        override val sides = 4
        val attacks = listOf(
                {
                    val randomFireballDamage = random.nextInt(5)
                    val message = "${name}'s fireball does $randomFireballDamage extra damage!!!"
                    val damage = damageRoll + randomFireballDamage
                    Pair(damage, message)
                },
                {
                    val damage = (0 until 2).map { Random().nextInt(4 + 1) }.sum()
                    val message = "${name}'s acid blast burns your armor!!!!"
                    Pair(damage, message)
                },
                {
                    val damage = damageRoll + (damageRoll / 3)
                    val message = "${name}'s color spray blinds you temporarily!!!!"
                    Pair(damage, message)
                })
        val randomAttack = attacks[random.nextInt(attacks.size)]
        override fun attack(opponent: Fightable) = applyDamage(opponent, randomAttack)
    }
}

private inline fun applyDamage(opponent: Fightable,
                               attackFunction: () -> Pair<Int, String>): Int {
    val attackValues = attackFunction()
    opponent.healthPoints -= attackValues.first
    println(attackValues.second)
    return attackValues.first
}
