package ru.diplom.projectYolo.main.data.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import ru.diplom.projectYolo.main.data.api.MainApi
import ru.diplom.projectYolo.main.data.api.MainApiImpl

/**
 * Функция dataDiModule создает модуль зависимостей для слоя данных (data layer).
 *
 * @return DI.Module, представляющий модуль зависимостей для слоя данных.
 */
fun dataDiModule() = DI.Module("dataDiModule") {
    // Связываем интерфейс MainApi с его реализацией MainApiImpl как singleton.
    bind<MainApi>() with singleton {
        MainApiImpl(
            instance() // Внедряем зависимость OkHttpClient, предоставленную DI
        )
    }
}
