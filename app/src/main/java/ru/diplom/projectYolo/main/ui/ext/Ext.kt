package ru.diplom.projectYolo.main.ui.ext

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.core.net.toUri
import ru.diplom.projectYolo.main.domain.model.CachedFile
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

/**
 * Расширение для класса Uri, которое кэширует файл во временную директорию и возвращает информацию о кэшированном файле.
 *
 * @param context Контекст, необходимый для доступа к ContentResolver.
 * @return Информация о кэшированном файле в виде объекта CachedFile или null, если произошла ошибка.
 */

fun Uri.cacheToFile(context: Context): CachedFile? {
    var filename: String? = null
    var fileSize: Long? = null

    // Получение типа MIME для URI
    val mimeType = context.contentResolver.getType(this) ?: return null

    // Запрос информации о файле из ContentResolver
    context.contentResolver.query(this, null, null, null, null)?.use { cursor ->
        // Получение индексов для имени и размера файла
        val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)

        // Перемещение курсора к первой записи
        cursor.moveToFirst()

        // Получение имени и размера файла
        filename = cursor.getString(nameIndex)
        fileSize = cursor.getLong(sizeIndex)
    }

    // Если имя файла или размер не удалось получить, возвращаем null
    if (filename == null || fileSize == null)
        return null

    // Открытие дескриптора файла
    val parcelFileDescriptor = context.contentResolver.openFileDescriptor(this, "r", null) ?: return null

    // Создание нового файла во временной директории с полученным именем файла
    val cachedFile = File(context.cacheDir, filename!!)

    // Копирование содержимого оригинального файла в кэшированный файл
    FileOutputStream(cachedFile).use { cachedFileStream ->
        FileInputStream(parcelFileDescriptor.fileDescriptor).use { originalFileStream ->
            originalFileStream.copyTo(cachedFileStream)
        }
    }

    // Получение URI кэшированного файла
    val cachedFileUri = cachedFile.toUri()

    // Закрытие дескриптора файла
    parcelFileDescriptor.close()

    // Возвращение объекта CachedFile с информацией о кэшированном файле
    return CachedFile(
        size = fileSize!!,
        ext = cachedFile.extension,
        name = cachedFile.nameWithoutExtension,
        uri = cachedFileUri.toString(),
        type = mimeType,
        sourceFile = cachedFile.path
    )
}