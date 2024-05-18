package ui.a2enhanced.model

import javafx.scene.text.Text
import ui.a2enhanced.view.View
class CourseModel {
    // a list of all subscribed views / views that listen to the model / views that observe the model
    private val views = mutableListOf<View>()


    // Subscribe to receive notifications about changes in the [Model].
    fun addView(view: View) {
        views.add(view)
    }
    // initialise attributes of the courses object model
    private val courses = mutableListOf<Course>(
            Course("CS135", Term.F20, "92"),
            Course("CS136", Term.W22, "85"),
            Course("CS137", Term.F21, "88"),
            Course("ENGL109", Term.F20, "38"),
            Course("CHEM120", Term.F20,"75"),
            Course("PHYS101",Term.W21,"WD"),
            Course("MATH137", Term.W22, "58")
    )

    // logic

    // used for course entry. Check if the input Course has course code, course term and a valid course grade
    fun addCourse(course: Course){
        if(course.code.isNotEmpty() && course.term != null && (course.grade == "WD" || course.grade.toInt() in 0..100)){
            courses.add(course)
        }
        views.forEach { it.update() }
    }

    // used to update a course
    fun updateCourse(course: Course) {
        for (i in courses.indices) {
            if (courses[i].code == course.code) {
                courses[i] = Course(courses[i].code, course.term, course.grade)
                break
            }
        }
        views.forEach { it.update() }
    }

    // used to remove a course
    fun removeCourse(course: Course) {
        for (i in courses.indices) {
            if (courses[i].code == course.code) {
                courses.removeAt(i)
                break
            }
        }
        views.forEach{ it.update() }
    }

    // used to notify all views subscribed to the model on a change
    fun resetCourseList(){
        views.forEach{ it.update() }
    }

    fun getCourses():List<Course>{
        courses.sortBy{ it.term}
        return courses
    }

    // logic for VisualisationTabOne

    // used to get the list of terms on x axes as list of Texts, from the term for which the first course was recorded to the newest term
    fun getListofTerms():List<Text>{
        if (courses.isNotEmpty()){
            courses.sortBy { it.term }
            val lastCourseTerm = courses.last().term
            val lastIndex = Term.values().indexOf(lastCourseTerm)
            val terms = Term.values().take(lastIndex + 1).toList()
            val termTexts = terms.map { Text(it.name) }
            return termTexts
        }
        return emptyList()
    }

    // used to get the average per term
    fun getAverageGradeByTerm(term: Term): Double{
        val coursesInTerm = courses.filter{it.grade != "WD"}.filter { it.term == term }
        val totalGrade = coursesInTerm.fold(0.0) { acc, course ->
            acc + course.grade.toDouble()
        }
        return totalGrade / coursesInTerm.size

    }

    // logic for VisualisationTabTwo

    // used to get the number of courses passed in the order of CS, MATH, OTHER, Total
    fun getNumberofCoursesPassed(): List<Int> {
        val cs = courses.filter { it.code.startsWith("CS") }.filter { it.grade != "WD" }.count { it.grade.toInt() >= 50 }
        val math = courses.filter { it.code.startsWith("MATH") || it.code.startsWith("STAT") || it.code.startsWith("CO") }.filter{it.grade != "WD"}.filter { it.grade.toInt() >= 50 }.count()
        val other = courses.filter { !it.code.startsWith("CS") && !it.code.startsWith("MATH") }.filter{it.grade != "WD"}.filter { it.grade.toInt() >= 50 }.count()
        val total = courses.filter{it.grade != "WD"}.filter { it.grade.toInt() >= 50 }.count()
        return listOf(cs, math, other, total)
    }

    // logic for VisualisationTabThree
    // used to get the list of courses, grouped by the category of the mark, in the order of wd,failed,low,good,great,excellent
    fun getCoursesByGrades(): List<List<String>>{
        val wd = courses.filter{it.grade.toIntOrNull() == null}.map{it.code}
        val failed = courses.filter{it.grade.toIntOrNull() in 0..49}.map{it.code}
        val low = courses.filter{ it.grade.toIntOrNull() in 50..59 }.map{it.code}
        val good = courses.filter{ it.grade.toIntOrNull() in 60..90 }.map{it.code}
        val great =  courses.filter{ it.grade.toIntOrNull() in 91..95 }.map{it.code}
        val excellent = courses.filter{it.grade.toIntOrNull() in 96 .. 100}.map{it.code}
        return listOf(wd,failed,low,good,great,excellent)
    }

    // used for the checklist

    private var hasMissingCourses = false

    fun getMissingCourses(): Boolean{
        return hasMissingCourses
    }
    fun setMissingCourses(newValue: Boolean){
        hasMissingCourses = newValue
        views.forEach{ it.update() }
    }

    // logic for bottom status bar

    // used to get number of courses (after filtering)
    fun getCourseCount(): Int{
        // get "taken" courses count (not WD-ed)
        return courses.filter{it.grade.toDoubleOrNull() != null}.size;
    }

    // used to get course average (after filtering)
    fun getCourseAverage(): Double{
        val courses =courses.filter{it.grade.toDoubleOrNull() != null}
        if (courses.isNotEmpty()) {
            var courseAverage = courses.map { it.grade.toDouble() }.reduce { total, next -> total + next } / courses.size.toDouble()
            return courseAverage
        } else {
            return -1.00
        }
    }
    // used to get number of failed courses (after filtering)
    fun getFailedCoursesCount(): Int{
        return courses.filter{it.grade != "WD"}.filter{ it.grade.toInt() < 50 }.count()
    }

    // used to get number of failed courses (after filtering)
    fun getWDCoursesCount(): Int{
        return courses.filter{ it.grade == "WD" }.count()
    }

}