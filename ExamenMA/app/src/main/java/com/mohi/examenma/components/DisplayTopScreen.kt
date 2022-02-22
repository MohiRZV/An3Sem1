package com.mohi.examenma.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.mohi.examenma.model.viewmodel.EntitiesViewModel

@ExperimentalMaterialApi
@Composable
fun DisplayTopScreen(
    viewModel: EntitiesViewModel = hiltViewModel()
) {
    val loading by remember { viewModel.loading }
    val listOfEntities by remember { viewModel.listOfTopEntities }

    Box(modifier = Modifier.fillMaxWidth().fillMaxHeight()) {
        LazyColumn {
            items(listOfEntities) { item ->
                SingleEntityItem(
                    entity = item,
                    viewModel
                )
            }
        }
    }
    CircularIndeterminateProgressBar(isDisplayed = loading)
}

