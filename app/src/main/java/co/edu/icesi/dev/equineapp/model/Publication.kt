package co.edu.icesi.dev.equineapp.model

class Publication(
    var id: String = "",
    var image: String = "",
    var name: String = "",
    var breed: String = "",
    var sex: String = "",
    var owner: String = "",
    var type: String = "",
    var status: String = "",
    var location: String = "",
    var age: String = "",
    var color: String = "",
    var description: String = "",
    var contactInformation: String = "",
    var userId: String = ""
) {

    companion object{
        const val MALE = "Macho"
        const val FEMALE = "Hembra"
        const val ADOPTION = "Adopción"
        const val LOST = "Perdido"
    }
}
