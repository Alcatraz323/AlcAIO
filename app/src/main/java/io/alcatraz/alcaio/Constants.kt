package io.alcatraz.alcaio

import io.alcatraz.alcaio.beans.QueryElement

object Constants {
    const val MY_PROJECT_CODE = "alcaio"
    const val MY_GITHUB = "https://github.com/Alcatraz323/"
    const val MY_GITHUB_PAGE = "https://alcatraz323.github.io/"
    const val OPEN_SOURCE_URL = MY_GITHUB + MY_PROJECT_CODE
    const val SUPPORT_URL = MY_GITHUB_PAGE + MY_PROJECT_CODE
    const val EASTER_URL = "$MY_GITHUB_PAGE$MY_PROJECT_CODE/easter"

    //===============================================



    //===============================================

    const val BROADCAST_ACTION_UPDATE_PREFERENCES = "update_preferences"

    //gson
    val openSourceProjects: List<QueryElement>
        get() {
            return listOf(
                QueryElement(
                    "google",
                    "gson",
                    "https://github.com/google/gson",
                    "Apache 2.0",
                    "A Java serialization/deserialization library to convert Java Objects into JSON and back"
                )
            )
        }
}
