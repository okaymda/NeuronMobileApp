package ru.diplom.projectYolo.main.ui.di

import org.kodein.di.DI
import ru.diplom.projectYolo.data.di.dataDi
import ru.diplom.projectYolo.main.domain.di.domainDiModule

/**
 * Функция для инициализации и настройки Dependency Injection (DI) контейнера.
 *
 * @return настроенный DI контейнер.
 */

fun mainDi() = DI {
    // Расширяем базовый DI модуль данными из dataDi
    extend(dataDi)

    // Импортируем модуль домена, чтобы добавить зависимости, связанные с бизнес-логикой
    importOnce(domainDiModule())
}
