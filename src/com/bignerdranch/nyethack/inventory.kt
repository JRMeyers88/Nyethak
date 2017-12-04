package com.bignerdranch.nyethack

interface Gear {
    val name: String
    val cost: Int
}

class Armor(override val name: String,
            override val cost: Int,
            val defenseBonus: Int) : Gear

class Weapon(override val name: String,
             override val cost: Int,
             val attackBonus: Int) : Gear

class Inventory<E> {
    private val backpack = mutableListOf<E>()

    fun add(element: E) = backpack.add(element)

    fun remove(element: E) = backpack.remove(element)

    fun size() = backpack.size

    fun equip(index: Int): E {
        val element = backpack[index]
        remove(element)
        return element
    }
}

class Trinket(val name: String)
