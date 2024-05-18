package ui.a2enhanced.view

import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.CheckBox
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.scene.shape.Arc
import javafx.scene.shape.ArcType
import javafx.scene.shape.Rectangle
import javafx.scene.text.Text
import ui.a2enhanced.model.CourseModel
import java.lang.Double.min

class VisualisationTabThree(private val courseModel: CourseModel): Pane(), View {
    // initialise constant, colours are in the order of WD'ed,Failed,Low,Good,Great,Excellent,Missing
    val colors = listOf<Color>(Color.DARKSLATEGRAY,Color.LIGHTCORAL,Color.LIGHTBLUE,Color.LIGHTGREEN,Color.SILVER,Color.GOLD,Color.WHITE)

    // for the creation of Arcs
    var courseListByGradeCategory = mutableListOf<List<String>>()
    var sizeList = mutableListOf<Int>()
    val listOfArcs = mutableListOf<Arc>()

    // for the creation of Arc labels on mouse enter
    val courseLabelsOnMouseEnter = mutableListOf<Text>()

    // initialise the WD check box
    val includeWDCheckBox = CheckBox("Include missing courses").apply{
        isSelected = courseModel.getMissingCourses()
        selectedProperty().addListener(){_,_,newValue ->
            courseModel.setMissingCourses(newValue)
        }
    }

    // initialise the enhancement (chart labels)
    val chartLabels = listOf<String>("WD'ed","Failed","Low","Good","Great","Excellent","Missing")
    val legendHBox = HBox().apply {
        alignment = Pos.CENTER
        for (i in chartLabels.indices){
            val chartLabelRectangle = Rectangle(10.0,10.0,colors[i])
            val chartLabelText = Text(chartLabels[i])
            HBox.setMargin(chartLabelText, Insets(0.0, 8.0, 0.0, 0.0))
            HBox.setMargin(chartLabelRectangle, Insets(0.0, 4.0, 0.0, 0.0))
            children.addAll(chartLabelRectangle,chartLabelText)
        }
    }
    init {
        update()
        courseModel.addView(this) // subscribe to the Model
        children.addAll(includeWDCheckBox,legendHBox)
    }

    override fun update() {
        // initialise and redraw arcs whenever course model changes
        // update course list by grade category
        courseListByGradeCategory.clear()
        courseListByGradeCategory = courseModel.getCoursesByGrades().toMutableList()
        sizeList.clear()
        sizeList = courseListByGradeCategory.map { it.size }.toMutableList()
        // redraw arcs
        children.removeAll(listOfArcs)
        listOfArcs.clear()
        var startangle = 0.0
        var total =  sizeList.sum()
        var angle = 360.0/total.toDouble()

        // modify total variable if there are missing courses, total becomes 40 courses plus number of WD-ed courses and number of failed courses
        if (courseModel.getMissingCourses()){
            total = 40 + sizeList[0] + sizeList[1]
            angle = 360.0/total.toDouble()
        }
        // add non-missing courses arcs
        for (i in sizeList.indices){
            val arc = Arc(width/2,0.45*height,min(width,height) / 2.5, min(width,height) / 2.5,startangle, ((sizeList[i]) * angle)).apply {
                fill = colors[i]
                type = ArcType.ROUND
                // show labels on mouse enter
                onMouseEntered = EventHandler {
                    courseLabelsOnMouseEnter.clear()
                    for ( j in courseListByGradeCategory[i].indices ){
                        val currText = Text(courseListByGradeCategory[i][j]).apply {
                            x = 20.0
                            y = 20.0*(j).toDouble() + 20.0
                        }
                        courseLabelsOnMouseEnter.add(currText)
                    }
                    children.addAll(courseLabelsOnMouseEnter)
                }
                onMouseExited = EventHandler {
                    children.removeAll(courseLabelsOnMouseEnter)
                    courseLabelsOnMouseEnter.clear()
                }
            }
            startangle += ((sizeList[i]) * angle)
            listOfArcs.add(arc)
        }
        // add missing courses arc if WD box is checked
        if (courseModel.getMissingCourses()) {
            val noOfMissingCourses = 40 - sizeList.drop(2).sum() // calculate number of missing courses
            val missingCoursesArc = Arc(width / 2, 0.45*height,  min(width,height) / 2.5,  min(width,height) / 2.5, startangle, noOfMissingCourses * angle).apply{
                fill = Color.WHITE
                type = ArcType.ROUND
            }
            listOfArcs.add(missingCoursesArc)
        }
        children.addAll(listOfArcs)


        heightProperty().addListener { _ ->
            // update arcs' centre and radius
            for (arc in listOfArcs){
                arc.centerY = 0.45*height
                arc.radiusX = min(width,height) / 2.5
                arc.radiusY = min(width,height) / 2.5
            }
            // update checkbox's position
            includeWDCheckBox.layoutY = 0.85*height

            // enhancement:  update legend position
            legendHBox.layoutY = 0.95*height
        }

        widthProperty().addListener { _ ->
            // update arcs' centre and radius
            for (arc in listOfArcs){
                arc.centerX = width/2
                arc.radiusX = min(width,height) / 2.5
                arc.radiusY = min(width,height) / 2.5
            }
            // update checkbox's position
            includeWDCheckBox.layoutX = width/2 - 76.4

            // enhancement: update legend position
            legendHBox.layoutX = width/2 - 200
        }

    }

}