package ui.a2enhanced.view

import javafx.geometry.Insets
import javafx.geometry.Orientation
import javafx.geometry.Pos
import javafx.scene.control.Label
import javafx.scene.control.Separator
import javafx.scene.layout.HBox
import ui.a2enhanced.model.CourseModel
import java.math.RoundingMode
import java.text.DecimalFormat
class BottomStatusBarHBox(private val courseModel: CourseModel): HBox(), View{
    // View for bottom status bar that shows information on courses displayed

    // initialise labels
    val courseAverageLabel = Label()
    val coursesTakenLabel = Label()
    val coursesFailedLabel = Label()
    val coursesWDLabel = Label()

    init{
        courseModel.addView(this) // subscribe to the Model
        update() // call to set text on the labels
        // configure HBox
        children.addAll(courseAverageLabel, Separator(Orientation.VERTICAL),coursesTakenLabel,Separator(Orientation.VERTICAL),coursesFailedLabel, Separator(Orientation.VERTICAL), coursesWDLabel)
        alignment = Pos.CENTER_LEFT
        spacing = 5.0
        padding = Insets(10.0)
    }
    override fun update() {
        // retrieve data from Model to get number of courses taken
        coursesTakenLabel.apply { text = "Courses Taken:  ${courseModel.getCourseCount()}" }

        // retrieve data from Model and display as 2d.p. or N/A if no courses are remaining
        courseAverageLabel.apply {
            var courseAverage = courseModel.getCourseAverage()
            val df = DecimalFormat("#.00").apply {  RoundingMode.HALF_UP }
            var avgRounded = df.format(courseAverage)
            if (courseAverage != -1.00){
                text = "Course Average:  $avgRounded"
            }
            else{
                text = "Course Average: N/A"
            }
        }

        //  retrieve data from Model to get number of courses failed (grade < 50)
        coursesFailedLabel.apply{ text = "Courses failed: ${courseModel.getFailedCoursesCount()}"}

        // check if WD box is checked and retrieve data from Model if it is
        coursesWDLabel.apply{
            text = "Courses WD'ed: ${courseModel.getWDCoursesCount()}"
        }
    }


}