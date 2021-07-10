package com.example.composetutoriallayouts

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.rounded.Home
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composetutoriallayouts.ui.theme.ComposeTutorialLayoutsTheme
import com.google.accompanist.coil.rememberCoilPainter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeTutorialLayoutsTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    LayoutMain()
                }
            }
        }
    }
}

@Composable
fun LayoutMain() {
    val context = LocalContext.current
    val items = listOf(
        Pair<Int, String>(R.drawable.abc_vector_test, "test"),
        Pair<Int, String>(R.drawable.abc_vector_test, "test 2")
    )
    Scaffold(
        topBar = { topBarContent(context) },
        content = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) { BodyContent(context) }
        },
        bottomBar = {
            BottomNavigation(
                elevation = 5.dp
            ) {
                items.map {
                    BottomNavigationItem(
                        icon = {
                            Icon(
                                painter = painterResource(id = it.first),
                                contentDescription = it.second
                            )
                        },
                        label = {
                            Text(
                                text = it.second
                            )
                        },
                        selected = false,
                        selectedContentColor = Color.White,
                        unselectedContentColor = Color.White.copy(alpha = 0.4f),
                        onClick = {
                            Toast.makeText(
                                context,
                                "BottomNavigationItem ${it.second}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    )
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                Toast.makeText(context, "floatingActionButton", Toast.LENGTH_SHORT).show()
            }) { }
        },
        drawerContent = {
            TextButton(onClick = {
                Toast.makeText(context, "drawerButton", Toast.LENGTH_SHORT).show()
            }) { }
        }
    )
}

@Composable
fun PhotographerCard(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Surface(
        modifier = modifier
            .padding(8.dp)
            .clip(RoundedCornerShape(4.dp)),
        color = Color.Red
    ) {
        Row(
            modifier
                //apply the padding modifier after the clickable one
                //then the padding is included in the clickable area
                .clickable(onClick = {
                    Toast
                        .makeText(context, "Row", Toast.LENGTH_SHORT)
                        .show()
                })
                .padding(16.dp)
        ) {
            Surface(
                modifier = Modifier
                    .size(50.dp),
                shape = CircleShape,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.2f)
            ) {
                //Image
            }
            Column(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .align(Alignment.CenterVertically)
            ) {
                Text(text = "Hello Vinc", fontWeight = FontWeight.Bold)
                // LocalContentAlpha is defining opacity level of its children
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    Text("3 minutes ago", style = MaterialTheme.typography.body2)
                }
            }
        }
    }
}

@Composable
fun MyButton(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit) = { },
    content: @Composable RowScope.() -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.textButtonColors(
            backgroundColor = Color.Red
        ),
        content = content
    )
}

@Composable
fun SimpleList() {
    // We save the scrolling position with this state that can also
    // be used to programmatically scroll the list
    val scrollState = rememberScrollState()

    Column(Modifier.verticalScroll(scrollState)) {
        repeat(100) {
            Text("Item #$it")
        }
    }
}

@Composable
fun LazyList() {
    //RecyclerView Like
    val listSize = 100
    // We save the scrolling position with this state
    val scrollState = rememberLazyListState()
    // We save the coroutine scope where our animated scroll will be executed
    val coroutineScope = rememberCoroutineScope()

    listButtons(
        coroutineScope,
        scrollState,
        listSize
    )
    LazyColumn(state = scrollState) {
        items(listSize) {
            ImageListItem(it)
        }
    }
}

@Composable
fun ImageListItem(index: Int) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = rememberCoilPainter(
                request = "https://developer.android.com/images/brand/Android_Robot.png"
            ),
            contentDescription = "Android Logo",
            modifier = Modifier.size(50.dp)
        )
        Spacer(Modifier.width(10.dp))
        Text("Item #$index", style = MaterialTheme.typography.subtitle1)
    }
}

@Composable
fun listButtons(
    coroutineScope: CoroutineScope,
    scrollState: LazyListState,
    listSize: Int
) {
    Row {
        Button(onClick = {
            coroutineScope.launch {
                // 0 is the first item index
                scrollState.animateScrollToItem(0)
            }
        }) {
            Text("Scroll to the top")
        }

        Button(onClick = {
            coroutineScope.launch {
                // listSize - 1 is the last index of the list
                scrollState.animateScrollToItem(listSize - 1)
            }
        }) {
            Text("Scroll to the end")
        }
    }
}

@Composable
fun topBarContent(context: Context) {
    val liked = remember { mutableStateOf(true) }
    val expanded = remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            Text(
                text = "Page title",
                maxLines = 2,
                color = Color.White
            )
        },
        navigationIcon = {
            IconButton(onClick = {
                Toast.makeText(context, "NavIcon", Toast.LENGTH_SHORT).show()
            }) {
                Icon(Icons.Rounded.Home, contentDescription = null)
            }
        },
        actions = {
            IconButton(onClick = {
                Toast.makeText(context, "PlayIcon", Toast.LENGTH_SHORT).show()
            }) {
                Icon(Icons.Filled.PlayCircle, contentDescription = "")
            }

            IconToggleButton(
                checked = liked.value,
                onCheckedChange = {
                    liked.value = it
                    if (liked.value) {
                        Toast.makeText(context, "Liked", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Unliked", Toast.LENGTH_SHORT).show()
                    }
                }
            ) {
                val tint by animateColorAsState(
                    if (liked.value) Color(0xFF7BB661)
                    else Color.LightGray
                )
                Icon(
                    Icons.Filled.Favorite,
                    contentDescription = "Localized description",
                    tint = tint
                )
            }

            Box(
                Modifier
                    .wrapContentSize(Alignment.TopEnd)
            ) {
                IconButton(onClick = {
                    expanded.value = true
                    Toast.makeText(context, "More icon", Toast.LENGTH_SHORT).show()
                }) {
                    Icon(
                        Icons.Filled.MoreVert,
                        contentDescription = "Localized description"
                    )
                }

                DropdownMenu(
                    expanded = expanded.value,
                    onDismissRequest = { expanded.value = false },
                ) {
                    DropdownMenuItem(onClick = {
                        expanded.value = false
                        Toast.makeText(context, "First item", Toast.LENGTH_SHORT).show()
                    }) {
                        Text("First Item")
                    }

                    DropdownMenuItem(onClick = {
                        expanded.value = false
                        Toast.makeText(context, "Second item", Toast.LENGTH_SHORT).show()
                    }) {
                        Text("Second item")
                    }

                    Divider()

                    DropdownMenuItem(onClick = {
                        expanded.value = false
                        Toast.makeText(context, "Third item", Toast.LENGTH_SHORT).show()
                    }) {
                        Text("Third item")
                    }

                    Divider()

                    DropdownMenuItem(onClick = {
                        expanded.value = false
                        Toast.makeText(context, "Fourth item", Toast.LENGTH_SHORT).show()
                    }) {
                        Text("Fourth item")
                    }
                }
            }
        },
        backgroundColor = colorResource(id = R.color.purple_200),
        elevation = AppBarDefaults.TopAppBarElevation //12.dp
    )
}

@Composable
fun BodyContent(context: Context, modifier: Modifier = Modifier) {
    PhotographerCard()
    MyButton(
        modifier = Modifier
            .padding(12.dp),
        onClick = {
            Toast.makeText(context, "Hello", Toast.LENGTH_SHORT).show()
        },
        content = {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val image: Painter =
                    painterResource(id = R.drawable.abc_vector_test)
                Image(
                    painter = image,
                    contentDescription = "",
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text("Button")
            }
        }
    )
    LazyList()
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposeTutorialLayoutsTheme {
        LayoutMain()
    }
}