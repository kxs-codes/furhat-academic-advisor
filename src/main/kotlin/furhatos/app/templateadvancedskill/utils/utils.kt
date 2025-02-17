git rm -r --cached .idea
git rm -r --cached .settings bin target
git commit -m "Updated .gitignore to exclude IDE-specific files"
git push origin your-branch
package furhatos.app.templateadvancedskill.utils

import furhat.libraries.standard.utils.logger
import furhatos.skills.Skill
import java.io.FileInputStream
import java.io.IOException
import java.util.Properties

val prop = Properties()
const val skillPropertiesName = "skill.properties"

fun loadProperties(){
    try {
        val skillPropertiesStream = Skill::class.java.getResourceAsStream("/${skillPropertiesName}") //Works only when packaged.
        if (skillPropertiesStream == null) {
            prop.load(FileInputStream(skillPropertiesName))
        } else {
            prop.load(skillPropertiesStream)
        }
    } catch (_: IOException) {
        logger.warn("Unable to load skill.properties")
    }
}