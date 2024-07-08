package ru.diplom.projectYolo.main.domain.model

/**
 * Data класс `CachedFile` представляет информацию о кэшированном файле.
 *
 * @property sourceFile Путь к исходному файлу.
 * @property uri URI файла в виде строки.
 * @property name Имя файла без расширения.
 * @property ext Расширение файла.
 * @property size Размер файла в байтах.
 * @property type MIME-тип файла.
 */

data class CachedFile(
    val sourceFile: String,
    val uri: String,
    val name: String,
    val ext: String,
    val size: Long,
    val type: String
)