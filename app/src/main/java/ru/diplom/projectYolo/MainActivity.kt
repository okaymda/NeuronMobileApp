package ru.diplom.projectYolo

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.diplom.projectYolo.main.ui.component.NewAttachment
import ru.diplom.projectYolo.main.ui.events.MainEvents
import ru.diplom.projectYolo.main.ui.ext.cacheToFile
import ru.diplom.projectYolo.main.ui.viewmodel.MainViewModel
import java.util.Base64

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
    }
}

@Composable
fun MainScreen() {
    // Получаем ViewModel для управления состоянием экрана
    val mainViewModel: MainViewModel = viewModel()
    val state = mainViewModel.viewState

    // Основной контейнер экрана
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val context = LocalContext.current

        // Создаем launcher для получения результата от действия выбора файлов
        val fileLauncher =
            rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    // Получаем URI выбранных файлов
                    val resultUris = result.data?.run {
                        when (val uris = clipData) {
                            null -> listOfNotNull(data)
                            else -> (0 until uris.itemCount).mapNotNull { index -> uris.getItemAt(index).uri }
                        }
                    } ?: return@rememberLauncherForActivityResult

                    // Преобразуем URI файлов в File объекты и передаем их в ViewModel
                    val files = resultUris.mapNotNull { it.cacheToFile(context) }
                    mainViewModel.onEvent(MainEvents.PassAttachments(files))
                }
            }

        // Кнопка для выбора изображений
        Button(onClick = {
            fileLauncher.launch(Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "*/*"
                putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                state.errorMessage = ""
            })
        }) {
            Text(text = "Загрузить изображение", fontSize = 20.sp)
        }

        // Отображаем выбранные файлы в горизонтальном списке
        LazyRow {
            items(state.attachments) {
                NewAttachment(
                    title = it.name,
                    uri = it.uri,
                )
            }
        }

        // Кнопка для отправки выбранных изображений на сервер
        Button(onClick = {
            mainViewModel.onEvent(MainEvents.SendAttachments)
        }) {
            Text(text = "Отправить", fontSize = 20.sp)
        }

        // Отображаем сообщение об ошибке, если оно есть
        state.errorMessage?.let { errorMessage ->
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(8.dp)
            )
        }

        // Отображаем размеченное изображение, если оно есть
        state.annotatedImageBase64?.let { base64Image ->
            val annotatedImageBitmap = remember(base64Image) {
                // Декодируем base64 изображение в Bitmap
                val decodedBytes = Base64.getDecoder().decode(base64Image)
                BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
            }
            Image(
                bitmap = annotatedImageBitmap.asImageBitmap(),
                contentDescription = "Annotated Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
        }
    }
}