package tasklist

import tasklist.Consts.ID_COLUMN_LEN
import tasklist.Consts.SINGLE_SPACE_CHAR
import tasklist.Consts.SINGLE_SPACE_STRING

class Task(val text: String) {
    fun textToPrint(id: Int): String {
        val lines = text.lines()
        val stringBuilder = StringBuilder()
        for (i in lines.indices) {
            val idColumn =
                if (i == 0) {
                    id.toString().padEnd(ID_COLUMN_LEN, SINGLE_SPACE_CHAR)
                } else {
                    SINGLE_SPACE_STRING.repeat(ID_COLUMN_LEN)
                }

            stringBuilder.append(idColumn)
            stringBuilder.append(lines[i])
        }

        return stringBuilder.toString()
    }
}