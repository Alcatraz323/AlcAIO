package io.alcatraz.alcaio.beans

data class ChargeInfo(
    var status: String,
    var level: String,
    var voltage: String,
    var temperature: String
) {
    fun convertToText(): String {
        return "$status;$level;$voltage;$temperature"
    }
}
