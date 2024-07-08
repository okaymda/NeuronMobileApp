package ru.diplom.projectYolo.main.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.skydoves.landscapist.glide.GlideImage

/**
 * @Composable функция для отображения нового вложения.
 *
 * @param modifier модификатор для настройки внешнего вида и поведения компонента.
 * @param title название вложения.
 * @param uri URI изображения вложения, если имеется.
 * @param onClose действие, выполняемое при закрытии вложения (по умолчанию ничего не выполняет).
 */

@Composable
fun NewAttachment(modifier: Modifier = Modifier, title: String, uri: String? = null, onClose: () -> Unit = {}) {
    // Вертикальная колонка для расположения элементов вложения
    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        // Контейнер для изображения с вложением
        Box(Modifier, contentAlignment = Alignment.TopEnd) {
            // Круглый контейнер с фоном
            Box(
                Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                // Если URI изображения предоставлен, отображаем изображение с использованием GlideImage
                if (uri != null)
                    GlideImage(
                        modifier = Modifier.fillMaxSize(),
                        imageModel = uri,
                        contentScale = ContentScale.Crop
                    )
            }
        }
        // Добавляем отступ между изображением и текстом
        Spacer(modifier = Modifier.height(8.dp))
        // Отображение названия вложения
        Text(
            textAlign = TextAlign.Center,
            modifier = Modifier.widthIn(max = 40.dp),
            text = title,
            style = MaterialTheme.typography.titleSmall,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}