package ru.diplom.projectYolo.main.data.api

import io.ktor.client.HttpClient
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import okio.FileSystem
import okio.Path.Companion.toPath
import ru.diplom.projectYolo.main.domain.model.CachedFile

/**
 * Класс MainApiImpl реализует интерфейс MainApi для выполнения операций с API.
 * @param client HttpClient, используемый для выполнения HTTP-запросов.
 */
class MainApiImpl(val client: HttpClient) : MainApi {

    /**
     * Функция sendAttachments выполняет отправку файлов на сервер через HTTP POST запрос.
     * @param attachments Список файлов, которые требуется отправить.
     * @return HttpResponse, содержащий ответ от сервера после отправки файлов.
     */
    override suspend fun sendAttachments(attachments: List<CachedFile>): HttpResponse {
        return client.post("/uploadImage") {
            // Устанавливаем тело запроса как MultiPartFormDataContent для отправки множественных частей формы
            setBody(
                MultiPartFormDataContent(
                    formData {
                        attachments.forEach {
                            // Читаем содержимое файла в виде массива байтов
                            val array = FileSystem.SYSTEM.read(it.sourceFile.toPath()) {
                                readByteArray()
                            }
                            // Добавляем файл в форму как часть MultiPartFormDataContent
                            append("files", array, Headers.build {
                                append(HttpHeaders.ContentType, it.type) // Устанавливаем тип содержимого
                                append(
                                    HttpHeaders.ContentDisposition,
                                    "filename=${it.name}.${it.ext}" // Устанавливаем имя файла в заголовке Content-Disposition
                                )
                            })
                        }
                    }
                )
            )
        }
    }
}

