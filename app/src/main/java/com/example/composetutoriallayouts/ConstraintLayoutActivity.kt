package com.example.composetutoriallayouts

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.composetutoriallayouts.ui.theme.ComposeTutorialLayoutsTheme

class ConstraintLayoutActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeTutorialLayoutsTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    ConstraintLayoutContent()
                }
            }
        }
    }

    @Composable
    fun ConstraintLayoutContent() {
        ConstraintLayout {
            // Creates references for the three composables
            // in the ConstraintLayout's body
            val (button1, button2, text) = createRefs()

            Button(
                onClick = { /* Do something */ },
                modifier = Modifier.constrainAs(button1) {
                    top.linkTo(parent.top, margin = 16.dp)
                }
            ) {
                Text("Button 1")
            }

            Text("Text", Modifier.constrainAs(text) {
                top.linkTo(button1.bottom, margin = 16.dp)
                centerAround(button1.end)
            })

            val barrier = createEndBarrier(button1, text)
            Button(
                onClick = { /* Do something */ },
                modifier = Modifier.constrainAs(button2) {
                    top.linkTo(parent.top, margin = 16.dp)
                    start.linkTo(barrier)
                }
            ) {
                Text("Button 2")
            }
        }
    }

    @Composable
    fun LargeConstraintLayout() {
        ConstraintLayout {
            val text = createRef()

            val guideline = createGuidelineFromStart(fraction = 0.5f)
            Text(
                "This is a very very very very very very very long text",
                Modifier.constrainAs(text) {
                    linkTo(start = guideline, end = parent.end)
                    width = Dimension.preferredWrapContent
                }
            )
        }
    }

    @Composable
    fun TwoTexts(modifier: Modifier = Modifier, text1: String, text2: String) {
        Row(modifier = modifier.height(IntrinsicSize.Min)) {
            Text(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 4.dp)
                    .wrapContentWidth(Alignment.Start),
                text = text1
            )

            Divider(color = Color.Black, modifier = Modifier.fillMaxHeight().width(1.dp))
            Text(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 4.dp)
                    .wrapContentWidth(Alignment.End),

                text = text2
            )
        }
    }

    @Preview
    @Composable
    fun TwoTextsPreview() {
        ComposeTutorialLayoutsTheme {
            Surface {
                TwoTexts(text1 = "Hi", text2 = "there")
            }
        }
    }

    @Preview
    @Composable
    fun LargeConstraintLayoutPreview() {
        ComposeTutorialLayoutsTheme {
            LargeConstraintLayout()
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        ComposeTutorialLayoutsTheme {
            ConstraintLayoutContent()
        }
    }
}