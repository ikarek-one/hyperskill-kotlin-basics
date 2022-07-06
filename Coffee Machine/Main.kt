package machine

fun main() {
//    stageFive()
    stageSix()
}

fun stageSix() {
    val machine = Machine(400, 540, 120, 9, 550)
    val runner = MachineRunner(machine)

    runner.awaitAction()

    while (true) {
        val line = readLine()!!
        if (line.trim().equals("exit")) {
            break
        }

        runner.takeLine(line)
    }
}

class MachineRunner(val machine: Machine, private var expecting: Expected = Expected.ACTION) {
    fun takeLine(line: String) {
        when (expecting) {
            Expected.ACTION -> doAction(line)
            Expected.COFFEE_TYPE -> {
                println(machine.buy(line))
                awaitAction()
            }
            Expected.WATER_SET -> {
                machine.addWater(line.toInt())
                println("\nWrite how many ml of milk do you want to add: ")
                expecting = Expected.MILK_SET
            }
            Expected.MILK_SET -> {
                machine.addMilk(line.toInt())
                println("\nWrite how many grams of coffee beans do you want to add: ")
                expecting = Expected.BEANS_SET
            }
            Expected.BEANS_SET -> {
                machine.addBeans(line.toInt())
                println("\nWrite how many disposable cups of coffee do you want to add: ")
                expecting = Expected.CUPS_SET
            }
            Expected.CUPS_SET -> {
                machine.addCups(line.toInt())
                awaitAction()
            }
        }
    }

    fun doAction(action: String) {
        when (action) {
            "buy" -> {
                println("\nWhat do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu:")
                expecting = Expected.COFFEE_TYPE
            }

            "fill" -> {
                println("\nWrite how many ml of water do you want to add: ")
                expecting = Expected.WATER_SET
            }

            "take" -> {
                machine.take()
                awaitAction()
            }

            "remaining" -> {
                machine.printState()
                awaitAction()
            }

            else -> {
                println("Unknown action: $action")
                awaitAction()
            }
        }

    }

    fun awaitAction() {
        println("\nWrite action (buy, fill, take, remaining, exit): ")
        expecting = Expected.ACTION
    }
}

class Machine(
    private var water: Int,
    private var milk: Int,
    private var beans: Int,
    private var cups: Int,
    private var money: Int
) {

    fun buy(type: String): String {
        val recipe = when (type) {
            // Array[Int](water, milk, beans, cups, money)
            "1" -> arrayOf(250, 0, 16, 1, 4)
            "2" -> arrayOf(350, 75, 20, 1, 7)
            "3" -> arrayOf(200, 100, 12, 1, 6)
            "back" -> return ""
            else ->
                throw RuntimeException("Wrong type of coffee: $type")
        }

        val response = when {
            (water - recipe[0] < 0) -> "Sorry, not enough water!"
            (milk - recipe[1] < 0) -> "Sorry, not enough milk!"
            (beans - recipe[2] < 0) -> "Sorry, not enough beans!"
            (beans - recipe[3] < 0) -> "Sorry, not enough cups!"
            else -> {
                water -= recipe[0]
                milk -= recipe[1]
                beans -= recipe[2]
                cups -= recipe[3]
                money += recipe[4]
                "I have enough resources, making you a coffee!"
            }
        }

        return response
    }

    fun take() {
        print("I gave you $" + money)
        money = 0
    }

    fun printState() {
        val msg = """
    The coffee machine has:
    $water of water
    $milk of milk
    $beans of coffee beans
    $cups of disposable cups
    $money of money """.trimIndent()

        print("\n" + msg)
    }

    fun addWater(amount: Int) {
        water += amount
    }

    fun addMilk(amount: Int) {
        milk += amount
    }

    fun addBeans(amount: Int) {
        beans += amount
    }

    fun addCups(amount: Int) {
        cups += amount
    }
}

enum class Expected {
    ACTION,
    COFFEE_TYPE,
    WATER_SET,
    MILK_SET,
    BEANS_SET,
    CUPS_SET
}



