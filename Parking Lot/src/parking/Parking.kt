package parking


class Parking() {
    private var spots: MutableList<Spot> = mutableListOf<Spot>()


    fun takeCommand(cmd: String) {
        val type = cmd.substringBefore(" ")

        if (type == "create") {
            create(cmd)
        } else if (size() == 0) {
            notCreatedInfo()
        } else {
            when (type) {
                "park" -> park(cmd)

                "leave" -> leave(cmd)

                "status" -> status()

                "reg_by_color" -> regByColor(cmd)

                "spot_by_color" -> spotByColor(cmd)

                "spot_by_reg" -> spotByReg(cmd)

                else -> println("Unknown command: $cmd")
            }
        }
    }

    fun size() = spots.size

    private fun create(cmd: String) {
        val sizeOfParking = cmd.substringAfter(" ").toInt()
        spots = mutableListOf()
        for (i in 0 until sizeOfParking) {
            spots.add(i, Spot(i+1, null))
        }
        println("Created a parking lot with $sizeOfParking spots.")
    }

    private fun status() {
        if (isEmpty()) {
            println("Parking lot is empty.")
            return
        }

        for (spot in spots) {
            if (spot.car != null) {
                println("${spot.number} ${spot.car!!.number} {${spot.car!!.color}}")
            }
        }
    }

    private fun notCreatedInfo() {
        println("Sorry, a parking lot has not been created.")
    }

    private fun park(cmd: String) {
        for (i in spots.indices) {
            if (spots.get(i).car == null) {
                val (_, nr, color) = cmd.split(" ")
                spots[i].car = Car(nr, color)
                println("$color car parked in spot ${spots[i].number}.")
                return
            }
        }
        println("Sorry, the parking lot is full.")
    }

    private fun leave(cmd: String) {
        val idx = cmd.substringAfterLast(" ").toInt() - 1
        if (spots[idx].car != null) {
            spots[idx].car = null
            println("Spot ${spots[idx].number} is free.")
        } else {
            println("There is no car in spot ${spots[idx].number}.")
        }
    }

    private fun isEmpty(): Boolean {
        for (spot in spots) {
            if (spot.car != null) return false
        }
        return true
    }

    private fun spotByReg(cmd: String) {
        val wantedPlate = cmd.substringAfter(" ")
        val plates = mutableListOf<String>()
        for (spot in spots) {
            if (spot.car?.number == wantedPlate) {
                println(spot.number)
                return
            }
        }
        println("No cars with registration number $wantedPlate were found.")
    }

    private fun spotByColor(cmd: String) {
        val wantedColor = cmd.substringAfter(" ")
        val foundSpotNumbers = mutableListOf<Int>()
        for (spot in spots) {
            if (spot.car?.color?.lowercase() == wantedColor.lowercase()) {
                foundSpotNumbers.add(spot.number)
            }
        }
        val result = if (foundSpotNumbers.isEmpty()) {
            "No cars with color $wantedColor were found."
        } else {
            foundSpotNumbers.joinToString(separator = ", ")
        }
        println(result)
    }

    private fun regByColor(cmd: String) {
        val wantedColor = cmd.substringAfter(" ")
        val plates = mutableListOf<String>()
        for (spot in spots) {
            if (spot.car?.color?.lowercase() == wantedColor.lowercase()) {
                plates.add(spot.car!!.number)
            }
        }
        val result = if (plates.isEmpty()) {
            "No cars with color $wantedColor were found."
        } else {
            plates.joinToString(separator = ", ")
        }
        println(result)
    }

}