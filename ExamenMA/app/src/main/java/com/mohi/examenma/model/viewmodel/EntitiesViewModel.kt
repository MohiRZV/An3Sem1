package com.mohi.examenma.model.viewmodel

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohi.examenma.MainActivity
import com.mohi.examenma.model.domain.Entity
import com.mohi.examenma.model.usecase.AddEntityUseCase
import com.mohi.examenma.model.usecase.ConfirmUseCase
import com.mohi.examenma.model.usecase.GetEntitiesUseCase
import com.mohi.examenma.model.usecase.SyncUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class EntitiesViewModel @Inject constructor(
    val getAllUseCase: GetEntitiesUseCase,
    val addUseCase: AddEntityUseCase,
    val confirmUseCase: ConfirmUseCase,
    val syncUseCase: SyncUseCase
) : ViewModel() {

    val loading = mutableStateOf(false)

    private val _listOfEntities: MutableState<List<Entity>> = mutableStateOf(emptyList())
    val listOfEntities: State<List<Entity>> = _listOfEntities

    private val _listOfTopEntities: MutableState<List<Entity>> = mutableStateOf(emptyList())
    val listOfTopEntities: State<List<Entity>> get() {
        getTop()
        return _listOfTopEntities
    }

    init {
        viewModelScope.launch {
            loading.value = true
            val entityList = getAllUseCase()
            _listOfEntities.value = entityList
            loading.value = false
        }
    }

    fun add(nume: String, medie: Int, etaj: Int, orientare: Boolean) {
        viewModelScope.launch {
            loading.value = true
            try {
                val entity = Entity(
                    nume = nume,
                    medie = medie,
                    etaj = etaj,
                    orientare = orientare,
                )
                Log.d("Mohi","Adding $entity")
                addUseCase(
                    entity = entity
                )
                val entityList = getAllUseCase()
                _listOfEntities.value = entityList
                loading.value = false
            }catch (ex: Exception) {
                Log.d("Mohi", ex.message?:"")
                showToast("Add failed")
            }
        }
    }

    private val myCustomComparatorNeconf =  Comparator<Entity> { a, b ->
        when {
            (a.medie > b.medie) -> 1
            (a.medie == b.medie && a.etaj > b.etaj) -> 1
            (a.medie == b.medie && a.etaj < b.etaj) -> -1
            else -> 0
        }
    }
    private fun getTop() {
        viewModelScope.launch {
            loading.value = true
            Log.d("Mohi","Generating unconfirmed report")
            val entityList = getAllUseCase()
            _listOfTopEntities.value = entityList.filter { it.status==false }.sortedWith(myCustomComparatorNeconf)
            loading.value = false
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(MainActivity.bcontext, message, Toast.LENGTH_SHORT).show()
    }

    fun confirm(entity: Entity) {
        viewModelScope.launch {
            loading.value = true
            try {
                confirmUseCase(entity)
                showToast("Registered $entity")
            } catch (ex: Exception) {
                showToast(ex.message.toString())
            }
            loading.value = false
        }
    }

    fun backOnline() {

        viewModelScope.launch {
            loading.value = true
            Log.d("Mohi","Synchronizing server with local data")
            try {
                syncUseCase()
                val entityList = getAllUseCase()
                _listOfEntities.value = entityList
                loading.value = false
            } catch (ex: Exception) {
                Log.d("Mohi", "Something went wrong while sync with server \n" +
                        " ${ex.message}")
                showToast("Something went wrong while sync with server " +
                        "\n ${ex.message}")
            }

        }
    }
}