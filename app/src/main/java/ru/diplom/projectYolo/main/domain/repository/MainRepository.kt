package ru.diplom.projectYolo.main.domain.repository

import ru.diplom.projectYolo.main.domain.model.CachedFile

/**
 * Интерфейс `MainRepository` определяет контракт для отправки вложений.
 */
interface MainRepository {
    /**
     * Сuspend-функция для отправки вложений.
     *
     * @param attachments Список объектов `CachedFile`, представляющих вложенные файлы для отправки.
     * @return Результат операции в виде строки, возвращаемой после успешной отправки.
     */
    suspend fun sendAttachments(attachments: List<CachedFile>): String
}

