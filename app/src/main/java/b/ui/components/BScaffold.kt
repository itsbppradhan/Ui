// Copyright 2023, Christopher Banes and the Haze project contributors
// SPDX-License-Identifier: Apache-2.0

package b.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import dev.chrisbanes.haze.HazeProgressive
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.haze
import dev.chrisbanes.haze.hazeChild
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials

enum class ScaffoldSampleMode {
    Default,
    Progressive,
    Mask,
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalHazeMaterialsApi::class)
@Composable
fun BScaffold(
) {
    val hazeState = remember { HazeState() }
    val gridState = rememberLazyGridState()
    val showNavigationBar by remember(gridState) {
        derivedStateOf { gridState.firstVisibleItemIndex == 0 }
    }

    val style = HazeMaterials.regular(MaterialTheme.colorScheme.surface)
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = { Text("Scaffold Component B UI")},
                navigationIcon = {
                    IconButton(
                        onClick = { /*todo*/ },
                        modifier = Modifier.testTag("back"),
                    ) {
                        Icon(Icons.AutoMirrored.Default.ArrowBack, null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    scrolledContainerColor = Color.Transparent,
                ),
                modifier = Modifier
                    .hazeChild(state = hazeState, style = style) {
                        progressive = HazeProgressive.verticalGradient(startIntensity = 1f, endIntensity = 0f)

                    }
                    .fillMaxWidth(),
                scrollBehavior = scrollBehavior,
            )
        },
        bottomBar = {
            var selectedIndex by remember { mutableIntStateOf(0) }
            AnimatedVisibility(
                visible = showNavigationBar,
                enter = slideInVertically { it },
                exit = slideOutVertically { it },
            ) {
                SampleNavigationBar(
                    selectedIndex,
                    onItemClicked = { selectedIndex = it },
                    modifier = Modifier
                        .hazeChild(state = hazeState, style = style)
                        .fillMaxWidth(),
                )
            }
        },
        modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),

    ) { contentPadding ->
        LazyVerticalGrid(
            state = gridState,
            columns = GridCells.Adaptive(128.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = contentPadding,
            modifier = Modifier
                .fillMaxSize()
                .testTag("lazy_grid")
                .haze(state = hazeState),
        ) {
            items(50) { index ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(4 / 2f),
                ){Text("HELO ${index + 1}")}
            }
        }
    }
}

@Composable
private fun SampleNavigationBar(
    selectedIndex: Int,
    onItemClicked: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationBar(
        containerColor = Color.Transparent,
        modifier = modifier,
    ) {
        for (i in (0 until 3)) {
            NavigationBarItem(
                selected = selectedIndex == i,
                onClick = { onItemClicked(i) },
                icon = {
                    Icon(
                        imageVector = when (i) {
                            0 -> Icons.Default.Call
                            1 -> Icons.Default.Lock
                            else -> Icons.Default.Search
                        },
                        contentDescription = null,
                    )
                },
            )
        }
    }
}