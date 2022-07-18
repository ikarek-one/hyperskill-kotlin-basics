package tasklist

class TaskList {
    private val tasks: MutableList<Task> = mutableListOf()

    fun runApp() {
        while (true) {
            println("Input an action (add, print, end):")
            val line = readln()
            when (line.trim().lowercase()) {
                "add" -> addTask()

                "print" -> printTasks()

                "end" -> {
                    println("Tasklist exiting!")
                    return
                }

                else -> {
                    println("The input action is invalid")
                }
            }
        }
    }

    fun addTask() {
        println("Input a new task (enter a blank line to end):")
        val task = linesToTask(readLinesUntil())
        if (task.text.isBlank()) {
            println("The task is blank")
        } else {
            this.tasks.add(task)
        }
    }

    fun printTasks() {
        if (tasks.isEmpty()) {
            println("No tasks have been input")
        }

        for (i in tasks.indices) {
            println(tasks[i].textToPrint(i + 1))
            println()
        }
    }

    fun readLinesUntil(
        stopCondition: (String) -> Boolean = String::isBlank
    ): MutableList<String> {
        val lines = mutableListOf<String>()
        while (true) {
            val line = readln()
            if (stopCondition(line)) {
                break
            }
            lines.add(line.trim())
//            lines.add(line)
        }
        return lines
    }

    fun linesToTask(lines: List<String>) =
        Task(lines.joinToString("\n"))

}