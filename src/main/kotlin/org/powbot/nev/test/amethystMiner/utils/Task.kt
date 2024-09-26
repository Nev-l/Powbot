package org.powbot.nev.amethystMiner.utils

abstract class Task {

    // Constructor
    init {
        // Initialization if necessary
    }

    // Abstract method to check if the task should activate
    abstract fun activate(): Boolean

    // Abstract method to execute the task
    abstract fun execute(): Boolean
}
