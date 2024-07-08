package ru.diplom.projectYolo.main.data.api

import io.ktor.client.statement.HttpResponse
import ru.diplom.projectYolo.main.domain.model.CachedFile

/**
 * Интерфейс MainApi определяет контракт для выполнения операций с API, связанных с отправкой файлов.
 */
interface MainApi {

    /**
     * Функция sendAttachments предназначена для отправки файлов на сервер.
     * @param attachments Список файлов типа CachedFile, которые требуется отправить.
     * @return HttpResponse, содержащий ответ от сервера после отправки файлов.
     */
    suspend fun sendAttachments(attachments: List<CachedFile>): HttpResponse
}


