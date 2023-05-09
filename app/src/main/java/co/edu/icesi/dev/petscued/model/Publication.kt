package co.edu.icesi.dev.petscued.model

import java.util.UUID

class Publication(
    var id: UUID,
    var image: String,
    var name: String,
    var breed: String,
    var sex: String,
    var owner: String,
    var type: String,
    var status: String,
    var location: String,
    var age: String,
    var color: String,
    var description: String,
    var contactInformation: String,
    var vaccinated: String
) {

    companion object{
        const val MALE = "Macho"
        const val FEMALE = "Hembra"
        const val ADOPTION = "Adopción"
        const val LOST = "Perdido"
        const val VACCINATED = "SÍ"
        const val NON_VACCINATED = "NO"
    }
}
