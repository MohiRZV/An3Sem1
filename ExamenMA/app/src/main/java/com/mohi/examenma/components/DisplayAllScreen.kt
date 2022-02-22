package com.mohi.examenma.components

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mohi.examenma.model.domain.Entity
import com.mohi.examenma.model.viewmodel.EntitiesViewModel
import com.mohi.examenma.utils.InternetStatus
import com.mohi.examenma.utils.InternetStatusLive

@ExperimentalMaterialApi
@Composable
fun DisplayAllScreen(
    viewModel: EntitiesViewModel = hiltViewModel(),
    onClick: () -> Unit,
    onSwitchToTopView: () -> Unit
) {
    val loading by remember { viewModel.loading }
    val listOfEntities by remember { viewModel.listOfEntities }

    Box(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()) {
        if(listOfEntities.isNotEmpty())
            LazyColumn {
                items(listOfEntities) { item ->
                    SingleEntityItem(entity = item, viewModel)
                }
            }
        else {
            Text(text = "You have no internet connection or haven't loaded the entities for the first time, please press retry")
            Button(onClick = {
                viewModel.backOnline()
            }, Modifier.align(Alignment.Center)) {
                Text(text = "Retry")
            }
        }
        CircularIndeterminateProgressBar(isDisplayed = loading)
        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(25.dp),
            onClick = { onClick() }
        ) {
            Text(text = "Add")
        }
        val context = LocalContext.current
        Button(
            onClick = {
                if (InternetStatusLive.status.value == InternetStatus.ONLINE)
                    onSwitchToTopView()
                else
                    Toast.makeText(context, "Only available online!", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start=25.dp,end = 25.dp, bottom = 25.dp)
        ) {
            Text(text = "See unconfirmed")
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun SingleEntityItem(
    entity: Entity,
    viewModel: EntitiesViewModel
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { viewModel.confirm(entity) },
        elevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                    Text(text = "nume: ${entity.nume}")
                    Spacer(modifier = Modifier.padding(start = 25.dp, end = 25.dp))
                    Text(text = "id: ${entity.id}")
                }
                Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                    Text(text = "etaj: ${entity.etaj}")
                    Spacer(modifier = Modifier.padding(start = 25.dp, end = 25.dp))
                    Text(text = "camera: ${entity.camera}")
                }
            }
        }
    }
}