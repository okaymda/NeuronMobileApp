package ru.diplom.projectYolo.main.domain.repository

import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import ru.diplom.projectYolo.main.data.api.MainApi
import ru.diplom.projectYolo.main.domain.model.CachedFile

/**
 * Реализация интерфейса `MainRepository`, обеспечивающая отправку вложенных файлов через `MainApi`.
 *
 * @property api API-интерфейс для выполнения запросов на сервер.
 */
class MainRepositoryImpl(val api: MainApi) : MainRepository {

    /**
    * Асинхронно отправляет вложенные файлы на сервер.
    *
    * @param attachments Список объектов `CachedFile`, представляющих вложенные файлы для отправки.
    * @return Результат операции в виде строки, возвращаемой после успешной отправки.
    * @throws Exception в случае неудачного ответа от сервера, содержащая информацию о причине ошибки.
    */

    override suspend fun sendAttachments(attachments: List<CachedFile>): String {
        // Выполняем запрос на отправку вложений через API
        val response = api.sendAttachments(attachments)

        // Проверяем успешность ответа
        if (response.status.isSuccess()) {
            // Если ответ успешный, возвращаем тело ответа в виде строки
            return response.bodyAsText()
        } else {
            // Если ответ неуспешный, извлекаем сообщение об ошибке из тела ответа
            val errorMessage = response.bodyAsText()
            // Выбрасываем исключение с деталями ошибки
            throw Exception("Failed to upload images: ${response.status.value} - $errorMessage")
        }
    }
}