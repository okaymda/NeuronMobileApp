package ru.diplom.projectYolo.main.domain.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import ru.diplom.projectYolo.main.data.di.dataDiModule
import ru.diplom.projectYolo.main.domain.repository.MainRepository
import ru.diplom.projectYolo.main.domain.repository.MainRepositoryImpl

/**
 * Функция domainDiModule создает модуль зависимостей для слоя домена (domain).
 *
 * @return DI.Module, представляющий модуль зависимостей для слоя домена.
 */
fun domainDiModule() = DI.Module("DomainDiModule") {
    // Импортируем однократно модуль зависимостей для слоя данных (data layer).
    importOnce(dataDiModule())

    // Связываем интерфейс MainRepository с его реализацией MainRepositoryImpl как singleton.
    bind<MainRepository>() with singleton {
        MainRepositoryImpl(
            instance() // Внедряем зависимость MainApi, предоставленную DI
        )
    }
}