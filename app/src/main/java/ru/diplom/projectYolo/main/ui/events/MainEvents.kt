package ru.diplom.projectYolo.main.ui.events

import ru.diplom.projectYolo.main.domain.model.CachedFile

/**
 * Sealed class для представления событий в MainScreen.
 * Содержит два типа событий: PassAttachments и SendAttachments.
 */
sealed class MainEvents {
    /**
     * Событие для передачи списка вложений.
     *
     * attachments Список файлов, которые нужно передать.
     */
    data class PassAttachments(val attachments: List<CachedFile>) : MainEvents()

    /**
     * Событие для отправки вложений на сервер.
     */
    data object SendAttachments : MainEvents()
}
