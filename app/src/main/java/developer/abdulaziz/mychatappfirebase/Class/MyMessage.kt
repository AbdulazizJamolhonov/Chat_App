package developer.abdulaziz.mychatappfirebase.Class

data class MyMessage(
    var message: String? = null,
    var fromUid: String? = null,
    var toUid: String? = null,
    var name: String? = null,
    var type: String = "text",
    var path: String? = null
)