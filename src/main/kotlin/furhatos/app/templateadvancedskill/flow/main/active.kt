package furhatos.app.templateadvancedskill.flow.main

import furhatos.app.templateadvancedskill.flow.Parent
import furhatos.app.templateadvancedskill.flow.log
import furhatos.app.templateadvancedskill.nlu.*
import furhatos.app.templateadvancedskill.responses.gestures.hearSpeechGesture
import furhatos.app.templateadvancedskill.setting.AutoGlanceAway
import furhatos.app.templateadvancedskill.setting.AutoUserAttentionSwitching
import furhatos.app.templateadvancedskill.setting.beActive
import furhatos.app.templateadvancedskill.setting.isAttended
import furhatos.flow.kotlin.*
import furhatos.nlu.common.Greeting
import furhatos.app.templateadvancedskill.responses.phrases
import furhatos.app.templateadvancedskill.nlu.ConfirmIntent
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import kotlin.Exception

/**
 * State where Furhat engage actively with the user.
 * Start your interaction from here.
 */
fun scrapeCreditHours(courseCode: String): String {
    val url = "https://catalog.sru.edu/undergraduate/engineering-and-science/computer-science/computing-bs-concentration-computer-science/#curriculumguidetext"

    return try {
        val doc: Document = Jsoup.connect(url).get()
        val rows = doc.select("table.sc_courselist tbody tr")

        for (row in rows) {
            val code = row.select("td.codecol a").text().trim()
            val hours = row.select("td.hourscol").text().trim()

            if (code.equals(courseCode, ignoreCase = true)) {
                return "$courseCode is $hours credit hours."
            }
        }
        "Course not found or credit hours not listed."
    } catch (e: Exception) {
        "An error occurred: ${e.message}"
    }
}
fun scrapeCourseNames(courseCode: String): String {
    val url = "https://catalog.sru.edu/undergraduate/engineering-and-science/computer-science/computing-bs-concentration-computer-science/#curriculumguidetext"

    return try {
        val doc: Document = Jsoup.connect(url).get()
        val rows = doc.select("table.sc_courselist tbody tr")

        for (row in rows) {
            val code = row.select("td.codecol a").text().trim()
            val courseName = row.select("td").text().trim()

            if (code.equals(courseCode, ignoreCase = true)) {
                return "$courseCode is $courseName ."
            }
        }
        "Course not found or credit hours not listed."
    } catch (e: Exception) {
        "An error occurred: ${e.message}"
    }
}

var lastHeard: String? = null
val discussedTopics = mutableListOf<String>()
val classCourses = mapOf(
    "CPSC 130" to "Both Fall and Spring", "MATH 120" to "Both Fall and Spring", "MATH 125" to "Both Fall and Spring", "CPSC 146" to "Both Fall and Spring",
    "STAT 152" to "Both Fall and Spring", "CPSC 207" to "Both Fall and Spring", "CPSC 246" to "Both Fall and Spring", "CPSC 370" to "Spring", "CPSC 323" to "Both Fall and Spring",
    "CPSC 327" to "Spring", "CPSC 376" to "Fall", "CSPC 300" to "Both Fall and Spring", "CPSC 374" to "Spring", "CPSC 311" to "Both Fall and Spring",
    "CPSC 423" to "Fall", "CPSC 476" to "Spring", "CPSC 488" to "Spring", "CPSC 474" to "Fall"
)
val fourYearPlan = mapOf(
    "Freshman" to mapOf(
        "Fall" to listOf("CPSC 130", "ENGL 102", "ESP 101", "MATH 120","SUBJ 139"),
        "Spring" to listOf("CPSC 146", "MATH 125", "ENGL 104")
    ),
    "Sophomore" to mapOf(
        "Fall" to listOf("CPSC 207", "CPSC 246", "STAT  152", "SCI 101", "ELECTIVE"),
        "Spring" to listOf("CPSC 323", "CPSC 370", "SCI 102")
    ),
    "Junior" to mapOf(
        "Fall" to listOf("CPSC 311", "CPSC 300","CPSC 376", "ELECTIVE"),
        "Spring" to listOf("CPSC 327", "CPSC 374", "ELECTIVE", "ELECTIVE")
    ),
    "Senior" to mapOf(
        "Fall" to listOf("CPSC 423", "CPSC 474", "ELECTIVE", "ELECTIVE"),
        "Spring" to listOf("CPSC 488", "CPSC 476", "ELECTIVE", "ELECTIVE")
    )
)
//TODO finish course descriptions
val courseDescriptions = mapOf(
    "CPSC 130" to "Introduction to Computing and Programming is An introductory course devoted to programming and to a description of hardware and software concepts. Programming concepts covered include top-down program development using pseudocode, algebraic notation, standard control structures, and arrays in an appropriate programming language. Other topics include binary representation, storage, and general architecture and functioning of a computer system.",
    "MATH 120" to "Intermediate Algebra is Linear functions, equations, inequalities, polynomials, algebra of functions, rational exponents, quadratic equations and inequalities, systems of equations.",

    )
val Active: State = state(Parent) {

    onEntry {
        furhat.beActive()
        log.debug("now I'm listening")

        // We're leaving the initiative to the user and extending the listen timeout from default 5000 ms to 8000 ms.
        furhat.listen(8000)
    }
    onReentry {
        when {
            !users.hasAny() -> goto(Idle)
            !furhat.isAttended() -> goto(WaitingToStart)
            else -> furhat.listen()
        }
    }
    include(AutoUserAttentionSwitching) // Switch user after a while
    include(AutoGlanceAway) // Glance away after some time of eye contact

    /** Handle simple meet and greet conversation **/
    // Handle multiple intents where one part of the intent was a Greeting
    onPartialResponse<Greeting> {
        furhat.attend(it.userId) // attend the user that spoke
        goto(GreetUser(it))
    }
    onResponse(listOf(Greeting(), HowAreYouIntent(), NiceToMeetYouIntent())) {
        furhat.attend(it.userId) // attend the user that spoke
        goto(GreetUser(it))
    }
    onResponse<ComputerScienceIntent> {
        furhat.attend(it.userId)
        furhat.say(phrases.A_ComputerScience)
        furhat.listen() // Keep the conversation open
    }
    onResponse<DepartmentIntent> {
        furhat.attend(it.userId)
        furhat.say(phrases.A_CompSciDept)
        furhat.listen() // Keep the conversation open
    }

    onResponse<FourYearPlanIntent> {
        val year = it.intent.year?.value
        val semester = it.intent.semester?.value

        when{
            year == null && semester == null -> furhat.say("which year and semester are you  talking about?")
            year == null -> furhat.say("Which year are you talking about?")
            semester == null -> furhat.say("which semester fall or spring?")
            else ->{
                val courses = fourYearPlan[year]?.get(semester)
                if(courses != null){
                    furhat.say("For your $year $semester semester, you should take: ${courses.joinToString(", ")}")
                } else{
                    furhat.say("Im not sure about that semester")
                }
            }
        }
        furhat.listen()
    }
    onResponse<ClassCoursesIntent>{
        val course = it.intent.cpscCourse?.value
        val semester = it.intent.semester?.value
        val semesterAvailable = classCourses[course]
        if (course != null && semesterAvailable != null) {
            if(semester == null){
                furhat.say("$course is offered in $semesterAvailable.")
            } else if (semesterAvailable.contains(semester, ignoreCase = true)){
                furhat.say("Yes, $course is available in $semester.")
            }else{
                furhat.say("No, $course is not available in $semester. It is offered in $semesterAvailable.")
            }
        } else {
            furhat.say("I'm not sure about that class.")
        }
        furhat.listen()
    }
    onResponse<LastNameQueryIntent> {
        furhat.attend(it.userId)
        val firstName = it.intent.firstName?.toText()

        if (firstName == null) {
            furhat.say("Please specify a first name.")
            furhat.listen()
            return@onResponse
        }

        try {
            val url = "https://webscraper.io/test-sites/tables"
            val doc = Jsoup.connect(url).get()

            // Find the last name by searching the table
            val row = doc.select("table.table-bordered tbody tr")
                .firstOrNull { it.select("td")[1].text().equals(firstName, ignoreCase = true) }

            val lastName = row?.select("td")?.get(2)?.text()

            if (lastName != null) {
                furhat.say("$firstName's last name is $lastName.")
            } else {
                furhat.say("I couldn't find the last name for $firstName.")
            }

        } catch (e: Exception) {
            furhat.say("I had trouble accessing the page.")
        }

        furhat.listen()
    }
    onResponse<CsMajorIntent> {
        furhat.attend(it.userId)
        furhat.say(phrases.A_CSMajor)
        furhat.listen()
    }
    onResponse<CyberMajorIntent> {
        furhat.attend(it.userId)
        furhat.say(phrases.A_CyberMajor)
        furhat.listen()
    }
    onResponse<GpaIntent> {
        furhat.attend(it.userId)
        furhat.say(phrases.A_Gpa)
        furhat.listen()
    }
    onResponse<CreditHoursIntent> {
        val courseCode = it.intent.cpscCourse?.value
        if (courseCode != null) {
            val creditInfo = scrapeCreditHours(courseCode)
            furhat.say(creditInfo)
        } else {
            furhat.say("I didn't catch the course. Could you repeat it?")
        }
        furhat.listen()
    }
    onResponse<CourseNameIntent>(){
        val courseCode = it.intent.cpscCourse?.value
        if (courseCode != null) {
            val courseInfo = scrapeCourseNames(courseCode)
            furhat.say(courseInfo)
        } else {
            furhat.say("I didn't catch the course. Could you repeat it?")
        }
        furhat.listen()
    }
    onResponse {
        furhat.attend(it.userId)
        lastHeard = it.text // Storing what the user says
        furhat.say("Did you say '${it.text}'?")
        goto(ConfirmUnderstanding)
    }

    /** Handle no response **/
    onNoResponse {
        furhat.say("I didn't hear anything. Could you repeat?")
        furhat.listen()
    }
    onResponse {
        // On unknown response, the robot reacts to the user speaking, but doesn't engage and take the initiative.
        // This overrides the default behavior: "Sorry I didn't understand that"
        furhat.attend(it.userId) // attend the user that spoke
        furhat.gesture(hearSpeechGesture)
        reentry()
    }
    /** Handle Attention switching, see also default attention behaviour in parent state **/
    onUserAttend {
        furhat.attend(it)
        reentry()
    }

}
val ConfirmUnderstanding: State = state {
    onEntry {
        furhat.listen(5000) // Listen for user confirmation
    }

    onResponse<ConfirmIntent> {
        val keywords = listOf("computer science", "cybersecurity", "CS major", "four-year plan")
        // TODO -> Make it to where it we dont stacticlly add irrelevantWords and to repeat what was said
        val irrelevantWords = listOf("eat", "destroy", "hate", "kill", "lick", "marry", "throw", "crush")
        val userInput = lastHeard?.lowercase() ?:""
        if (irrelevantWords.any {badWord -> userInput.contains(badWord)}){
            furhat.say("That doesn't sound like a real question. Could you rephrase it?")
            furhat.listen()
            return@onResponse
        }
        val validTopic = keywords.find { topic ->
            userInput.matches(Regex(".*\\b$topic\\b.*")) == true
        }

        if (validTopic != null) {
            discussedTopics.add(validTopic)
            when (validTopic) {
                "computer science" -> furhat.say(phrases.A_ComputerScience)
                "cybersecurity" -> furhat.say(phrases.A_CyberMajor)
                "CS major" -> furhat.say(phrases.A_CSMajor)
                "four-year plan" -> furhat.say(phrases.A_FourYearPlanFallY1)
            }
        } else {
            furhat.say("I'm not sure what you mean. Are you asking about a topic like computer science or cybersecurity?")
            furhat.listen()
        }

        goto(Active)
    }

    onResponse {
        furhat.say("Okay, let's talk about something else.")
        goto(Active)
    }

    onNoResponse {
        furhat.say("I didn't hear a confirmation. Let's try again.")
        goto(Active)
    }
}