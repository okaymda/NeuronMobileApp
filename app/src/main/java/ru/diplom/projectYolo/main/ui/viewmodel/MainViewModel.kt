package ru.diplom.projectYolo.main.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import org.kodein.di.DI
import org.kodein.di.instance
import ru.diplom.projectYolo.main.domain.repository.MainRepository
import ru.diplom.projectYolo.main.ui.di.mainDi
import ru.diplom.projectYolo.main.ui.events.MainEvents
import ru.diplom.projectYolo.main.ui.state.MainScreenState

class MainViewModel : ViewModel() {

    // Состояние экрана, управляемое ViewModel
    var viewState by mutableStateOf(MainScreenState(emptyList()))
        private set

    // Lazy инициализация Dependency Injection (DI)
    private val di: DI by lazy { mainDi() }

    // Получаем экземпляр репозитория из DI
    private val mainRepository: MainRepository by di.instance<MainRepository>()

    // Метод для обработки событий UI
    fun onEvent(event: MainEvents) {
        when (event) {
            // Обработка события добавления вложений
            is MainEvents.PassAttachments -> {
                val attachments = event.attachments
                // Обновляем список вложений, исключая дубликаты
                val newAttachments = (viewState.attachments + attachments).distinctBy { cachedFile -> cachedFile.uri }

                // Обновляем состояние экрана
                viewState = viewState.copy(attachments = newAttachments)
            }

            // Обработка события отправки вложений
            MainEvents.SendAttachments -> {
                viewModelScope.launch {
                    try {
                        // Отправляем вложения на сервер и получаем ответ
                        val response = mainRepository.sendAttachments(attachments = viewState.attachments)
                        val jsonResponse = JSONObject(response)
                        // Извлекаем размеченное изображение в формате base64 из ответа
                        val annotatedImageBase64 = jsonResponse.getString("predicted_image_base64")
                        // Обновляем состояние экрана, добавляя размеченное изображение
                        viewState = viewState.copy(annotatedImageBase64 = annotatedImageBase64)
                    } catch (e: Exception) {
                        // Логируем ошибку и обновляем состояние экрана, добавляя сообщение об ошибке
                        Log.e("MainViewModel", "Error uploading images", e)
                        viewState = viewState.copy(errorMessage = e.message)
                    }
                }
            }
        }
    }
}
