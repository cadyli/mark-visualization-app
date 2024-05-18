package ui.a2enhanced.view

import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.scene.shape.Line
import javafx.scene.shape.Rectangle
import javafx.scene.text.Text
import ui.a2enhanced.model.CourseModel

class VisualisationTabTwo(private val courseModel: CourseModel): Pane(), View {

    // initialise constants
    val categoricalLabelText = listOf<String>("CS", "MATH", "Other","Total")
    val totalNumberOfCourses = listOf<Int>(22,8,10,40)
    val passedCoursesColors = listOf<Color>(Color.YELLOW, Color.DEEPPINK, Color.GREY,Color.GREEN)
    val remainingCoursesColors = listOf<Color>(Color.LIGHTYELLOW, Color.LIGHTPINK, Color.LIGHTGREY,Color.LIGHTGREEN)
    
    // initalise variables
    // x-axis, y-axis, vertical lines that are markers for xaxis
    val listOfVerticalLines = mutableListOf<Line>()
    val listOfxAxisLabelText = mutableListOf<Text>()
    val listOfyAxisLabelText = mutableListOf<Text>()

    // for the initialisation of data (rectangles)
    var numberofCoursesPassed = mutableListOf<Int>()
    val listOfPassedRectangles = mutableListOf<Rectangle>()
    val listOfRemainingRectangles = mutableListOf<Rectangle>()

    init {
        update() // call to update the view of all the courses
        courseModel.addView(this) // subscribe to the Model

        // init: add list of vertical lines
        for (i in 0..4){
            val newLine = Line(0.0,30.0,0.0,0.0)
            listOfVerticalLines.add(newLine)
        }
        children.addAll(listOfVerticalLines)

        // init: add x axis label texts
        for (i in 0..4){
            val newText = Text("${i*5.0}")
            listOfxAxisLabelText.add(newText)
        }
        children.addAll(listOfxAxisLabelText)

        // init: add y axis (course category) labels
        for (i in categoricalLabelText.indices){
            val newText = Text(categoricalLabelText[i])
            newText.x = 30.0
            listOfyAxisLabelText.add(newText)
        }
        children.addAll(listOfyAxisLabelText)



    }

    override fun update(){

        // update data / rectangles if course model changes
        numberofCoursesPassed.clear()
        numberofCoursesPassed = courseModel.getNumberofCoursesPassed().toMutableList();
        val dataSpacing = (0.8*height - 100.0)/3
        // clear all existing data
        children.removeAll(listOfPassedRectangles)
        children.removeAll(listOfRemainingRectangles)
        listOfPassedRectangles.clear()
        listOfRemainingRectangles.clear()
        // redraw rectangles
        for (i in numberofCoursesPassed.indices){
            val passedRectangleWidth =  (numberofCoursesPassed[i]/40.0)*(width-120.0)
            val y = 100.0 + i*dataSpacing
            val passedRectangle = Rectangle(100.0,y-20.0,passedRectangleWidth,40.0).apply {
                fill = passedCoursesColors[i]
                stroke = Color.rgb(0xFF, 0xFF, 0xFF, 0.5) // adjust opacity
            }
            val remainingRectangleWidth = (totalNumberOfCourses[i]-numberofCoursesPassed[i])/40.0*(width - 120.0)
            val remainingRectangle = Rectangle(100.0 + passedRectangleWidth, y-20.0,remainingRectangleWidth,40.0).apply{
                fill = remainingCoursesColors[i]
                stroke = Color.rgb(0xFF, 0xFF, 0xFF, 0.5) // adjust opacity
            }
            listOfPassedRectangles.add(passedRectangle)
            listOfRemainingRectangles.add(remainingRectangle)
        }
        // add back to the Pane
        children.addAll(listOfPassedRectangles)
        children.addAll(listOfRemainingRectangles)

        heightProperty().addListener { _ ->
            
            // modify data (rectangles) to make layout responsive
            for (i in listOfPassedRectangles.indices){
                val dataSpacing = (0.8*height - 100.0)/3
                listOfPassedRectangles[i].y =  100.0 + i*dataSpacing - 20.0
                listOfRemainingRectangles[i].y =  100.0 + i*dataSpacing - 20.0
            }

            // modify vertical lines' height
            for (line in listOfVerticalLines){
                line.endY = height*0.95
            }

            // modify x axis label text's positioning
            for (i in listOfxAxisLabelText.indices){
                listOfxAxisLabelText[i].y = height*0.95 + 15.0
            }

            // modify y axis label text's positioning
            for (i in listOfyAxisLabelText.indices){
                listOfyAxisLabelText[i].y = 100.0 + i*(0.8*height - 100.0)/3
            }


        }

        widthProperty().addListener { _ ->

            // modify data (rectangles) to make layout responsive
            for (i in listOfPassedRectangles.indices){
                listOfPassedRectangles[i].width =  (numberofCoursesPassed[i]/40.0)*(width-120.0)
                listOfRemainingRectangles[i].width =  (totalNumberOfCourses[i]-numberofCoursesPassed[i])/40.0*(width - 120.0)
                listOfRemainingRectangles[i].x = 100.0 + (numberofCoursesPassed[i]/40.0)*(width-120.0)
            }

            // modify vertical lines' width
            for (i in listOfVerticalLines.indices){
                listOfVerticalLines[i].startX = 100.0 + i*(width- 100.0 - 20.0)/4
                listOfVerticalLines[i].endX = 100.0 + i*(width- 100.0 - 20.0)/4
            }

            // modify x axis label text's positioning
            for (i in listOfxAxisLabelText.indices){
                listOfxAxisLabelText[i].x = 100.0 + i*(width- 100.0 - 20.0)/4 - listOfxAxisLabelText[i].layoutBounds.width/2.0
            }


        }

    }
}