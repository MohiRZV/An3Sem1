package com.mohi.examenma.components

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mohi.examenma.model.viewmodel.EntitiesViewModel

@Composable
fun AddEntityScreen(
    viewModel: EntitiesViewModel = hiltViewModel(),
    onClick: (String, Int, Int, Boolean) -> Unit
) {
    var textNume by rememberSaveable { mutableStateOf("") }
    var textMedie by rememberSaveable { mutableStateOf("0") }
    var textEtaj by rememberSaveable { mutableStateOf("0") }
    var textOrientare by rememberSaveable { mutableStateOf("true") }

    val context = LocalContext.current
    Box(modifier = Modifier.fillMaxHeight().fillMaxWidth(0.9f)) {
        Column() {
            TextField(
                value = textNume,
                onValueChange = {
                    textNume = it
                },
                label = { Text("Nume") }
            )
            TextField(
                value = textMedie,
                onValueChange = {
                    textMedie = it
                },
                label = { Text("Medie") }
            )
            TextField(
                value = textEtaj,
                onValueChange = {
                    textEtaj = it
                },
                label = { Text("Etaj") }
            )
            TextField(
                value = textOrientare,
                onValueChange = {
                    textOrientare = it
                },
                label = { Text("Orientare") }
            )
        }
        FloatingActionButton(
            modifier = Modifier.align(Alignment.BottomEnd).padding(25.dp),
            onClick = {
                try {
                    onClick(
                        textNume,
                        textMedie.toInt(),
                        textEtaj.toInt(),
                        textOrientare.toBooleanStrict()
                    )
                } catch (ex: Exception) {
                    Toast.makeText(context, ex.message, Toast.LENGTH_SHORT).show()
                }
            }
        ) {
            Text(text = "Save")
        }
    }
}