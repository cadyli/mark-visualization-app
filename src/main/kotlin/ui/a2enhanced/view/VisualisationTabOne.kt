package ui.a2enhanced.view

import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.scene.shape.Line
import javafx.scene.text.Text
import ui.a2enhanced.model.CourseModel


class VisualisationTabOne(private val courseModel: CourseModel): Pane(), View {

    // for the initialisation of elements in init and update
    val xaxis = Line(30.0, 10.0, 10.0, 10.0)
    val yaxis = Line(30.0, 30.0, 30.0, 10.0)
    val greyLines = mutableListOf<Line>()
    var xaxisLabels = mutableListOf<Text>()
    val yaxisLabels = mutableListOf<Text>()
    var graphPoints = mutableListOf<Circle>()
    init {
        // initialise xaxis and yaxis
        children.addAll(xaxis,yaxis)

        // initialise grey horizontal lines
        for (i in 0..9) {
            val line = Line(30.0,0.0,0.0,0.0).apply { stroke = Color.LIGHTGRAY }
            greyLines.add(line)
        }
        children.addAll(greyLines)

        // initialise yaxis labels
        for (i in 0..10){
            val label = 100 - i*10
            val labelText = Text("$label")
            labelText.x = 18.0 - labelText.layoutBounds.width / 2
            yaxisLabels.add(labelText)
        }
        children.addAll(yaxisLabels)

        update() // call to update the view of all the courses
        courseModel.addView(this) // subscribe to the Model
    }
    override fun update(){

        // initialise and update xaxis labels whenever model changes
        children.removeAll(xaxisLabels)
        if (courseModel.getListofTerms().isNotEmpty()){
            xaxisLabels = courseModel.getListofTerms().toMutableList(); // get list of terms, until the last term on the model, even if middle terms do not have courses
            val xAxisSpacing = (width - 60.0 - 40.0) / (xaxisLabels.size - 1) // get the spacing between two labels
            for (i in xaxisLabels.indices) {
                val x = 30.0 + 40.0 + i * xAxisSpacing // calculate x position for each text
                val y = height * 0.95 + 15.0 // calculate y position for each text
                xaxisLabels[i].x = x -  xaxisLabels[i].layoutBounds.width // center the text horizontally
                xaxisLabels[i].y = y // set the y position
            }
            children.addAll(xaxisLabels)
        }

        // initialise and update and display graph points whenever model changes
        val uniqueTerms = courseModel.getCourses().distinctBy { it.term }.map { it.term }
        children.removeAll(graphPoints)
        graphPoints.clear()
        for (uniqueTerm in uniqueTerms){
            val averageGradeByTerm = courseModel.getAverageGradeByTerm(uniqueTerm) // get average grade in the term
            val y = 30.0 + ((100.0-averageGradeByTerm)/100.0)*(height*0.95-30.0) // calculate y position for each point
            val i = xaxisLabels.indexOfFirst { it.text == uniqueTerm.toString() } // get the index based on the x axis labels for positioning of the point on the x axis
            val x = 30.0 + 40.0 + i * (width - 60.0 - 40.0) / (xaxisLabels.size - 1) - xaxisLabels[i].layoutBounds.width/2 // calculate x position for each point
            val colour = when (averageGradeByTerm) {
                in 0.0..49.99 -> Color.LIGHTCORAL
                in 50.0..59.99 -> Color.LIGHTBLUE
                in 60.0..90.99 -> Color.LIGHTGREEN
                in 91.0..96.99 -> Color.SILVER
                else -> Color.GOLD
            }
            val point= Circle(x,y,3.0,colour)
            graphPoints.add(point)
        }
        children.addAll(graphPoints)

        heightProperty().addListener { _ ->
            // adjust axes' height and width
            xaxis.startY = height * 0.95
            xaxis.endY = height * 0.95
            yaxis.endY = height * 0.95

            // adjust grey lines positioning
            for (i in greyLines.indices){
                greyLines[i].startY = 30.0 + i * (height*0.95 - 30.0) / 10
                greyLines[i].endY = 30.0 + i * (height*0.95 - 30.0) / 10
            }

            // adjust x-axis labels positioning
            for (i in xaxisLabels.indices){
                xaxisLabels[i].y = height * 0.95 + 15.0
            }

            // adjust y-axis labels positioning
            for (i in yaxisLabels.indices){
                yaxisLabels[i].y = 30.0 + i * (height*0.95 - 30.0) / 10 + 6.0
            }

            // adjust graph points positioning
            if (uniqueTerms.isNotEmpty()){
                for (i in uniqueTerms.indices){
                    val averageGradeByTerm = courseModel.getAverageGradeByTerm(uniqueTerms[i])
                    graphPoints[i].centerY = 30.0 + ((100.0-averageGradeByTerm)/100.0)*(height*0.95-30.0)
                }
            }

        }

        widthProperty().addListener { _ ->
            // adjust x axis width
            xaxis.endX = width - 30.0

            // adjust grey lines width
            for (i in greyLines.indices){
                greyLines[i].endX = width - 30.0
            }

            // adjust x-axis labels positioning
            for (i in xaxisLabels.indices){
                xaxisLabels[i].x = 30.0 + 40.0 + i * (width - 60.0 - 40.0) / (xaxisLabels.size - 1) - xaxisLabels[i].layoutBounds.width
            }

            // adjust graph points positioning
            if (uniqueTerms.isNotEmpty()){
                for (j in uniqueTerms.indices){
                    val i = xaxisLabels.indexOfFirst { it.text == uniqueTerms[j].toString()}
                    graphPoints[j].centerX = 30.0 + 40.0 + i * (width - 60.0 - 40.0) / (xaxisLabels.size - 1) - xaxisLabels[i].layoutBounds.width/2
                }
            }
        }

    }
}